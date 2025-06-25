package com.example.AuthMicroservice.AuthMicroservice.Domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)

public class User implements UserDetails, Principal {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;
    @Getter
    private String password;
    private boolean enabled;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "permissionid", referencedColumnName = "permissionid")
    private Permission permission;

    private boolean accountLocked;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    public User( String firstname, String lastName, String email, String password, boolean isEnabled, boolean isAccountLocked, Permission permission) {
        this.firstName = firstname;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.enabled = isEnabled;
        this.accountLocked = isAccountLocked;
        this.permission = permission;

    }


    @Override
    public String getName() {
        return email;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.permission.getName()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getUserID() {
        return userID;
    }
}
