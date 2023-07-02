package com.bootcamp.rules_engine.controller;

import com.bootcamp.rules_engine.api.TableDataAPI;
import com.bootcamp.rules_engine.dto.request.TableDataDTO;
import com.bootcamp.rules_engine.enums.ErrorCode;
import com.bootcamp.rules_engine.error.exception.DetailBuilder;
import com.bootcamp.rules_engine.model.TableData;
import com.bootcamp.rules_engine.service.TableDataService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static com.bootcamp.rules_engine.api.TableDataAPI.BASE_TABLE_URL;
import static com.bootcamp.rules_engine.error.util.RulesEngineExceptionBuilder.createRulesEngineException;

@RestController
@RequestMapping(BASE_TABLE_URL)
@AllArgsConstructor
public class TableDataController implements TableDataAPI {
    private final TableDataService tableDataService;

    @Override
    public void createTable (MultipartFile file) {
        try {
            // Create a CSVParser to separate the data
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .withIgnoreQuotations(true)
                    .build();

            // Create a CSVReader with the uploaded CSV file
            CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
                    .withCSVParser(parser)
                    .build();

            // Parse the CSV data
            List<String[]> rows = csvReader.readAll();

            tableDataService.saveTable(rows, file.getOriginalFilename());

        } catch (IOException e) {
            e.printStackTrace();
            throw createRulesEngineException(
                    "IO exception occurred while processing the file.",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_500)
            ).get();
        } catch (CsvException e) {
            throw createRulesEngineException(
                    "Error reading CSV file. Invalid format found.",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_500)
            ).get();
        }
    }
}
