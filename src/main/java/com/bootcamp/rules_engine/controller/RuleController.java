package com.bootcamp.rules_engine.controller;

import java.util.List;

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
    public Rule createRule(RuleDTO ruleDTO){
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
    public boolean evaluateRule(String ruleName) {
        return this.ruleService.evaluateRule(ruleName);
    }
}
