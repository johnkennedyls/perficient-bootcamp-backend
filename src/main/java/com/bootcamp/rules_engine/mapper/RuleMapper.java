package com.bootcamp.rules_engine.mapper;

import org.mapstruct.Mapper;

import com.bootcamp.rules_engine.dto.request.RuleDTO;
import com.bootcamp.rules_engine.model.Rule;

@Mapper(componentModel = "spring")
public interface RuleMapper {
    Rule fromRuleDTO(RuleDTO roleDTO);
    RuleDTO fromRuleToRuleDTO(Rule role);
}
