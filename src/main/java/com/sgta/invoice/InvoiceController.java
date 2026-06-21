package com.sgta.invoice;

import com.sgta.invoice.dto.CreateInvoiceDto;
import com.sgta.invoice.dto.InvoiceEnrichedResponseDto;
import com.sgta.invoice.dto.InvoiceResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/next-number")
    public ResponseEntity<Map<String, String>> getNextNumber() {
        return ResponseEntity.ok(Map.of("nextNumber", invoiceService.getNextNumber()));
    }

    @GetMapping
    public ResponseEntity<Page<InvoiceResponseDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(invoiceService.findAll(pageable));
    }

    @GetMapping("/enriched")
    public ResponseEntity<Page<InvoiceEnrichedResponseDto>> findAllEnriched(Pageable pageable) {
        return ResponseEntity.ok(invoiceService.findAllEnriched(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.findById(id));
    }

    @PostMapping
    public ResponseEntity<InvoiceResponseDto> create(@Valid @RequestBody CreateInvoiceDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(invoiceService.create(dto));
    }

    @PostMapping("/from-work-order/{workOrderId}")
    public ResponseEntity<InvoiceResponseDto> createFromWorkOrder(
            @PathVariable Long workOrderId,
            @Valid @RequestBody CreateInvoiceDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(invoiceService.generateFromWorkOrder(workOrderId, dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> update(@PathVariable Long id, @Valid @RequestBody CreateInvoiceDto dto) {
        return ResponseEntity.ok(invoiceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        invoiceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
