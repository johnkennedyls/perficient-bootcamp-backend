package com.bootcamp.rules_engine.service;

import com.bootcamp.rules_engine.dto.request.RoleDTO;
import com.bootcamp.rules_engine.mapper.RoleMapper;
import com.bootcamp.rules_engine.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final CellphoneShopExceptionBuilder exceptionBuilder = new CellphoneShopExceptionBuilder();

    public RoleDTO saveRole(RoleDTO roleDTO){
        checkPermissions();
        if(roleRepository.isNameInUse(roleDTO.getRoleName())){
            throw exceptionBuilder.duplicatedValueException(
                    "Another role already has this name.", roleDTO.getRoleName());
        }
        Role newRole = roleMapper.fromRoleDTO(roleDTO);
        newRole.setRoleId(UUID.randomUUID());
        roleRepository.save(newRole);
        return roleMapper.fromRoleToRoleDTO(newRole);
    }

    public void checkPermissions() {
        if(!CellphoneSecurityContext.getCurrentUserRole().equals(UserRole.ADMIN.getRole())){
            throw exceptionBuilder.noPermissionException(
                    "Only an ADMIN user can create new roles and visualize them."
            );
        }
    }

    public RoleDTO getRole(String roleName) {
        return roleMapper.fromRoleToRoleDTO(
                roleRepository.findByName(roleName).orElseThrow(
                        () -> exceptionBuilder.notFoundException(
                                "The role with the specified name does not exists.", roleName))
        );
    }
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::fromRoleToRoleDTO)
                .collect(Collectors.toList());
    }
}
