package com.bootcamp.rules_engine.service;

import com.bootcamp.rules_engine.dto.request.TableDataDTO;
import com.bootcamp.rules_engine.enums.ErrorCode;
import com.bootcamp.rules_engine.enums.UserRole;
import com.bootcamp.rules_engine.error.exception.DetailBuilder;
import com.bootcamp.rules_engine.model.TableData;
import com.bootcamp.rules_engine.repository.TableDataRepository;
import com.bootcamp.rules_engine.security.RulesEngineSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.bootcamp.rules_engine.error.util.RulesEngineExceptionBuilder.createRulesEngineException;

@Service
@AllArgsConstructor
public class TableDataService {
    private final TableDataRepository tableDataRepository;

    public TableDataDTO saveTable(TableDataDTO tableData, List<String[]> rows) {
        checkPermissions();
        // Get the headers (first row)
        String[] headers = rows.get(1);

        List<String> types = Arrays.asList(rows.get(0));

        // Remove the headers from the rows list
        rows.remove(0);

        // Build the object
        TableData newTableData = TableData.builder()
                .tableId(UUID.randomUUID())
                .headers(headers)
                .rows(rows)
                .types(types)
                .build();
        tableDataRepository.save(newTableData);
        tableData.setMessage("Table created successfully");
        return tableData;
    }


    public void checkPermissions() {
        if (!RulesEngineSecurityContext.getCurrentUserRole().equals(UserRole.RESEARCHER.getRole())) {
            throw createRulesEngineException(
                    "Only a RESEARCHER user can import CSV files.",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403)
            ).get();
        }
    }

}
