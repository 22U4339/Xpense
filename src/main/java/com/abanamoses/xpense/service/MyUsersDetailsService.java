package com.abanamoses.xpense.service;

import com.abanamoses.xpense.entities.Users;
import com.abanamoses.xpense.repositories.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyUsersDetailsService implements UserDetailsService {
    private final UsersRepo repo;

    @Override
    public Users loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user= repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User couldn't be Loaded"));

        return user;
    }
}