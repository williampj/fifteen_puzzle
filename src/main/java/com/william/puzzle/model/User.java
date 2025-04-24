package com.william.puzzle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.william.puzzle.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class User implements UserDetails {
    private UUID id;
    private String username;
    private Role role;
    @JsonIgnore
    private String encodedPassword;
    private LocalDateTime lastLogin;

    public User(UUID id, String username, String encodedPassword, Role role) {
        this.id = id;
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.role = role;
        this.lastLogin = LocalDateTime.now();
    }

    public User(UUID id, String username, String encodedPassword, Role role, LocalDateTime lastLogin) {
        this.id = id;
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.role = role;
        this.lastLogin = lastLogin;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getAuthority()));
    }

    @Override
    public String getPassword() {
        return encodedPassword;
    }
}
