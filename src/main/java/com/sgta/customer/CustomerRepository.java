package com.sgta.customer;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sgta.customer.dto.CustomerMinimalDto;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    boolean existsByDocumentNumber(String documentNumber);

    Page<Customer> findByStatusTrue(Pageable pageable);

    long countByStatusTrue();

    @Query("""
            SELECT new com.sgta.customer.dto.CustomerMinimalDto(c.id, c.firstName, c.lastName)
            FROM Customer c WHERE c.status = true
            ORDER BY c.firstName, c.lastName
            """)
    List<CustomerMinimalDto> findAllMinimal();

    @Query("""
            SELECT new com.sgta.customer.dto.CustomerMinimalDto(c.id, c.firstName, c.lastName)
            FROM Customer c WHERE c.status = true
            ORDER BY c.firstName, c.lastName
            """)
    List<CustomerMinimalDto> findTopMinimal(Pageable pageable);

    @Query("""
            SELECT new com.sgta.customer.dto.CustomerMinimalDto(c.id, c.firstName, c.lastName)
            FROM Customer c
            WHERE c.status = true
            AND (LOWER(c.firstName) LIKE LOWER(CONCAT('%', :q, '%'))
                 OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :q, '%'))
                 OR LOWER(CONCAT(c.firstName, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY c.firstName, c.lastName
            """)
    List<CustomerMinimalDto> searchMinimal(@Param("q") String q, Pageable pageable);
}
