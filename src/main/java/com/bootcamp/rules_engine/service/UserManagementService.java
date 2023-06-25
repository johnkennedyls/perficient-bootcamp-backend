package com.bootcamp.rules_engine.service;

import com.bootcamp.rules_engine.model.SecurityUser;
import com.bootcamp.rules_engine.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserManagementService implements UserDetailsService {
    private final UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findByEmail(username)
                .map(SecurityUser::new)
                .orElseThrow(()-> new UsernameNotFoundException("User name not found: "+username));
    }
}
