package com.sgta.workorder.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sgta.workorder.model.WorkOrderPart;

public interface WorkOrderPartRepository extends JpaRepository<WorkOrderPart, Long> {

    Page<WorkOrderPart> findByStatusTrue(Pageable pageable);

    Page<WorkOrderPart> findByWorkOrderIdAndStatusTrue(Long workOrderId, Pageable pageable);

    Optional<WorkOrderPart> findByIdAndStatusTrue(Long id);

    @Query("SELECT COALESCE(SUM(wp.subtotal), 0) FROM WorkOrderPart wp WHERE wp.workOrder.id = :workOrderId AND wp.status = true")
    BigDecimal sumSubtotalByWorkOrderId(@Param("workOrderId") Long workOrderId);
}
