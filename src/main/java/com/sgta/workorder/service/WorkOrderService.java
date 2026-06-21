package com.sgta.workorder.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.sgta.customer.Customer;
import com.sgta.shared.exception.RecursoDuplicadoException;
import com.sgta.shared.exception.RecursoNoEncontradoException;
import com.sgta.user.User;
import com.sgta.vehicle.Vehicle;
import com.sgta.workorder.dto.workorder.CreateWorkOrderDto;
import com.sgta.workorder.dto.workorder.WorkOrderEnrichedResponseDto;
import com.sgta.workorder.dto.workorder.WorkOrderResponseDto;
import com.sgta.workorder.mapper.WorkOrderMapper;
import com.sgta.workorder.model.WorkOrder;
import com.sgta.workorder.repository.WorkOrderPartRepository;
import com.sgta.workorder.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderMapper workOrderMapper;
    private final WorkOrderPartRepository workOrderPartRepository;

    @Transactional(readOnly = true)
    public Page<WorkOrderResponseDto> findAll(Pageable pageable) {
        return workOrderRepository.findByStatusTrue(pageable)
                .map(workOrderMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<WorkOrderEnrichedResponseDto> findAllEnriched(Pageable pageable) {
        return workOrderRepository.findEnriched(pageable)
                .map(workOrderMapper::toEnrichedResponse);
    }

    @Transactional(readOnly = true)
    public WorkOrderResponseDto findById(Long id) {
        return workOrderMapper.toResponse(buscarActivo(id));
    }

    @Transactional
    public WorkOrderResponseDto create(CreateWorkOrderDto dto) {
        validarCodigoUnico(dto.code());
        WorkOrder workOrder = workOrderMapper.toEntity(dto);
        if (workOrder.getWorkOrderStatus() == null) {
            workOrder.setWorkOrderStatus("PENDIENTE");
        }
        setRelations(workOrder, dto);
        workOrder = workOrderRepository.save(workOrder);
        return workOrderMapper.toResponse(workOrder);
    }

    @Transactional
    public WorkOrderResponseDto update(Long id, CreateWorkOrderDto dto) {
        WorkOrder workOrder = buscarActivo(id);

        if (!workOrder.getCode().equals(dto.code())
                && workOrderRepository.existsByCodeAndIdNot(dto.code(), id)) {
            throw new RecursoDuplicadoException("code", dto.code());
        }

        setRelations(workOrder, dto);
        workOrderMapper.update(dto, workOrder);

        workOrder = workOrderRepository.save(workOrder);
        return workOrderMapper.toResponse(workOrder);
    }

    @Transactional
    public void delete(Long id) {
        WorkOrder workOrder = buscarActivo(id);
        workOrder.setStatus(false);
        workOrderRepository.save(workOrder);
    }


    @Transactional
    public void recalcularTotales(Long id) {
        WorkOrder wo = buscarActivo(id);
        BigDecimal totalParts = workOrderPartRepository.sumSubtotalByWorkOrderId(id);
        wo.setTotalParts(totalParts);
        BigDecimal subtotal = totalParts.add(wo.getTotalLabor() != null ? wo.getTotalLabor() : BigDecimal.ZERO);
        wo.setSubtotal(subtotal);
        BigDecimal igvRate = BigDecimal.valueOf(0.18);
        if (wo.getSubtotal() != null && wo.getSubtotal().compareTo(BigDecimal.ZERO) > 0
                && wo.getIgv() != null && wo.getIgv().compareTo(BigDecimal.ZERO) > 0) {
            igvRate = wo.getIgv().divide(wo.getSubtotal(), 4, RoundingMode.HALF_UP);
        }
        BigDecimal igv = subtotal.multiply(igvRate).setScale(2, RoundingMode.HALF_UP);
        wo.setIgv(igv);
        wo.setTotal(subtotal.add(igv));
        workOrderRepository.save(wo);
    }

    private WorkOrder buscarActivo(Long id) {
        WorkOrder workOrder = workOrderRepository.findByIdAndStatusTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("WorkOrder", id));
        return workOrder;
    }

    private void validarCodigoUnico(String code) {
        if (workOrderRepository.existsByCode(code)) {
            throw new RecursoDuplicadoException("code", code);
        }
    }

    private void setRelations(WorkOrder workOrder, CreateWorkOrderDto dto) {
        Customer customer = new Customer();
        customer.setId(dto.customerId());
        workOrder.setCustomer(customer);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(dto.vehicleId());
        workOrder.setVehicle(vehicle);

        if (dto.mechanicUserId() != null) {
            User mechanic = new User();
            mechanic.setId(dto.mechanicUserId());
            workOrder.setMechanicUser(mechanic);
        } else {
            workOrder.setMechanicUser(null);
        }
    }
}
