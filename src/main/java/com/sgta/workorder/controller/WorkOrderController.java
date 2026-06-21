package com.sgta.workorder.controller;

import com.sgta.workorder.dto.workorder.CreateWorkOrderDto;
import com.sgta.workorder.dto.workorder.WorkOrderEnrichedResponseDto;
import com.sgta.workorder.dto.workorder.WorkOrderResponseDto;
import com.sgta.workorder.service.WorkOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
public class    WorkOrderController {

    private final WorkOrderService workOrderService;

    @GetMapping
    public ResponseEntity<Page<WorkOrderResponseDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(workOrderService.findAll(pageable));
    }

    @GetMapping("/enriched")
    public ResponseEntity<Page<WorkOrderEnrichedResponseDto>> findAllEnriched(Pageable pageable) {
        return ResponseEntity.ok(workOrderService.findAllEnriched(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(workOrderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<WorkOrderResponseDto> create(@Valid @RequestBody CreateWorkOrderDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(workOrderService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkOrderResponseDto> update(@PathVariable Long id, @Valid @RequestBody CreateWorkOrderDto dto) {
        return ResponseEntity.ok(workOrderService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/recalculate")
    public ResponseEntity<Void> recalculate(@PathVariable Long id) {
        workOrderService.recalcularTotales(id);
        return ResponseEntity.noContent().build();
    }
}
