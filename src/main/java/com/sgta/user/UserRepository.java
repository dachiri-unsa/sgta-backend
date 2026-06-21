package com.sgta.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sgta.user.dto.UserMinimalDto;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndStatusTrue(String email);

    boolean existsByEmailAndStatusTrue(String email);

    Page<User> findByStatusTrue(Pageable pageable);

    long countByStatusTrue();

    @Query("""
            SELECT new com.sgta.user.dto.UserMinimalDto(u.id, u.name)
            FROM User u WHERE u.status = true
            ORDER BY u.name
            """)
    List<UserMinimalDto> findAllMinimal();

    @Query("""
            SELECT new com.sgta.user.dto.UserMinimalDto(u.id, u.name)
            FROM User u
            WHERE u.status = true
            AND EXISTS (
                SELECT 1 FROM RoleUser ru
                WHERE ru.user.id = u.id
                AND ru.role.name = 'MECHANIC'
                AND ru.status = true
            )
            ORDER BY u.name
            """)
    List<UserMinimalDto> findMechanicsMinimal();
}
