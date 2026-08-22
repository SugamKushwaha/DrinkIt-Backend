package com.drinkIt.entity;

import java.time.LocalDateTime;

import com.drinkIt.enums.Role;
import com.drinkIt.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
     private UserStatus status;

     @Column(nullable = false)
    private boolean verified;

     @Column(nullable=false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
 
    @PrePersist
    public void onCreate() { 

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (role == null) {
            role = Role.CUSTOMER;
        }

        if (status == null) {
            status = UserStatus.ACTIVE;
        }
    }

    @PreUpdate
    public void onUpdate() {

        updatedAt = LocalDateTime.now();
 }
}
