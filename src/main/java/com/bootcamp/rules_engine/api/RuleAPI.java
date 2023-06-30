package com.bootcamp.rules_engine.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bootcamp.rules_engine.dto.request.RuleDTO;
import com.bootcamp.rules_engine.model.Rule;

@RequestMapping(RuleAPI.BASE_RULE_URL)
public interface RuleAPI {
    String BASE_RULE_URL = "/rules";
    @PostMapping("/create")
    Rule createRule(@RequestBody @Valid RuleDTO ruleDTO);
    @GetMapping("/{name}")
    RuleDTO getRule(@PathVariable("name") String name);
    @GetMapping("/getRoles")
    List<RuleDTO> getAllRules();
    @GetMapping("/evaluate/{ruleName}")
    boolean evaluateRule(@PathVariable("ruleName") String ruleName);
}
