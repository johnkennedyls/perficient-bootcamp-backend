package com.bootcamp.rules_engine.mapper;

import com.bootcamp.rules_engine.dto.request.RequestUserDTO;
import com.bootcamp.rules_engine.dto.response.ResponseUserDTO;
import com.bootcamp.rules_engine.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role",ignore=true)
    @Mapping(target = "password", source = "password",ignore=true)
    User fromUserDTO(RequestUserDTO requestUserDTO);
    @Mapping(target = "role", expression = "java(shopUser.getRole().getRoleName())")
    ResponseUserDTO fromUserToResponseUserDTO(User user);
}
