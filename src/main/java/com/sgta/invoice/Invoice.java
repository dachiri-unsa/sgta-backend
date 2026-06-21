package com.sgta.invoice;

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
import com.sgta.workorder.model.WorkOrder;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workOrderId", nullable = false)
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    @Column(name = "number", nullable = false, updatable = true, unique = true, length = 50)
    private String number;

    @Column(name = "receiptType", nullable = false, updatable = true, length = 50)
    private String receiptType;

    @Column(name = "subtotal", nullable = false, updatable = true, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax", nullable = false, updatable = true, precision = 12, scale = 2)
    private BigDecimal tax;

    @Column(name = "total", nullable = false, updatable = true, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "issueDate", nullable = false, updatable = true)
    private LocalDateTime issueDate;

    @Column(name = "status", nullable = false)
    private boolean status;
}
