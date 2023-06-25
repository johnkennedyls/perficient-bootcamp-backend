package com.bootcamp.rules_engine.mapper;

import com.bootcamp.rules_engine.dto.request.RequestUserDTO;
import com.bootcamp.rules_engine.dto.response.ResponseUserDTO;
import com.bootcamp.rules_engine.model.RulesEngineUser;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role",ignore=true)
    @Mapping(target = "password", source = "password",ignore=true)
    RulesEngineUser fromUserDTO(RequestUserDTO requestUserDTO);
    @Mapping(target = "role", expression = "java(rulesEngineUser.getRole().getRoleName())")
    ResponseUserDTO fromUserToResponseUserDTO(RulesEngineUser rulesEngineUser);
}
