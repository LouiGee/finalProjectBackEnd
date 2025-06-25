package com.example.AuthMicroservice.AuthMicroservice.Domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jwtTokens")
@EntityListeners(AuditingEntityListener.class)

public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tokenid;
    @Column(name = "authentication_token", length = 1000)
    private String authenticationToken;
    private String refreshToken;
    private Date authenticationTokenExpiration;
    private Date refreshTokenExpiration;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "sessionid", nullable = false)
    private Session session;

    private String userEmail;
    private String userPermission;
    private Boolean tokenInUse;



}
