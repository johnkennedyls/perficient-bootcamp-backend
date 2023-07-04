package com.bootcamp.rules_engine.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bootcamp.rules_engine.dto.request.RuleDTO;
import com.bootcamp.rules_engine.model.Rule;

@RequestMapping(RuleAPI.BASE_RULE_URL)
public interface RuleAPI {
    String BASE_RULE_URL = "/rules";
    @PostMapping("/create")
    RuleDTO createRule(@RequestBody @Valid RuleDTO ruleDTO);
    @GetMapping("/{name}")
    RuleDTO getRule(@PathVariable("name") String name);
    @GetMapping("/getRoles")
    List<RuleDTO> getAllRules();

    @GetMapping("/evaluate/register/{ruleName}/{tableName}/{rowPosition}")
    boolean evaluateRuleToRegister(
        @PathVariable("ruleName") String ruleName,
        @PathVariable("tableName") String tableName,
        @PathVariable("rowPosition") Integer rowPosition);

    @GetMapping("/evaluate/table/{ruleName}/{tableName}")
    List<Boolean> evaluateRuleToTable(
        @PathVariable("ruleName") String ruleName,
        @PathVariable("tableName") String tableName);
    
    @GetMapping("/evaluate/List/{ruleName}/{tableName}/{rowPosition}")
    List<Boolean> evaluateRuleToRegistersList(
        @PathVariable("ruleName") String ruleName,
        @PathVariable("tableName") String tableName,
        @PathVariable("rowPosition") String positions);

    @DeleteMapping("/delete/{ruleName}")
    void deleteRule(@PathVariable("ruleName") String ruleName);
}
