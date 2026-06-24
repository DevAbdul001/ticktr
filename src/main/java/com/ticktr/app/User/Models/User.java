package com.ticktr.app.User.Models;

import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Column(nullable = false)
    String name;

    @NotBlank
    @Column(nullable = false, unique = true)
    String email;

    @NotBlank
    @Column(nullable = false, name = "password_hash")
    String passwordHash;

    protected  User(){

    }

    public User (String name, String email, String passwordHash){
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

}
