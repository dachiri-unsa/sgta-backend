package com.sgta.vehicle;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sgta.vehicle.dto.VehicleMinimalDto;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByLicensePlate(String licensePlate);

    boolean existsByVin(String vin);

    Page<Vehicle> findByStatusTrue(Pageable pageable);

    long countByStatusTrue();

    @Query("""
            SELECT v FROM Vehicle v JOIN FETCH v.customer
            WHERE v.status = true
            """)
    Page<Vehicle> findEnriched(Pageable pageable);

    @Query("""
            SELECT new com.sgta.vehicle.dto.VehicleMinimalDto(v.id, v.customer.id, v.licensePlate, v.brand, v.model)
            FROM Vehicle v WHERE v.status = true
            ORDER BY v.licensePlate
            """)
    List<VehicleMinimalDto> findAllMinimal();
}
