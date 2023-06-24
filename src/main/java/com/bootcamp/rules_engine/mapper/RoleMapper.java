package com.bootcamp.rules_engine.mapper;

import com.bootcamp.rules_engine.dto.request.RoleDTO;
import com.bootcamp.rules_engine.model.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role fromRoleDTO(RoleDTO roleDTO);
    RoleDTO fromRoleToRoleDTO(Role role);
}
