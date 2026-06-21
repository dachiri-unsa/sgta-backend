package com.sgta.vehicle;

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

import com.sgta.customer.Customer;
import com.sgta.shared.model.audit.Auditable;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    @Column(name = "licensePlate", nullable = false, updatable = true, unique = true, length = 20)
    private String licensePlate;

    @Column(name = "brand", nullable = false, updatable = true, length = 100)
    private String brand;

    @Column(name = "model", nullable = false, updatable = true, length = 100)
    private String model;

    @Column(name = "year", updatable = true)
    private Short year;

    @Column(name = "color", updatable = true, length = 50)
    private String color;

    @Column(name = "vin", updatable = true, unique = true, length = 100)
    private String vin;

    @Column(name = "mileage", nullable = false, updatable = true)
    private Integer mileage;

    @Column(name = "fuelType", updatable = true, length = 50)
    private String fuelType;

    @Column(name = "transmission", updatable = true, length = 50)
    private String transmission;

    @Column(name = "status", nullable = false)
    private boolean status;
}
