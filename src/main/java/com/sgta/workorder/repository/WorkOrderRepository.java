package com.sgta.workorder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sgta.workorder.model.WorkOrder;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    Page<WorkOrder> findByStatusTrue(Pageable pageable);

    @Query("""
            SELECT w FROM WorkOrder w
            JOIN FETCH w.customer
            JOIN FETCH w.vehicle
            LEFT JOIN FETCH w.mechanicUser
            WHERE w.status = true
            """)
    Page<WorkOrder> findEnriched(Pageable pageable);

    Optional<WorkOrder> findByIdAndStatusTrue(Long id);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    @Query("""
            SELECT COUNT(w) FROM WorkOrder w
            WHERE w.status = true
            AND (:mechanicUserId IS NULL OR w.mechanicUser.id = :mechanicUserId)
            """)
    long countByStatusTrue(@Param("mechanicUserId") Long mechanicUserId);

    @Query("""
            SELECT COUNT(w) FROM WorkOrder w
            WHERE w.status = true AND w.deliveryDate IS NOT NULL
            AND (:mechanicUserId IS NULL OR w.mechanicUser.id = :mechanicUserId)
            """)
    long countCompleted(@Param("mechanicUserId") Long mechanicUserId);

    @Query("""
            SELECT COUNT(w) FROM WorkOrder w
            WHERE w.status = true AND w.deliveryDate IS NULL
            AND (:mechanicUserId IS NULL OR w.mechanicUser.id = :mechanicUserId)
            """)
    long countPending(@Param("mechanicUserId") Long mechanicUserId);

    @Query("""
            SELECT w FROM WorkOrder w
            WHERE w.status = true
            AND (:mechanicUserId IS NULL OR w.mechanicUser.id = :mechanicUserId)
            ORDER BY w.intakeDate DESC
            """)
    List<WorkOrder> findTop5ByStatusTrueOrderByIntakeDateDesc(@Param("mechanicUserId") Long mechanicUserId);
}
