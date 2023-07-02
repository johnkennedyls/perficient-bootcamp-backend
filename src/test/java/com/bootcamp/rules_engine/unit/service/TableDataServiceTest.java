package com.bootcamp.rules_engine.unit.service;

import com.bootcamp.rules_engine.error.exception.ColumnNameException;
import com.bootcamp.rules_engine.model.TableData;
import com.bootcamp.rules_engine.repository.TableDataRepository;
import com.bootcamp.rules_engine.service.TableDataService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class TableDataServiceTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    public void testCreateTableInDatabaseColumnNameWithSpaceThrowsColumnNameException() {
        // Arrange
        TableDataRepository tableDataRepository = mock(TableDataRepository.class);
        TableDataService tableDataService = new TableDataService(jdbcTemplate, tableDataRepository);

        List<String> headers = Arrays.asList("Column1", "Column 2", "Column3");
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Value1", "Value2", "Value3"});
        List<String> types = Arrays.asList("text", "text", "text");

        TableData tableData = TableData.builder()
                .name("TestTable")
                .headers(headers)
                .rows(rows)
                .types(types)
                .build();

        // Act & Assert
        assertThrows(ColumnNameException.class, () -> {
            tableDataService.createTableInDatabase(tableData);
        });
    }
}

