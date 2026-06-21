package com.sgta.part;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PartRepository extends JpaRepository<Part, Long> {

    boolean existsByCode(String code);

    Page<Part> findByStatusTrue(Pageable pageable);

    long countByStatusTrue();

    @Query("SELECT COUNT(p) FROM Part p WHERE p.status = true AND p.stock <= 0")
    long countOutOfStock();

    @Query("SELECT COUNT(p) FROM Part p WHERE p.status = true AND p.stock > 0 AND p.stock < p.minimumStock")
    long countLowStock();

    @Query("SELECT COUNT(p) FROM Part p WHERE p.status = true AND p.stock >= p.minimumStock")
    long countAdequateStock();
}
