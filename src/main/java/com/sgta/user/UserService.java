package com.sgta.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sgta.role.model.Role;
import com.sgta.role.model.RoleName;
import com.sgta.role.model.RoleUser;
import com.sgta.role.repository.RoleRepository;
import com.sgta.shared.exception.RecursoDuplicadoException;
import com.sgta.shared.exception.RecursoNoEncontradoException;
import com.sgta.user.dto.CreateUserDto;
import com.sgta.user.dto.UpdateUserDto;
import com.sgta.user.dto.UserMinimalDto;
import com.sgta.user.dto.UserResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository  userRepository;
    private final UserMapper      userMapper;
    private final RoleRepository  roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserResponseDto> findAll(Pageable pageable) {
        return userRepository.findByStatusTrue(pageable)
                .map(userMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<UserMinimalDto> findAllMinimal() {
        return userRepository.findAllMinimal();
    }

    @Transactional(readOnly = true)
    public List<UserMinimalDto> findMechanicsMinimal() {
        return userRepository.findMechanicsMinimal();
    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        return userMapper.toResponse(buscarActivo(id));
    }

    @Transactional
    public UserResponseDto create(CreateUserDto dto) {
        if (userRepository.existsByEmailAndStatusTrue(dto.email())) {
            throw new RecursoDuplicadoException("email", dto.email());
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));

        actualizarRoles(user, dto.roles());

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponseDto update(Long id, UpdateUserDto dto) {
        User user = buscarActivo(id);

        if (!user.getEmail().equals(dto.email())
                && userRepository.existsByEmailAndStatusTrue(dto.email())) {
            throw new RecursoDuplicadoException("email", dto.email());
        }

        userMapper.update(dto, user);

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        actualizarRoles(user, dto.roles());

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = buscarActivo(id);
        user.setStatus(false);
        userRepository.save(user);
    }

    private void actualizarRoles(User user, List<RoleName> roleNames) {
        Map<String, RoleUser> existingByRole = new HashMap<>();
        for (RoleUser ru : user.getRoleUsers()) {
            existingByRole.put(ru.getRole().getName(), ru);
        }

        Set<String> processed = new HashSet<>();

        for (RoleName roleName : roleNames) {
            processed.add(roleName.name());
            RoleUser ru = existingByRole.get(roleName.name());
            if (ru != null) {
                ru.setStatus(true);
            } else {
                Role role = roleRepository.findByName(roleName.name())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Role", roleName.name()));
                RoleUser newRu = new RoleUser();
                newRu.setUser(user);
                newRu.setRole(role);
                newRu.setStatus(true);
                user.getRoleUsers().add(newRu);
            }
        }

        for (RoleUser ru : user.getRoleUsers()) {
            if (!processed.contains(ru.getRole().getName())) {
                ru.setStatus(false);
            }
        }
    }

    private User buscarActivo(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("User", id));
        if (!user.isStatus()) {
            throw new RecursoNoEncontradoException("User", id);
        }
        return user;
    }
}
