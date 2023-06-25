package com.bootcamp.rules_engine.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserDTO {

    private String name;
    private String lastName;
    private String email;

    @NotBlank(message = "The password of a user can't be blank")
    @NotNull(message = "The password of a user can't be null")
    private String password;

    private String role;

}
