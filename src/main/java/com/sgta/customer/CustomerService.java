package com.sgta.customer;

import java.util.List;

import com.sgta.customer.dto.CreateCustomerDto;
import com.sgta.customer.dto.CustomerMinimalDto;
import com.sgta.customer.dto.CustomerResponseDto;
import com.sgta.shared.exception.RecursoDuplicadoException;
import com.sgta.shared.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> findAll(Pageable pageable) {
        return customerRepository.findByStatusTrue(pageable)
                .map(customerMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<CustomerMinimalDto> findAllMinimal() {
        return customerRepository.findAllMinimal();
    }

    @Transactional(readOnly = true)
    public List<CustomerMinimalDto> searchMinimal(String q) {
        if (q == null || q.isBlank()) {
            return customerRepository.findTopMinimal(PageRequest.of(0, 20));
        }
        return customerRepository.searchMinimal(q, PageRequest.of(0, 20));
    }

    @Transactional(readOnly = true)
    public CustomerResponseDto findById(Long id) {
        return customerMapper.toResponse(buscarActivo(id));
    }

    @Transactional
    public CustomerResponseDto create(CreateCustomerDto dto) {
        validarCamposUnicos(dto.email(), dto.documentNumber());
        Customer customer = customerMapper.toEntity(dto);
        customer = customerRepository.save(customer);
        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponseDto update(Long id, CreateCustomerDto dto) {
        Customer customer = buscarActivo(id);

        if (!customer.getEmail().equals(dto.email())
                && customerRepository.existsByEmail(dto.email())) {
            throw new RecursoDuplicadoException("email", dto.email());
        }
        if (!customer.getDocumentNumber().equals(dto.documentNumber())
                && customerRepository.existsByDocumentNumber(dto.documentNumber())) {
            throw new RecursoDuplicadoException("documentNumber", dto.documentNumber());
        }

        customerMapper.update(dto, customer);

        customer = customerRepository.save(customer);
        return customerMapper.toResponse(customer);
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = buscarActivo(id);
        customer.setStatus(false);
        customerRepository.save(customer);
    }

    private Customer buscarActivo(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Customer", id));
        if (!customer.isStatus()) {
            throw new RecursoNoEncontradoException("Customer", id);
        }
        return customer;
    }

    private void validarCamposUnicos(String email, String documentNumber) {
        if (customerRepository.existsByEmail(email)) {
            throw new RecursoDuplicadoException("email", email);
        }
        if (customerRepository.existsByDocumentNumber(documentNumber)) {
            throw new RecursoDuplicadoException("documentNumber", documentNumber);
        }
    }
}
