package com.sgta.security.user;

import org.springframework.security.core.userdetails.UserDetails;  
import org.springframework.security.core.userdetails.UserDetailsService;  
import org.springframework.security.core.userdetails.UsernameNotFoundException;  
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sgta.role.model.RoleUser;
import com.sgta.user.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String gmail) throws UsernameNotFoundException {

        var user = userRepository.findByEmailAndStatusTrue(gmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.isStatus() == false) {
            throw new UsernameNotFoundException("User not found");
        }

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRoleUsers().stream()
                        .filter(RoleUser::isStatus)
                        .map(ru -> ru.getRole().getName())
                        .toList(),
                user.isStatus()
        );
    }
}
