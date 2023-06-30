package com.bootcamp.rules_engine.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleDTO {
    
    @NotNull(message = "The name of a rule can't be null")
    @NotBlank(message = "The name of a rule can't be blank")
    private String name;
    @NotNull(message = "The rule instruction of a rule can't be null")
    @NotBlank(message = "The rule instruction of a rule can't be blank")
    private String rule;
}
