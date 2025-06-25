package com.example.AuthMicroservice.AuthMicroservice.Domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "permissions")
@EntityListeners(AuditingEntityListener.class)

public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionID;
    @Column(unique = true)
    private String name;

    @CreatedDate
    @Column(nullable = true, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = true)
    private LocalDateTime lastModifiedDate;


}















