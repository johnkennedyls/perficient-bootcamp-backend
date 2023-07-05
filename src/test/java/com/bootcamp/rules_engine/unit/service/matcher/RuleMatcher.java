package com.bootcamp.rules_engine.unit.service.matcher;

import java.util.Objects;

import org.mockito.ArgumentMatcher;

import com.bootcamp.rules_engine.model.Rule;

public class RuleMatcher implements ArgumentMatcher<Rule>{
    private Rule ruleLeft;
    public RuleMatcher(Rule ruleLeft){this.ruleLeft = ruleLeft;}

    @Override
    public boolean matches(Rule ruleRight) {
        return ruleRight.getId()!=null &&
                Objects.equals(ruleRight.getName(),ruleLeft.getName()) &&
                Objects.equals(ruleRight.getRule(), ruleLeft.getRule());
    }
}
