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

import com.sgta.part.Part;
import com.sgta.shared.model.audit.Auditable;

@Entity
@Table(name = "parts_work_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderPart extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partId", nullable = false)
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workOrderId", nullable = false)
    private WorkOrder workOrder;

    @Column(name = "quantity", nullable = false, updatable = true)
    private Integer quantity;

    @Column(name = "unitPrice", nullable = false, updatable = true, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "subtotal", nullable = false, updatable = true, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "status", nullable = false)
    private boolean status;
}
