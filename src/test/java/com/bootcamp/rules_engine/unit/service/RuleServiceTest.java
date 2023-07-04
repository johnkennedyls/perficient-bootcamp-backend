package com.bootcamp.rules_engine.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.h2.table.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bootcamp.rules_engine.dto.request.RuleDTO;
import com.bootcamp.rules_engine.error.exception.RulesEngineException;
import com.bootcamp.rules_engine.mapper.RuleMapper;
import com.bootcamp.rules_engine.mapper.RuleMapperImpl;
import com.bootcamp.rules_engine.model.Rule;
import com.bootcamp.rules_engine.model.TableData;
import com.bootcamp.rules_engine.repository.RuleRepository;
import com.bootcamp.rules_engine.repository.TableDataRepository;
import com.bootcamp.rules_engine.service.RuleService;
import com.bootcamp.rules_engine.unit.service.matcher.RuleMatcher;

public class RuleServiceTest {

    private RuleService ruleService;
    private RuleRepository ruleRepository;
    private RuleMapper ruleMapper;
    private TableDataRepository tableRepository;

    @BeforeEach
    private void init(){
        ruleRepository = mock(RuleRepository.class);
        tableRepository = mock(TableDataRepository.class);
        ruleMapper = spy(RuleMapperImpl.class);
        ruleService = new RuleService(ruleRepository, ruleMapper, tableRepository);
        ruleService = spy(ruleService);
    }

    @Test
    public void testCreateRule_HappyPath(){
        // Arrange
        RuleDTO ruleDTO= defaultRuleDTO();
        doNothing().when(ruleService).checkPermissions();
        // Act
        ruleService.createRule(ruleDTO);
        // Assert
        Rule newRule = defaultRule();
        verify(ruleRepository,times(1)).save(argThat(new RuleMatcher(newRule)));
        verify(ruleMapper, times(1)).fromRuleDTO(any());
        verify(ruleMapper, times(1)).fromRuleToRuleDTO(any());
        verify(ruleRepository, times(1)).isNameInUse(any());
    }

    @Test
    public void testCreateRuleWhenNameAlreadyExists(){
        // Arrange
        RuleDTO ruleDTO= defaultRuleDTO();
        Rule rule= defaultRule();
        when(ruleRepository.isNameInUse(rule.getName())).thenReturn(true);
        doNothing().when(ruleService).checkPermissions();
        try {
            // Act
            ruleService.createRule(ruleDTO);
            fail();
        } catch (RulesEngineException exception){
            String message = exception.getMessage();
            var error = exception.getError();
            var details = error.getDetails();
            assertEquals(1, details.size());
            var detail = details.get(0);
            // Assert
            assertEquals("ERR_DUPLICATED", detail.getErrorCode(), "Code doesn't match");
            assertEquals("Resource Rule with field name: testRule, already exists.", detail.getErrorMessage(), "Error message doesn't match");
            assertEquals("Another rule already has this name.", message);
        }
    }

    @Test
    public void testEvaluateTable(){
        String ruleName = "testRule";
        String tableName = "testTable";

        when(ruleRepository.findByName(ruleName)).thenReturn(Optional.of(defaultRule()));
        when(tableRepository.findByName(tableName)).thenReturn(Optional.of(defaultTable()));
        
        List<Boolean> results = ruleService.evaluateRuleToTable(ruleName, tableName);

        assertEquals(2, results.size());
        assertTrue(results.get(0));
        assertFalse(results.get(1));
    }

    @Test
    public void testEvaluateRegister(){
        String ruleName = "testRule";
        String tableName = "testTable";
        int firstPosition = 0;

        when(ruleRepository.findByName(ruleName)).thenReturn(Optional.of(defaultRule()));
        when(tableRepository.findByName(tableName)).thenReturn(Optional.of(defaultTable()));
        
        boolean result = ruleService.evaluateRuleToRegister(ruleName, tableName, firstPosition);

        assertTrue(result);
    }

    @Test
    public void testEvaluateListOfRegisters(){
        String ruleName = "testRule";
        String tableName = "testTable";
        Integer rowToEvaluate = 0;
        List<Integer> positions = new ArrayList<Integer>();
        positions.add(rowToEvaluate);

        when(ruleRepository.findByName(ruleName)).thenReturn(Optional.of(defaultRule()));
        when(tableRepository.findByName(tableName)).thenReturn(Optional.of(defaultTable()));
        
        List<Boolean> results = ruleService.evaluateRuleToRegistersList(ruleName, tableName, positions);

        assertEquals(1, results.size());
        assertTrue(results.get(0));
    }

    public void testDeleteRule(){
        RuleDTO ruleDTO= defaultRuleDTO();
        doNothing().when(ruleService).checkPermissions();
        ruleService.createRule(ruleDTO);

        String ruleName = "testRule";
        ruleService.deleteRule(ruleName);
        verify(ruleService).deleteRule(ruleName);
    }


    private RuleDTO defaultRuleDTO() {
        return RuleDTO.builder()
                .name("testRule")
                .rule("testHeader1 = testHeader2")
                .build();
    }

    private Rule defaultRule() {
        return Rule.builder()
                .id(UUID.randomUUID())
                .name("testRule")
                .rule("testHeader1 = testHeader2")
                .build();
    }

    private TableData defaultTable(){
        String tableName = "testTable";
        List<String> headers = List.of("testHeader1", "testHeader2");
        List<String[]> rows = List.of(new String[]{"testValueTrue", "testValueTrue"}, new String[]{"testValueFalse", "anotherTestValue"});
        TableData tableData = TableData.builder()
                .name(tableName)
                .headers(headers)
                .rows(rows)
                .build();
        return tableData;
    }
}
