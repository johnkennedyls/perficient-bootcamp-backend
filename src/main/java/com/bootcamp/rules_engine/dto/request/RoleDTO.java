package com.bootcamp.rules_engine.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @NotNull(message = "The name of a role can't be null")
    @NotBlank(message = "The name of a role can't be blank")
    private String roleName;
    @NotNull(message = "The description of a role can't be null")
    @NotBlank(message = "The description of a role can't be blank")
    private String description;
}
