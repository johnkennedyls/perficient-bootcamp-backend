package com.bootcamp.rules_engine.repository;

import com.bootcamp.rules_engine.model.TableData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TableDataRepository extends JpaRepository<TableData, UUID> {
}
