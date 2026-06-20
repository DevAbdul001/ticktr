package com.ticktr.app.Auth.Filters;

import com.ticktr.app.Auth.Service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JWTMiddleware extends OncePerRequestFilter {


    private final JWTService jwtService;


    public JWTMiddleware(JWTService jwtService){
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {


        String token = null;


        Cookie[] cookies = request.getCookies();


        if(cookies != null){

            for(Cookie cookie : cookies){

                if(cookie.getName().equals("refreshToken")){

                    token = cookie.getValue();
                    break;
                }
            }
        }


        if(token == null){

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }


        if(!jwtService.isValid(token)){

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }


        Long userId = jwtService.extractUserId(token);

        request.setAttribute("userId", userId);

        filterChain.doFilter(request, response);
    }
}