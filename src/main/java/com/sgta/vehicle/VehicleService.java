package com.sgta.vehicle;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sgta.customer.CustomerRepository;
import com.sgta.shared.exception.RecursoDuplicadoException;
import com.sgta.shared.exception.RecursoNoEncontradoException;
import com.sgta.vehicle.dto.CreateVehicleDto;
import com.sgta.vehicle.dto.VehicleEnrichedResponseDto;
import com.sgta.vehicle.dto.VehicleMinimalDto;
import com.sgta.vehicle.dto.VehicleResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final VehicleMapper vehicleMapper;

    @Transactional(readOnly = true)
    public Page<VehicleResponseDto> findAll(Pageable pageable) {
        return vehicleRepository.findByStatusTrue(pageable)
                .map(vehicleMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<VehicleEnrichedResponseDto> findAllEnriched(Pageable pageable) {
        return vehicleRepository.findEnriched(pageable)
                .map(vehicleMapper::toEnrichedResponse);
    }

    @Transactional(readOnly = true)
    public List<VehicleMinimalDto> findAllMinimal() {
        return vehicleRepository.findAllMinimal();
    }

    @Transactional(readOnly = true)
    public VehicleResponseDto findById(Long id) {
        return vehicleMapper.toResponse(buscarActivo(id));
    }

    @Transactional
    public VehicleResponseDto create(CreateVehicleDto dto) {
        validarCamposUnicos(dto.licensePlate(), dto.vin());
        Vehicle vehicle = vehicleMapper.toEntity(dto);
        vehicle.setCustomer(customerRepository.getReferenceById(dto.customerId()));
        vehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(vehicle);
    }

    @Transactional
    public VehicleResponseDto update(Long id, CreateVehicleDto dto) {
        Vehicle vehicle = buscarActivo(id);

        if (!vehicle.getLicensePlate().equals(dto.licensePlate())
                && vehicleRepository.existsByLicensePlate(dto.licensePlate())) {
            throw new RecursoDuplicadoException("licensePlate", dto.licensePlate());
        }
        if (dto.vin() != null && !dto.vin().equals(vehicle.getVin())
                && vehicleRepository.existsByVin(dto.vin())) {
            throw new RecursoDuplicadoException("vin", dto.vin());
        }

        vehicle.setCustomer(customerRepository.getReferenceById(dto.customerId()));
        vehicleMapper.update(dto, vehicle);

        vehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(vehicle);
    }

    @Transactional
    public void delete(Long id) {
        Vehicle vehicle = buscarActivo(id);
        vehicle.setStatus(false);
        vehicleRepository.save(vehicle);
    }

    private Vehicle buscarActivo(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehicle", id));
        if (!vehicle.isStatus()) {
            throw new RecursoNoEncontradoException("Vehicle", id);
        }
        return vehicle;
    }

    private void validarCamposUnicos(String licensePlate, String vin) {
        if (vehicleRepository.existsByLicensePlate(licensePlate)) {
            throw new RecursoDuplicadoException("licensePlate", licensePlate);
        }
        if (vin != null && vehicleRepository.existsByVin(vin)) {
            throw new RecursoDuplicadoException("vin", vin);
        }
    }
}
