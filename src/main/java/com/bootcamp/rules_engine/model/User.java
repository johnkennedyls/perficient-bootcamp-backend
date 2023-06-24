package com.bootcamp.rules_engine.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private UUID userId;
    @Column(unique=true)
    private String email;
    private String password;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_role_id", nullable = false)
    private Role role;
    @OneToMany(mappedBy = "shopUser")
    private List<ShopOrder> shopOrders;
}
