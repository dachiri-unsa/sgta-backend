package com.sgta.workorder.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.sgta.customer.Customer;
import com.sgta.shared.model.audit.Auditable;
import com.sgta.user.User;
import com.sgta.vehicle.Vehicle;

@Entity
@Table(name = "work_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrder extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, updatable = true, unique = true, length = 50)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicleId", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechanicUserId")
    private User mechanicUser;

    @Column(name = "vehicleStatus", updatable = true, length = 50)
    private String vehicleStatus;

    @Column(name = "priority", updatable = true, length = 20)
    private String priority;

    @Column(name = "intakeDate", nullable = false, updatable = true)
    private LocalDateTime intakeDate;

    @Column(name = "estimatedDate", updatable = true)
    private LocalDateTime estimatedDate;

    @Column(name = "deliveryDate", updatable = true)
    private LocalDateTime deliveryDate;

    @Column(name = "reportedProblem", nullable = false, updatable = true, columnDefinition = "TEXT")
    private String reportedProblem;

    @Column(name = "diagnosis", updatable = true, columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "observations", updatable = true, columnDefinition = "TEXT")
    private String observations;

    @Column(name = "intakeMileage", updatable = true)
    private Integer intakeMileage;

    @Column(name = "fuelLevel", updatable = true, length = 50)
    private String fuelLevel;

    @Column(name = "accessories", updatable = true, columnDefinition = "TEXT")
    private String accessories;

    @Column(name = "visibleDamage", updatable = true, columnDefinition = "TEXT")
    private String visibleDamage;

    @Column(name = "totalParts", nullable = false, updatable = true, precision = 12, scale = 2)
    private BigDecimal totalParts;

    @Column(name = "totalLabor", nullable = false, updatable = true, precision = 12, scale = 2)
    private BigDecimal totalLabor;

    @Column(name = "subtotal", nullable = false, updatable = true, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "igv", nullable = false, updatable = true, precision = 12, scale = 2)
    private BigDecimal igv;

    @Column(name = "total", nullable = false, updatable = true, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "workOrderStatus", length = 30)
    private String workOrderStatus;

    @Column(name = "status", nullable = false)
    private boolean status;
}
