package com.drinkIt.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "admins",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = "user_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;


    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;


    @Column(nullable = false)
    private String adminRole;


    @Column(nullable = false)
    private String status;


    @Column(nullable = false)
    private LocalDateTime createdAt;


    @PrePersist
    public void onCreate() {

        if (status == null) {

            status = "ACTIVE";
        }

        if (createdAt == null) {

            createdAt = LocalDateTime.now();
        }
    }
}