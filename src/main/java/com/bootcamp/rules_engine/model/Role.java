package com.bootcamp.rules_engine.model;

import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @OneToMany(mappedBy = "role")
    private List<User> users;
    @Id
    private UUID roleId;
    private String roleName;
    private String description;
}
