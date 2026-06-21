package com.sgta.role.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sgta.role.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
