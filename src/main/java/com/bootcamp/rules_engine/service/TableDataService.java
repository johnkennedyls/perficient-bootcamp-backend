package com.bootcamp.rules_engine.service;

import com.bootcamp.rules_engine.dto.request.TableDataDTO;
import com.bootcamp.rules_engine.enums.ErrorCode;
import com.bootcamp.rules_engine.enums.UserRole;
import com.bootcamp.rules_engine.error.exception.DetailBuilder;
import com.bootcamp.rules_engine.model.TableData;
import com.bootcamp.rules_engine.repository.TableDataRepository;
import com.bootcamp.rules_engine.security.RulesEngineSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

import static com.bootcamp.rules_engine.error.util.RulesEngineExceptionBuilder.createRulesEngineException;

@Service
@AllArgsConstructor
public class TableDataService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final TableDataRepository tableDataRepository;

    public void saveTable(List<String[]> rows, String originalFilename) {
        checkPermissions();

        List<String> headers = Arrays.asList(rows.get(1));
        List<String> types = Arrays.asList(rows.get(0));

        rows.remove(0);
        rows.remove(1);

        String[] nameParts = originalFilename.split(".");

        TableData newTableData = TableData.builder()
                .tableId(UUID.randomUUID())
                .name(nameParts[0])
                .headers(headers)
                .rows(rows)
                .types(types)
                .build();
        tableDataRepository.save(newTableData);
        createTableInDatabase(newTableData);
    }

    public void createTableInDatabase(TableData newTableData) {
        StringBuilder queryBuilder = new StringBuilder("CREATE TABLE ")
                .append(newTableData.getName())
                .append("(");

        Map<String, String> dataTypeMapping = new HashMap<>();
        dataTypeMapping.put("number", "INT");
        dataTypeMapping.put("text", "VARCHAR(255)");
        dataTypeMapping.put("boolean", "BOOLEAN");

        IntStream.range(0, newTableData.getHeaders().size())
                .forEach(k -> {
                    queryBuilder.append(newTableData.getHeaders().get(k));

                    if (k == 0) {
                        queryBuilder.append(" VARCHAR(255) PRIMARY KEY");
                    } else {
                        String type = newTableData.getTypes().get(k);
                        String dataType = dataTypeMapping.get(type.toLowerCase());

                        if (dataType != null) {
                            queryBuilder.append(" ").append(dataType);
                        } else {
                            throw createRulesEngineException(
                                    "The specified type for a column is not valid",
                                    HttpStatus.BAD_REQUEST,
                                    new DetailBuilder(ErrorCode.ERR_500)
                            ).get();
                        }
                    }

                    if (k == newTableData.getHeaders().size() - 1) {
                        queryBuilder.append(");");
                    } else {
                        queryBuilder.append(", ");
                    }
                });

        String query = queryBuilder.toString();
        jdbcTemplate.execute(query);
        newTableData.getRows().forEach(row -> addRowToDatabase(row, newTableData));
    }

    public void addRowToDatabase(String[] row, TableData table) {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ")
                .append(table.getName())
                .append(" (");

        StringJoiner columnJoiner = new StringJoiner(", ");

        table.getHeaders().forEach(columnJoiner::add);

        queryBuilder.append(columnJoiner)
                .append(") VALUES (");

        List<String> values = Arrays.stream(row)
                .map(value -> {
                    if (isNumeric(value) && value.startsWith("\"") && value.endsWith("\"")) {
                        return value.substring(1, value.length() - 1);
                    }
                    return value;
                })
                .toList();

        StringJoiner valueJoiner = new StringJoiner(", ");

        values.forEach(valueJoiner::add);

        queryBuilder.append(valueJoiner)
                .append(")");

        String query = queryBuilder.toString();
        jdbcTemplate.update(query);
    }

    private static boolean isNumeric(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
