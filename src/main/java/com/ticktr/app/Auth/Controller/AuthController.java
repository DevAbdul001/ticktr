package com.ticktr.app.Auth.Controller;

import com.ticktr.app.Auth.DTOs.AuthResponseDTO;
import com.ticktr.app.Auth.DTOs.LoginDTO;
import com.ticktr.app.Auth.DTOs.RegisterDTO;
import com.ticktr.app.Auth.Service.AuthService;
import com.ticktr.app.Auth.Service.JWTService;
import com.ticktr.app.User.Services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JWTService jwtService;
    private final UserService userService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDTO register(
            @RequestBody RegisterDTO dto
            ){
        return  authService.create(dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody LoginDTO loginDTO
    ){

        AuthResponseDTO response = authService.login(loginDTO);
        String accessToken = String.valueOf(jwtService.generateAccessToken(response));
        String refreshToken = String.valueOf(jwtService.generateRefreshToken(response));

        ResponseCookie accessCookie =
                ResponseCookie.from("accessToken", accessToken)
                        .httpOnly(true)
                        .secure(true)
                        .sameSite("Strict")
                        .path("/")
                        .maxAge(Duration.ofMinutes(15))
                        .build();

        ResponseCookie refreshCookie =
                ResponseCookie.from("refreshToken", refreshToken)
                        .httpOnly(true)
                        .secure(true)
                        .sameSite("Strict")
                        .path("/api/v1/auth/refresh")
                        .maxAge(Duration.ofDays(7))
                        .build();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        accessCookie.toString()
                )
                .header(
                        HttpHeaders.SET_COOKIE,
                        refreshCookie.toString()
                )
                .body(response);

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestAttribute Long userId
    ){
        AuthResponseDTO user = userService.fetchById(userId);
        String token = String.valueOf(jwtService.generateAccessToken(user));

        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        ResponseCookie accessCookie =
                ResponseCookie.from("accessToken", "")
                        .path("/")
                        .httpOnly(true)
                        .secure(true)
                        .maxAge(0)
                        .build();

        ResponseCookie refreshCookie =
                ResponseCookie.from("refreshToken", "")
                        .path("/api/v1/auth/refresh")
                        .httpOnly(true)
                        .secure(true)
                        .maxAge(0)
                        .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }

}
