package com.bootcamp.rules_engine.repository;

import com.bootcamp.rules_engine.model.Rule;
import com.bootcamp.rules_engine.model.TableData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface TableDataRepository extends JpaRepository<TableData, UUID> {

    @Query("SELECT table FROM TableData table WHERE table.tableId = :tableId")
    Optional<TableData> findByName(@Param("tableId") String tableId);
}
