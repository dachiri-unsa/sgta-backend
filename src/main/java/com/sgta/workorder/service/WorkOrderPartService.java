package com.sgta.workorder.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.sgta.part.Part;
import com.sgta.part.PartRepository;
import com.sgta.shared.exception.RecursoNoEncontradoException;
import com.sgta.workorder.dto.workorderpart.CreateWorkOrderPartDto;
import com.sgta.workorder.dto.workorderpart.WorkOrderPartResponseDto;
import com.sgta.workorder.mapper.WorkOrderPartMapper;
import com.sgta.workorder.model.WorkOrder;
import com.sgta.workorder.model.WorkOrderPart;
import com.sgta.workorder.repository.WorkOrderPartRepository;
import com.sgta.workorder.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkOrderPartService {

    private final WorkOrderPartRepository workOrderPartRepository;
    private final WorkOrderPartMapper workOrderPartMapper;
    private final WorkOrderRepository workOrderRepository;
    private final PartRepository partRepository;

    @Transactional(readOnly = true)
    public Page<WorkOrderPartResponseDto> findAll(Pageable pageable) {
        return workOrderPartRepository.findByStatusTrue(pageable)
                .map(workOrderPartMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<WorkOrderPartResponseDto> findByWorkOrderId(Long workOrderId, Pageable pageable) {
        return workOrderPartRepository.findByWorkOrderIdAndStatusTrue(workOrderId, pageable)
                .map(workOrderPartMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public WorkOrderPartResponseDto findById(Long id) {
        return workOrderPartMapper.toResponse(buscarActivo(id));
    }

    @Transactional
    public WorkOrderPartResponseDto create(CreateWorkOrderPartDto dto) {
        Part part = partRepository.findById(dto.partId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Part", dto.partId()));
        if (part.getStock() < dto.quantity()) {
            throw new IllegalArgumentException(String.format(
                    "Stock insuficiente para '%s' (código: %s): disponible %d, requerido %d",
                    part.getName(), part.getCode(), part.getStock(), dto.quantity()));
        }
        part.setStock(part.getStock() - dto.quantity());
        partRepository.save(part);

        WorkOrderPart workOrderPart = workOrderPartMapper.toEntity(dto);
        workOrderPart.setPart(part);
        workOrderPart.setWorkOrder(workOrderRepository.getReferenceById(dto.workOrderId()));
        workOrderPart = workOrderPartRepository.save(workOrderPart);
        recalcularWorkOrderTotales(dto.workOrderId());
        return workOrderPartMapper.toResponse(workOrderPart);
    }

    @Transactional
    public WorkOrderPartResponseDto update(Long id, CreateWorkOrderPartDto dto) {
        WorkOrderPart existing = buscarActivo(id);
        int oldQty = existing.getQuantity();
        Long oldPartId = existing.getPart().getId();
        int newQty = dto.quantity();
        Long newPartId = dto.partId();

        if (!oldPartId.equals(newPartId)) {
            Part oldPart = partRepository.findById(oldPartId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Part", oldPartId));
            oldPart.setStock(oldPart.getStock() + oldQty);
            partRepository.save(oldPart);

            Part newPart = partRepository.findById(newPartId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Part", newPartId));
            if (newPart.getStock() < newQty) {
                throw new IllegalArgumentException(String.format(
                        "Stock insuficiente para '%s' (código: %s): disponible %d, requerido %d",
                        newPart.getName(), newPart.getCode(), newPart.getStock(), newQty));
            }
            newPart.setStock(newPart.getStock() - newQty);
            partRepository.save(newPart);
            existing.setPart(newPart);
        } else {
            int diff = newQty - oldQty;
            if (diff != 0) {
                Part part = partRepository.findById(newPartId)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Part", newPartId));
                if (diff > 0 && part.getStock() < diff) {
                    throw new IllegalArgumentException(String.format(
                            "Stock insuficiente para '%s' (código: %s): disponible %d, requerido %d adicional",
                            part.getName(), part.getCode(), part.getStock(), diff));
                }
                part.setStock(part.getStock() - diff);
                partRepository.save(part);
            }
        }

        existing.setWorkOrder(workOrderRepository.getReferenceById(dto.workOrderId()));
        workOrderPartMapper.update(dto, existing);
        existing = workOrderPartRepository.save(existing);
        recalcularWorkOrderTotales(existing.getWorkOrder().getId());
        return workOrderPartMapper.toResponse(existing);
    }

    @Transactional
    public void delete(Long id) {
        WorkOrderPart workOrderPart = buscarActivo(id);
        Long workOrderId = workOrderPart.getWorkOrder().getId();

        Part part = partRepository.findById(workOrderPart.getPart().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Part", workOrderPart.getPart().getId()));
        part.setStock(part.getStock() + workOrderPart.getQuantity());
        partRepository.save(part);

        workOrderPart.setStatus(false);
        workOrderPartRepository.save(workOrderPart);
        recalcularWorkOrderTotales(workOrderId);
    }

    private void recalcularWorkOrderTotales(Long workOrderId) {
        WorkOrder wo = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RecursoNoEncontradoException("WorkOrder", workOrderId));
        BigDecimal totalParts = workOrderPartRepository.sumSubtotalByWorkOrderId(workOrderId);
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

    private WorkOrderPart buscarActivo(Long id) {
        return workOrderPartRepository.findByIdAndStatusTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("WorkOrderPart", id));
    }
}
