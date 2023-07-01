package com.bootcamp.rules_engine.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bootcamp.rules_engine.dto.request.RuleDTO;
import com.bootcamp.rules_engine.error.exception.RulesEngineException;
import com.bootcamp.rules_engine.mapper.RuleMapper;
import com.bootcamp.rules_engine.mapper.RuleMapperImpl;
import com.bootcamp.rules_engine.model.Rule;
import com.bootcamp.rules_engine.repository.RuleRepository;
import com.bootcamp.rules_engine.service.RuleService;
import com.bootcamp.rules_engine.unit.service.matcher.RuleMatcher;

public class RuleServiceTest {

    private RuleService ruleService;
    private RuleRepository ruleRepository;
    private RuleMapper ruleMapper;
    
    @BeforeEach
    private void init(){
        ruleRepository = mock(RuleRepository.class);
        ruleMapper = spy(RuleMapperImpl.class);
        ruleService = new RuleService(ruleRepository, ruleMapper);
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
        Rule newRule = Rule.builder()
                .name("Rule standar")
                .rule("1 < 2")
                .build();
        verify(ruleRepository,times(1)).save(argThat(new RuleMatcher(newRule)));
        verify(ruleMapper, times(1)).fromRuleDTO(any());
        verify(ruleMapper, times(1)).fromRuleToRuleDTO(any());
        verify(ruleRepository, times(1)).isNameInUse(any());
    }

    @Test
    public void testCreateRoleWhenNameAlreadyExists(){
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
            assertEquals("Resource Rule with field name: Rule standar, already exists.", detail.getErrorMessage(), "Error message doesn't match");
            assertEquals("Another rule already has this name.", message);
        }
    }


    private RuleDTO defaultRuleDTO() {
        return RuleDTO.builder()
                .name("Rule standar")
                .rule("1 < 2")
                .build();
    }

    private Rule defaultRule() {
        return Rule.builder()
                .id(UUID.randomUUID())
                .name("Rule standar")
                .rule("1 < 2")
                .build();
    }
}
