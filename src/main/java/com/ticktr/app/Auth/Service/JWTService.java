package com.ticktr.app.Auth.Service;

import com.ticktr.app.Auth.DTOs.AuthResponseDTO;
import com.ticktr.app.User.Models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Service
public class JWTService {

    private final String SECRET_KEY;

    public JWTService(
            @Value("${jwt.secret}") String SECRET_KEY
    ){
        this.SECRET_KEY = SECRET_KEY;
    }


    public String getSecretKey(){
        return SECRET_KEY;
    }

    public String generateAccessToken(@NonNull AuthResponseDTO dto){

        return Jwts.builder()
                .subject(String.valueOf(dto.id()))
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                + 1000 * 60 * 15
                        )
                )
                .signWith(
                        Keys.hmacShaKeyFor(
                                SECRET_KEY.getBytes()
                        )
                )
                .compact();
    }

    public String generateRefreshToken(@NonNull AuthResponseDTO dto){

        return Jwts.builder()
                .subject(String.valueOf(dto.id()))
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60 * 7
                        )
                )
                .signWith(
                        Keys.hmacShaKeyFor(
                                SECRET_KEY.getBytes()
                        )
                )
                .compact();
    }

    public boolean isValid(String token){
        try{
            Jwts.parser()
                    .verifyWith(
                            Keys.hmacShaKeyFor(
                                    SECRET_KEY.getBytes()
                            )
                    )
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e){
            return  false;
        }
    }

    public Long extractUserId(String token) {
        return Long.parseLong(
                Jwts.parser()
                        .verifyWith(
                                Keys.hmacShaKeyFor(
                                        SECRET_KEY.getBytes()
                                )
                        )
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject()
        );
    }
}
