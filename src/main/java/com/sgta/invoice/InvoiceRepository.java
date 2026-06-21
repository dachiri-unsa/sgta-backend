package com.sgta.invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Page<Invoice> findByStatusTrue(Pageable pageable);

    Optional<Invoice> findByIdAndStatusTrue(Long id);

    boolean existsByNumber(String number);

    boolean existsByNumberAndIdNot(String number, Long id);

    long countByStatusTrue();

    @Query("""
            SELECT i FROM Invoice i JOIN FETCH i.customer JOIN FETCH i.workOrder
            WHERE i.status = true
            """)
    Page<Invoice> findEnriched(Pageable pageable);

    @Query("SELECT MAX(i.number) FROM Invoice i WHERE i.number LIKE :prefix%")
    Optional<String> findMaxNumberByPrefix(String prefix);
}
