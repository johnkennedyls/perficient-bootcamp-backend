package com.bootcamp.rules_engine.service;

import com.bootcamp.rules_engine.dto.request.RoleDTO;
import com.bootcamp.rules_engine.enums.ErrorCode;
import com.bootcamp.rules_engine.enums.UserRole;
import com.bootcamp.rules_engine.error.exception.DetailBuilder;
import com.bootcamp.rules_engine.mapper.RoleMapper;
import com.bootcamp.rules_engine.model.Role;
import com.bootcamp.rules_engine.repository.RoleRepository;
import com.bootcamp.rules_engine.security.RulesEngineSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.bootcamp.rules_engine.error.util.RulesEngineExceptionBuilder.createRulesEngineException;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDTO saveRole(RoleDTO roleDTO){
        checkPermissions();
        if(roleRepository.isNameInUse(roleDTO.getRoleName())){
            throw createRulesEngineException(
                    "Another role already has this name.",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "Role", "name", roleDTO.getRoleName())
            ).get();
        }
        Role newRole = roleMapper.fromRoleDTO(roleDTO);
        newRole.setRoleId(UUID.randomUUID());
        roleRepository.save(newRole);
        return roleMapper.fromRoleToRoleDTO(newRole);
    }

    public void checkPermissions() {
        if(!RulesEngineSecurityContext.getCurrentUserRole().equals(UserRole.ADMIN.getRole())){
            throw createRulesEngineException(
                    "Only an ADMIN user can create new roles and visualize them.",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403)
            ).get();
        }
    }

    public RoleDTO getRole(String roleName) {
        return roleMapper.fromRoleToRoleDTO(
                roleRepository.findByName(roleName).orElseThrow(
                        createRulesEngineException(
                                "The role with the specified name does not exists.",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404, "Role", "name", roleName)
                        )
        );
    }
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::fromRoleToRoleDTO)
                .collect(Collectors.toList());
    }
}
