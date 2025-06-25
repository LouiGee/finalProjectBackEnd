package com.example.AuthMicroservice.AuthMicroservice.Domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sessions")
@EntityListeners(AuditingEntityListener.class)
public class Session {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionid;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private User user;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateTimeStarted;

    @Column()
    private LocalDateTime dateTimeFinish;


}
