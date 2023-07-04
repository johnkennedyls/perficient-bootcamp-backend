package com.bootcamp.rules_engine.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.rules_engine.api.RuleAPI;
import com.bootcamp.rules_engine.dto.request.RuleDTO;
import com.bootcamp.rules_engine.model.Rule;
import com.bootcamp.rules_engine.service.RuleService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/rules")
@AllArgsConstructor
public class RuleController implements RuleAPI{
    
    private final RuleService ruleService;

    @Override
    public RuleDTO createRule(RuleDTO ruleDTO){
       return this.ruleService.createRule(ruleDTO);
    }

    @Override
    public RuleDTO getRule(String name) {
        return this.ruleService.getRule(name);
    }

    @Override
    public List<RuleDTO> getAllRules() {
        return this.ruleService.getAllRules();
    }

    @Override
    public boolean evaluateRuleToRegister(String ruleName, String tableName, Integer rowPosition) {
        return this.ruleService.evaluateRuleToRegister(ruleName, tableName, rowPosition);
    }

    @Override
    public List<Boolean> evaluateRuleToTable(String ruleName, String tableName) {
        return this.ruleService.evaluateRuleToTable(ruleName, tableName);
    }

    @Override
    public List<Boolean> evaluateRuleToRegistersList(String ruleName, String tableName, String positions) {
        List<Integer> positionsList = Arrays.stream(positions.split(","))
                                      .map(Integer::parseInt)
                                      .collect(Collectors.toList());
        return this.ruleService.evaluateRuleToRegistersList(ruleName, tableName, positionsList);
    }

    @Override
    public void deleteRule(String ruleName) {
        this.ruleService.deleteRule(ruleName);
    }

   
}
