package com.sgta.customer;

import java.util.List;

import com.sgta.customer.dto.CreateCustomerDto;
import com.sgta.customer.dto.CustomerMinimalDto;
import com.sgta.customer.dto.CustomerResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(customerService.findAll(pageable));
    }

    @GetMapping("/minimal")
    public ResponseEntity<List<CustomerMinimalDto>> findAllMinimal() {
        return ResponseEntity.ok(customerService.findAllMinimal());
    }

    @GetMapping("/minimal-search")
    public ResponseEntity<List<CustomerMinimalDto>> searchMinimal(@RequestParam(defaultValue = "") String q) {
        return ResponseEntity.ok(customerService.searchMinimal(q));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> create(@Valid @RequestBody CreateCustomerDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> update(@PathVariable Long id, @Valid @RequestBody CreateCustomerDto dto) {
        return ResponseEntity
                .ok(customerService
                .update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
