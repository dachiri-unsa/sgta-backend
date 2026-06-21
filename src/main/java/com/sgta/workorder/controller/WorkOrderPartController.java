package com.sgta.workorder.controller;

import com.sgta.workorder.dto.workorderpart.CreateWorkOrderPartDto;
import com.sgta.workorder.dto.workorderpart.WorkOrderPartResponseDto;
import com.sgta.workorder.service.WorkOrderPartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-order-parts")
@RequiredArgsConstructor
public class WorkOrderPartController {

    private final WorkOrderPartService workOrderPartService;

    @GetMapping
    public ResponseEntity<Page<WorkOrderPartResponseDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(workOrderPartService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderPartResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(workOrderPartService.findById(id));
    }

    @GetMapping("/by-work-order/{workOrderId}")
    public ResponseEntity<Page<WorkOrderPartResponseDto>> findByWorkOrderId(@PathVariable Long workOrderId, Pageable pageable) {
        return ResponseEntity.ok(workOrderPartService.findByWorkOrderId(workOrderId, pageable));
    }

    @PostMapping
    public ResponseEntity<WorkOrderPartResponseDto> create(@Valid @RequestBody CreateWorkOrderPartDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(workOrderPartService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkOrderPartResponseDto> update(@PathVariable Long id, @Valid @RequestBody CreateWorkOrderPartDto dto) {
        return ResponseEntity.ok(workOrderPartService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workOrderPartService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
