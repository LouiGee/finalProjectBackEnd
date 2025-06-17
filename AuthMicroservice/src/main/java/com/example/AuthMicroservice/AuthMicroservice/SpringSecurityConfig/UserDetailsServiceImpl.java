package com.example.AuthMicroservice.AuthMicroservice.SpringSecurityConfig;

import com.example.AuthMicroservice.AuthMicroservice.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // marks this class as a service class
@RequiredArgsConstructor // automatically creates a constructor for this class
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    @Transactional //marks the method to participate in a database transaction
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return repository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }
}
