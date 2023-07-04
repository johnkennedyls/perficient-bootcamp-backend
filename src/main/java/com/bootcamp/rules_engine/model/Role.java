package com.bootcamp.rules_engine.model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @OneToMany(mappedBy = "role")
    private List<RulesEngineUser> users;
    @Id
    private UUID roleId;
    private String roleName;
    private String description;
}
