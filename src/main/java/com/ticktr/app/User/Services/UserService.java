package com.ticktr.app.User.Services;

import com.ticktr.app.Auth.DTOs.AuthResponseDTO;
import com.ticktr.app.User.Models.User;
import com.ticktr.app.User.Repositories.UserRepo;
import com.ticktr.app.User.UserDTOs.UserResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo , PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO fetchById(Long id){
        User user = userRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));

        return new AuthResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

}
