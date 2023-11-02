package com.fredyhg.destiny2jobs.models;


import com.fredyhg.destiny2jobs.enums.Role;
import com.fredyhg.destiny2jobs.security.token.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "user_tb")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(name="name")
    private String name;

    @NotBlank
    @Column(name="last_name")
    private String lastName;

    @NotBlank
    @Column(name="email")
    private String email;

    @NotBlank
    @Column(name = "password")
    private String password;

    @NotBlank
    @Column(name = "discord_name")
    private String discordName;


    @Column(name="balance")
    private Double balance;

    @Column(name="create_at")
    private Date createAt;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<CustomPackageModel> userPackages;

    @OneToMany(mappedBy = "worker")
    private List<CustomPackageModel> workerPackages;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
