package com.ticktr.app.Auth.DTOs;

public record RegisterDTO(
        String name,
        String email,
        String password
) {}
