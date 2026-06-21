package com.sgta.invoice;

import java.time.Year;

import com.sgta.customer.CustomerRepository;
import com.sgta.invoice.dto.CreateInvoiceDto;
import com.sgta.invoice.dto.InvoiceEnrichedResponseDto;
import com.sgta.invoice.dto.InvoiceResponseDto;
import com.sgta.shared.exception.RecursoDuplicadoException;
import com.sgta.shared.exception.RecursoNoEncontradoException;
import com.sgta.workorder.model.WorkOrder;
import com.sgta.workorder.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final WorkOrderRepository workOrderRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public Page<InvoiceResponseDto> findAll(Pageable pageable) {
        return invoiceRepository.findByStatusTrue(pageable)
                .map(invoiceMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<InvoiceEnrichedResponseDto> findAllEnriched(Pageable pageable) {
        return invoiceRepository.findEnriched(pageable)
                .map(invoiceMapper::toEnrichedResponse);
    }

    @Transactional(readOnly = true)
    public InvoiceResponseDto findById(Long id) {
        return invoiceMapper.toResponse(buscarActivo(id));
    }

    @Transactional
    public InvoiceResponseDto create(CreateInvoiceDto dto) {
        validarNumeroUnico(dto.number());
        Invoice invoice = invoiceMapper.toEntity(dto);
        invoice.setWorkOrder(workOrderRepository.getReferenceById(dto.workOrderId()));
        invoice.setCustomer(customerRepository.getReferenceById(dto.customerId()));
        invoice = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(invoice);
    }

    @Transactional
    public InvoiceResponseDto generateFromWorkOrder(Long workOrderId, CreateInvoiceDto dto) {
        validarNumeroUnico(dto.number());

        WorkOrder workOrder = workOrderRepository.findByIdAndStatusTrue(workOrderId)
                .orElseThrow(() -> new RecursoNoEncontradoException("WorkOrder", workOrderId));

        Invoice invoice = invoiceMapper.toEntity(dto);
        invoice.setWorkOrder(workOrder);
        invoice.setCustomer(workOrder.getCustomer());
        invoice = invoiceRepository.save(invoice);

        workOrder.setWorkOrderStatus("FACTURADA");
        workOrderRepository.save(workOrder);

        return invoiceMapper.toResponse(invoice);
    }

    @Transactional(readOnly = true)
    public String getNextNumber() {
        String prefix = "F-" + Year.now().getValue() + "-";
        String max = invoiceRepository.findMaxNumberByPrefix(prefix).orElse(null);
        int nextSeq = 1;
        if (max != null) {
            String seqPart = max.substring(prefix.length());
            try {
                nextSeq = Integer.parseInt(seqPart) + 1;
            } catch (NumberFormatException ignored) {}
        }
        return prefix + String.format("%04d", nextSeq);
    }

    @Transactional
    public InvoiceResponseDto update(Long id, CreateInvoiceDto dto) {
        Invoice invoice = buscarActivo(id);

        if (!invoice.getNumber().equals(dto.number())
                && invoiceRepository.existsByNumber(dto.number())) {
            throw new RecursoDuplicadoException("number", dto.number());
        }

        invoice.setWorkOrder(workOrderRepository.getReferenceById(dto.workOrderId()));
        invoice.setCustomer(customerRepository.getReferenceById(dto.customerId()));
        invoiceMapper.update(dto, invoice);

        invoice = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(invoice);
    }

    @Transactional
    public void delete(Long id) {
        Invoice invoice = buscarActivo(id);
        WorkOrder wo = invoice.getWorkOrder();
        if (wo != null && "FACTURADA".equals(wo.getWorkOrderStatus())) {
            wo.setWorkOrderStatus("COMPLETADA");
            workOrderRepository.save(wo);
        }
        invoice.setStatus(false);
        invoiceRepository.save(invoice);
    }

    private Invoice buscarActivo(Long id) {
        return invoiceRepository.findByIdAndStatusTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Invoice", id));
    }

    private void validarNumeroUnico(String number) {
        if (invoiceRepository.existsByNumber(number)) {
            throw new RecursoDuplicadoException("number", number);
        }
    }
}
