package com.bootcamp.rules_engine.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bootcamp.rules_engine.model.Rule;

@Repository
public interface RuleRepository extends JpaRepository<Rule,UUID>{

    @Query("SELECT rule FROM Rule rule WHERE rule.name = :name")
    Optional<Rule> findByName(@Param("name") String name);
    
    @Query("SELECT COUNT(rule) > 0 FROM Rule rule WHERE rule.name = :name")
    Boolean isNameInUse(@Param("name") String name);
}
