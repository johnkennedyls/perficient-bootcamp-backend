package com.bootcamp.rules_engine.repository;

import com.bootcamp.rules_engine.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Query("SELECT role FROM Role role WHERE role.roleName = :roleName")
    Optional<Role> findByName(@Param("roleName") String roleName);

    @Query("SELECT COUNT(role) > 0 FROM Role role WHERE role.roleName = :roleName")
    Boolean isNameInUse(@Param("roleName") String roleName);
}
