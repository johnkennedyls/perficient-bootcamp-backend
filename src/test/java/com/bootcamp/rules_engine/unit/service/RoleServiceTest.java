package com.bootcamp.rules_engine.unit.service;

import com.bootcamp.rules_engine.mapper.RoleMapper;
import com.bootcamp.rules_engine.repository.RoleRepository;
import com.bootcamp.rules_engine.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class RoleServiceTest {
    /*private RoleService roleService;
    private RoleRepository roleRepository;
    private RoleMapper roleMapper;

    @BeforeEach
    private void init(){
        roleRepository = mock(RoleRepository.class);
        roleMapper = spy(RoleMapperImpl.class);
        roleService = new RoleService(roleRepository, roleMapper);
        roleService = spy(roleService);
    }

    @Test
    public void testCreateRole_HappyPath(){
        // Arrange
        RoleDTO roleDTO= defaultRoleDTO();
        doNothing().when(roleService).checkPermissions();
        // Act
        roleService.saveRole(roleDTO);
        // Assert
        Role newRole = Role.builder()
                .description("Este rol puede acceder a la creacion de items y manejo de pedidos de la tienda (cambiar los estados)")
                .roleName("SHOP")
                .build();
        verify(roleRepository,times(1)).save(argThat(new RoleMatcher(newRole)));
        verify(roleMapper, times(1)).fromRoleDTO(any());
        verify(roleMapper, times(1)).fromRoleToRoleDTO(any());
        verify(roleRepository, times(1)).isNameInUse(any());
    }

    @Test
    public void testCreateRoleWhenNameAlreadyExists(){
        // Arrange
        RoleDTO roleDTO= defaultRoleDTO();
        Role role= defaultRole();
        when(roleRepository.isNameInUse(role.getRoleName())).thenReturn(true);
        doNothing().when(roleService).checkPermissions();
        try {
            // Act
            roleService.saveRole(roleDTO);
            fail();
        } catch (CellphoneException exception){
            String message = exception.getMessage();
            var error = exception.getError();
            var details = error.getDetails();
            assertEquals(1, details.size());
            var detail = details.get(0);
            // Assert
            assertEquals("ERR_DUPLICATED", detail.getErrorCode(), "Code doesn't match");
            assertEquals("SHOP, already exists.", detail.getErrorMessage(), "Error message doesn't match");
            assertEquals("Another role already has this name.", message);
        }
    }

    private RoleDTO defaultRoleDTO() {
        return RoleDTO.builder()
                .description("Este rol puede acceder a la creacion de items y manejo de pedidos de la tienda (cambiar los estados)")
                .roleName("SHOP")
                .build();
    }

    private Role defaultRole() {
        return Role.builder()
                .roleId(UUID.randomUUID())
                .description("Este rol puede acceder a la creacion de items y manejo de pedidos de la tienda (cambiar los estados)")
                .roleName("SHOP")
                .build();
    }*/
}
