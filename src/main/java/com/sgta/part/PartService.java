package com.sgta.part;

import com.sgta.part.dto.CreatePartDto;
import com.sgta.part.dto.PartResponseDto;
import com.sgta.shared.exception.RecursoDuplicadoException;
import com.sgta.shared.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;
    private final PartMapper partMapper;

    @Transactional(readOnly = true)
    public Page<PartResponseDto> findAll(Pageable pageable) {
        return partRepository.findByStatusTrue(pageable)
                .map(partMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public PartResponseDto findById(Long id) {
        return partMapper.toResponse(buscarActivo(id));
    }

    @Transactional
    public PartResponseDto create(CreatePartDto dto) {
        validarCodigoUnico(dto.code());
        Part part = partMapper.toEntity(dto);
        part = partRepository.save(part);
        return partMapper.toResponse(part);
    }

    @Transactional
    public PartResponseDto update(Long id, CreatePartDto dto) {
        Part part = buscarActivo(id);

        if (!part.getCode().equals(dto.code())
                && partRepository.existsByCode(dto.code())) {
            throw new RecursoDuplicadoException("code", dto.code());
        }

        partMapper.update(dto, part);

        part = partRepository.save(part);
        return partMapper.toResponse(part);
    }

    @Transactional
    public void delete(Long id) {
        Part part = buscarActivo(id);
        part.setStatus(false);
        partRepository.save(part);
    }

    private Part buscarActivo(Long id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Part", id));
        if (!part.isStatus()) {
            throw new RecursoNoEncontradoException("Part", id);
        }
        return part;
    }

    private void validarCodigoUnico(String code) {
        if (partRepository.existsByCode(code)) {
            throw new RecursoDuplicadoException("code", code);
        }
    }
}
