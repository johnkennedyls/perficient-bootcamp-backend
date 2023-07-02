package com.bootcamp.rules_engine.api;

import com.bootcamp.rules_engine.dto.request.TableDataDTO;
import com.bootcamp.rules_engine.model.TableData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(TableDataAPI.BASE_TABLE_URL)
public interface TableDataAPI {
    String BASE_TABLE_URL="/tables";

    @PostMapping("/create")
    void createTable(@RequestPart("file") MultipartFile file);

}
