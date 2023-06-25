package com.bootcamp.rules_engine.repository;

import com.bootcamp.rules_engine.model.RulesEngineUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<RulesEngineUser, UUID> {
    Optional<RulesEngineUser> findByEmail(@Param("email") String email);
}
