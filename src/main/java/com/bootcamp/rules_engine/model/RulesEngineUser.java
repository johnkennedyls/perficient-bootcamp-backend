package com.bootcamp.rules_engine.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Entity
@Table(name = "User")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RulesEngineUser {
    @Id
    private UUID userId;
    private String name;
    private String lastName;
    @Column(unique=true)
    private String email;
    private String password;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_role_id", nullable = false)
    private Role role;
}
