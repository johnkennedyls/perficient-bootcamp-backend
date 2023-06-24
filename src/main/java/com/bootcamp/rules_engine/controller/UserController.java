package com.bootcamp.rules_engine.controller;

import com.bootcamp.rules_engine.api.UserAPI;
import com.bootcamp.rules_engine.dto.request.RequestUserDTO;
import com.bootcamp.rules_engine.dto.response.ResponseUserDTO;
import com.bootcamp.rules_engine.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.bootcamp.rules_engine.api.UserAPI.BASE_USER_URL;

@RestController
@RequestMapping(BASE_USER_URL)
@AllArgsConstructor
public class UserController implements UserAPI {
    private final UserService userService;

    @Override
    public ResponseUserDTO getUser(String userEmail) {
        return userService.getUser(userEmail);
    }


    @Override
    public List<ResponseUserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public ResponseUserDTO createUser(RequestUserDTO requestUserDTO) {
        return userService.saveUser(requestUserDTO);
    }
}
