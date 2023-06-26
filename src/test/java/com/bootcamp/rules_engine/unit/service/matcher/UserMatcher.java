package com.bootcamp.rules_engine.unit.service.matcher;

import com.bootcamp.rules_engine.model.RulesEngineUser;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class UserMatcher implements ArgumentMatcher<RulesEngineUser> {
    private RulesEngineUser rulesEngineUserLeft;
    public UserMatcher(RulesEngineUser rulesEngineUserLeft){
        this.rulesEngineUserLeft = rulesEngineUserLeft;
    }

    @Override
    public boolean matches(RulesEngineUser rulesEngineUserRight){
        return rulesEngineUserRight.getUserId() != null && rulesEngineUserRight.getRole() != null &&
                Objects.equals(rulesEngineUserRight.getName(), rulesEngineUserLeft.getName()) &&
                Objects.equals(rulesEngineUserRight.getLastName(), rulesEngineUserLeft.getLastName()) &&
                Objects.equals(rulesEngineUserRight.getEmail(), rulesEngineUserLeft.getEmail()) &&
                Objects.equals(rulesEngineUserRight.getPassword(), rulesEngineUserLeft.getPassword());
    }
}
