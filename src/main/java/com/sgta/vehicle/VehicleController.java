package com.sgta.vehicle;

import java.util.List;

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

import com.sgta.vehicle.dto.CreateVehicleDto;
import com.sgta.vehicle.dto.VehicleEnrichedResponseDto;
import com.sgta.vehicle.dto.VehicleMinimalDto;
import com.sgta.vehicle.dto.VehicleResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<Page<VehicleResponseDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(vehicleService.findAll(pageable));
    }

    @GetMapping("/enriched")
    public ResponseEntity<Page<VehicleEnrichedResponseDto>> findAllEnriched(Pageable pageable) {
        return ResponseEntity.ok(vehicleService.findAllEnriched(pageable));
    }

    @GetMapping("/minimal")
    public ResponseEntity<List<VehicleMinimalDto>> findAllMinimal() {
        return ResponseEntity.ok(vehicleService.findAllMinimal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<VehicleResponseDto> create(@Valid @RequestBody CreateVehicleDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehicleService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> update(@PathVariable Long id, @Valid @RequestBody CreateVehicleDto dto) {
        return ResponseEntity
                .ok(vehicleService
                .update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
