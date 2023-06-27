package com.bootcamp.rules_engine.unit.service;

import com.bootcamp.rules_engine.dto.request.RequestUserDTO;
import com.bootcamp.rules_engine.error.exception.RulesEngineException;
import com.bootcamp.rules_engine.mapper.UserMapper;
import com.bootcamp.rules_engine.mapper.UserMapperImpl;
import com.bootcamp.rules_engine.model.Role;
import com.bootcamp.rules_engine.model.RulesEngineUser;
import com.bootcamp.rules_engine.repository.RoleRepository;
import com.bootcamp.rules_engine.repository.UserRepository;
import com.bootcamp.rules_engine.service.UserService;
import com.bootcamp.rules_engine.unit.service.matcher.UserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    private void init(){
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userMapper = spy(UserMapperImpl.class);
        passwordEncoder = spy(PasswordEncoder.class);
        userService = new UserService(userRepository,  userMapper, roleRepository, passwordEncoder);
        userService=spy(userService);
    }

//    @Test
//    public void testCreateUser_HappyPath(){
//        // Arrange
//        var userDTO = defaultUserDTO();
//        var rulesEngineRole= defaultRulesRole();
//        when(roleRepository.findByName(any())).thenReturn(Optional.of(rulesEngineRole));
//        doNothing().when(userService).checkPermissions();
//        // Act
//        userService.saveUser(userDTO);
//        // Assert
//        RulesEngineUser newRulesUser = RulesEngineUser.builder()
//                .name("Damiano")
//                .lastName("David")
//                .email("ykaar@gmail.com")
//                .password(passwordEncoder.encode("taEkbs08"))
//                .role(defaultRulesRole())
//                .build();
//
//
//        verify(userRepository, times(1)).findByEmail(any());
//        verify(userRepository,times( 1)).save(argThat(new UserMatcher(newRulesUser)));
//        verify(userMapper, times(1)).fromUserDTO(any());
//        verify(userMapper, times(1)).fromUserToResponseUserDTO(any());
//    }

    @Test
    public void testCreateUserWithoutRole(){
        // Arrange
        var userDTO = defaultUserDTO1();
        doNothing().when(userService).checkPermissions();
        try {
            // Act
            userService.saveUser(userDTO);
            fail();
        } catch (RulesEngineException exception){
            String message = exception.getMessage();
            var error = exception.getError();
            var details = error.getDetails();
            assertEquals(1, details.size());
            var detail = details.get(0);
            // Assert
            assertEquals("The role with the specified name does not exists.", message);
            assertEquals("ERR_404", detail.getErrorCode(), "Code doesn't match");
            assertEquals("Role with name:  not found.", detail.getErrorMessage(), "Error message doesn't match");
        }
    }

    @Test
    public void testCreateUserWithRoleThatDoesNotExists(){
        // Arrange
        var userDTO = defaultUserDTO();
        doNothing().when(userService).checkPermissions();
        try {
            // Act
            userService.saveUser(userDTO);
            fail();
        } catch (RulesEngineException exception){
            String message = exception.getMessage();
            var error = exception.getError();
            var details = error.getDetails();
            assertEquals(1, details.size());
            var detail = details.get(0);
            // Assert
            assertEquals("The role with the specified name does not exists.", message);
            assertEquals("ERR_404", detail.getErrorCode(), "Code doesn't match");
            assertEquals("Role with name: Director SIS not found.", detail.getErrorMessage(), "Error message doesn't match");
        }
    }

    @Test
    public void testCreateUserWhenEmailAlreadyExists(){
        // Arrange
        var userDTO = defaultUserDTO();
        var rulesEngineRole= defaultRulesRole();
        var rulesEngineUser= defaultRulesUser();
        when(roleRepository.findByName(any())).thenReturn(Optional.of(rulesEngineRole));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(rulesEngineUser));
        doNothing().when(userService).checkPermissions();
        try {
            // Act
            userService.saveUser(userDTO);
            fail();
        } catch (RulesEngineException exception){
            String message = exception.getMessage();
            var error = exception.getError();
            var details = error.getDetails();
            assertEquals(1, details.size());
            var detail1 = details.get(0);
            // Assert
            assertEquals("A user with the entered email already exists.", message);
            assertEquals("ERR_DUPLICATED", detail1.getErrorCode(), "Code doesn't match");
            assertEquals("Resource user with field email: ykaar@gmail.com, already exists.", detail1.getErrorMessage(), "Error message doesn't match");
        }
    }

    private RequestUserDTO defaultUserDTO(){
        return RequestUserDTO.builder()
                .name("Damiano")
                .lastName("David")
                .email("ykaar@gmail.com")
                .password("taEkbs08")
                .role("Director SIS")
                .build();
    }

    private RequestUserDTO defaultUserDTO1(){
        return RequestUserDTO.builder()
                .name("Damiano")
                .lastName("David")
                .email("ykaar@gmail.com")
                .password("taEkbs08")
                .role("")
                .build();
    }

    private RulesEngineUser defaultRulesUser(){
        return RulesEngineUser.builder()
                .userId(UUID.randomUUID())
                .name("Damiano")
                .lastName("David")
                .email("ykaar@gmail.com")
                .password(passwordEncoder.encode("taEkbs08"))
                .role(defaultRulesRole())
                .build();
    }

    private Role defaultRulesRole() {
        return Role.builder()
                .roleId(UUID.randomUUID())
                .description("Role for a person")
                .roleName("MANAGER")
                .build();
    }

}
