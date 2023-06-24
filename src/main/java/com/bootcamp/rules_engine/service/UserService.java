package com.bootcamp.rules_engine.service;

import com.bootcamp.rules_engine.dto.response.ResponseUserDTO;
import com.bootcamp.rules_engine.mapper.UserMapper;
import com.bootcamp.rules_engine.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ResponseUserDTO getUser(String userEmail) {
        return userMapper.fromUserToResponseUserDTO(
                userRepository.findByEmail(userEmail).orElseThrow(
                        () -> exceptionBuilder.notFoundException(
                                "The user with the specified email does not exists.", userEmail))
        );
    }
    public List<ResponseUserDTO> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::fromUserToResponseUserDTO)
                .collect(Collectors.toList());
    }
}
