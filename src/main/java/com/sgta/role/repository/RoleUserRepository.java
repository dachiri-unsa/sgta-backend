package com.sgta.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sgta.role.model.RoleUser;

public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {

}
