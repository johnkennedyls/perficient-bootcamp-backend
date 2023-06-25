package com.bootcamp.rules_engine.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
public class ResponseUserDTO {

    private String name;
    private String lastName;
    private String email;
    private String password;
    private String role;
}
