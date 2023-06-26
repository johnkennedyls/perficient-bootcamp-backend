package com.bootcamp.rules_engine.service;

import com.bootcamp.rules_engine.dto.request.RequestUserDTO;
import com.bootcamp.rules_engine.dto.response.ResponseUserDTO;
import com.bootcamp.rules_engine.enums.ErrorCode;
import com.bootcamp.rules_engine.enums.UserRole;
import com.bootcamp.rules_engine.error.exception.DetailBuilder;
import com.bootcamp.rules_engine.mapper.UserMapper;
import com.bootcamp.rules_engine.model.Role;
import com.bootcamp.rules_engine.model.RulesEngineUser;
import com.bootcamp.rules_engine.repository.RoleRepository;
import com.bootcamp.rules_engine.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.bootcamp.rules_engine.error.util.RulesEngineExceptionBuilder.createRulesEngineException;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseUserDTO saveUser(RequestUserDTO requestUserDTO) {
        Role role = roleRepository.findByName(requestUserDTO.getRole()).orElseThrow(
                createRulesEngineException(
                        "The role with the specified name does not exists.",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "Role", "name", requestUserDTO.getRole())
                )
        );

        checkPermissionsToAssignRole(requestUserDTO.getRole());
        validateIfEmailIsDuplicated(requestUserDTO.getEmail());
        RulesEngineUser rulesEngineUser = userMapper.fromUserDTO(requestUserDTO);
        rulesEngineUser.setUserId(UUID.randomUUID());
        rulesEngineUser.setRole(role);
        rulesEngineUser.setPassword(passwordEncoder.encode(requestUserDTO.getPassword()));
        userRepository.save(rulesEngineUser);
        return userMapper.fromUserToResponseUserDTO(rulesEngineUser);
    }

    public ResponseUserDTO getUser(String userEmail) {
        return userMapper.fromUserToResponseUserDTO(
                userRepository.findByEmail(userEmail).orElseThrow(
                        createRulesEngineException(
                                "The user with the specified email does not exists",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404, "User", "email", userEmail)
                        )
                )
        );
    }
    public List<ResponseUserDTO> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::fromUserToResponseUserDTO)
                .collect(Collectors.toList());
    }

    private void validateIfEmailIsDuplicated(String userEmail){
        if(userRepository.findByEmail(userEmail).isPresent()){
            throw createRulesEngineException(
                    "A user with the entered email already exists.",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "user", "email", userEmail)
            ).get();
        }
    }

    public void checkPermissionsToAssignRole(String roleToAssign) {
        if (roleToAssign.equals(UserRole.ADMIN.getRole())) {
            throw createRulesEngineException(
                    "Only an ADMIN user can assign roles.",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403)
            ).get();
        }
    }

}
