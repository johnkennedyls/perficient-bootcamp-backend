package com.bootcamp.rules_engine.api;

import com.bootcamp.rules_engine.dto.request.RoleDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(RoleAPI.BASE_ROLE_URL)
public interface RoleAPI {
    String BASE_ROLE_URL="/roles";
    @PostMapping("/create")
    RoleDTO createRole(@RequestBody @Valid RoleDTO roleDTO);
    @GetMapping("/{name}")
    RoleDTO getRole(@PathVariable("name") String roleName);
    @GetMapping("/getRoles")
    List<RoleDTO> getAllRoles();
}
