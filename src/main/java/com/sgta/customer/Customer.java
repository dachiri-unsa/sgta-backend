package com.sgta.customer;

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

import com.sgta.shared.model.audit.Auditable;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "firstName", nullable = false, updatable = true, length = 100)
    private String firstName;

    @Column(name = "lastName", nullable = false, updatable = true, length = 100)
    private String lastName;

    @Column(name = "documentNumber", nullable = false, updatable = true, unique = true, length = 20)
    private String documentNumber;

    @Column(name = "phone", updatable = true, length = 20)
    private String phone;

    @Column(name = "email", updatable = true, unique = true, length = 150)
    private String email;

    @Column(name = "address", updatable = true, columnDefinition = "TEXT")
    private String address;

    @Column(name = "status", nullable = false)
    private boolean status;
}
