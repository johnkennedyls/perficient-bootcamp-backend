package com.bootcamp.rules_engine.api;

import com.bootcamp.rules_engine.dto.request.TableDataDTO;
import com.bootcamp.rules_engine.model.TableData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(TableDataAPI.BASE_TABLE_URL)
public interface TableDataAPI {
    String BASE_TABLE_URL="/tables";

    @PostMapping("/create")
    TableDataDTO createTable(@RequestBody TableDataDTO tableDataDTO);

}
