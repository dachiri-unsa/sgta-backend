package com.sgta.part;

import com.sgta.part.dto.CreatePartDto;
import com.sgta.part.dto.PartResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parts")
@RequiredArgsConstructor
public class PartController {

    private final PartService partService;

    @GetMapping
    public ResponseEntity<Page<PartResponseDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(partService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(partService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PartResponseDto> create(@Valid @RequestBody CreatePartDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(partService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartResponseDto> update(@PathVariable Long id, @Valid @RequestBody CreatePartDto dto) {
        return ResponseEntity
                .ok(partService
                .update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        partService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
