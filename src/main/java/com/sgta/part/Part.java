package com.sgta.part;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import com.sgta.shared.model.audit.Auditable;

@Entity
@Table(name = "parts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Part extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, updatable = true, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false, updatable = true, length = 150)
    private String name;

    @Column(name = "description", updatable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", updatable = true, length = 100)
    private String category;

    @Column(name = "brand", updatable = true, length = 100)
    private String brand;

    @Column(name = "stock", nullable = false, updatable = true)
    private Integer stock;

    @Column(name = "minimumStock", nullable = false, updatable = true)
    private Integer minimumStock;

    @Column(name = "location", updatable = true, length = 100)
    private String location;

    @Column(name = "purchasePrice", nullable = false, updatable = true, precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "salePrice", nullable = false, updatable = true, precision = 12, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "status", nullable = false)
    private boolean status;
}
