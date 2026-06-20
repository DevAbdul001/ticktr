package com.ticktr.app.Auth.Service;

import com.ticktr.app.Auth.DTOs.AuthResponseDTO;
import com.ticktr.app.Auth.DTOs.LoginDTO;
import com.ticktr.app.Auth.DTOs.RegisterDTO;
import com.ticktr.app.User.Services.UserService;
import com.ticktr.app.User.UserDTOs.UserResponseDTO;
import com.ticktr.app.User.Models.User;
import com.ticktr.app.User.Repositories.UserRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepo userRepo , PasswordEncoder passwordEncoder, UserService userService){
        this.userRepo = userRepo;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO create(@NonNull RegisterDTO dto){
        Optional<User> user = userRepo.findByEmail(dto.email());
        String passwordHash = passwordEncoder.encode(dto.password());
        User newUser = new User(
                dto.name(),
                dto.email(),
                passwordHash
        );

        userRepo.save(newUser);
        return new AuthResponseDTO(
                newUser.getId(),
                newUser.getName(),
                newUser.getEmail()
        );
    }

    public AuthResponseDTO login(@NonNull LoginDTO dto){
        User user = userRepo.findByEmail(dto.email())
                .orElseThrow(()-> new RuntimeException("User not found"));
        boolean match = passwordEncoder.matches(
                dto.password(), user.getPasswordHash()
        );

        if(!match){
            throw new RuntimeException("Invalid credentials");
        }


        return new AuthResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }


}
