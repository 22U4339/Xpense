package com.abanamoses.xpense.controllers;


import com.abanamoses.xpense.dtos.AdminPasswordResetDto;
import com.abanamoses.xpense.dtos.ChangePasswordDto;
import com.abanamoses.xpense.dtos.TransactionDto;
import com.abanamoses.xpense.dtos.UsersDto;
import com.abanamoses.xpense.entities.Transaction;
import com.abanamoses.xpense.entities.Users;
import com.abanamoses.xpense.repositories.TransactionRepo;
import com.abanamoses.xpense.repositories.UsersRepo;
import com.abanamoses.xpense.service.JwtService;
import com.abanamoses.xpense.service.MyUsersDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class Home {
    private final UsersRepo usersRepo;
    private final TransactionRepo repo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping
    public ResponseEntity<?> home(@AuthenticationPrincipal Users user){
        List<Transaction> allRecords =  repo.findByUser_Id(user.getId());
        if(allRecords.isEmpty() || null == allRecords)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();



        return ResponseEntity.ok(allRecords
                .stream()
                .map(TransactionDto::fromTransactions)
                .toList());
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> sendToken(@RequestBody UsersDto userDto){
        Users user = usersRepo.findByEmail(userDto.getEmail())
                .orElseThrow();

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }

        String token = jwtService.generateToken(user);

        // ✅ Return JSON response
        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", user.getEmail(),
                "message", "Login successful"
        ));
    }


    @PostMapping("/auth/signup")
    public ResponseEntity<?> register(@RequestBody UsersDto usersDto){
        if (usersRepo.existsByEmail(usersDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email already registered"));
        }

        Users user = new Users();
        user.setEmail(usersDto.getEmail());
        user.setPassword(passwordEncoder.encode(usersDto.getPassword()));

        usersRepo.save(user);

        //generate token after signup
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(Map.of(
                "message", "Signup successful",
                "token", token,
                "email", user.getEmail()
        ));
    }

    @GetMapping("/auth/profile")
    public ResponseEntity<?> profile(@AuthenticationPrincipal Users user){
        return ResponseEntity.ok(Map.of("email", user.getEmail()));
    }

    @DeleteMapping("/auth/profile")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal Users user){
        usersRepo.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/auth/profile/reset")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal Users user, @RequestBody ChangePasswordDto changePasswordDto){
        if(!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String encoded = passwordEncoder.encode(changePasswordDto.getNewPassword());
        user.setPassword(encoded);
        usersRepo.save(user);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/admin/reset")
    public ResponseEntity<?> adminPassword(@AuthenticationPrincipal Users user, @RequestBody AdminPasswordResetDto adminPasswordResetDto, MyUsersDetailsService usersDetailsService){
        if(!(user.getRole() == "ADMIN")){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Users userToModified = usersDetailsService.loadUserByUsername(adminPasswordResetDto.getEmail());
        userToModified.setPassword( passwordEncoder.encode(adminPasswordResetDto.getPassword()));

        return ResponseEntity.ok().build();
    }

}
