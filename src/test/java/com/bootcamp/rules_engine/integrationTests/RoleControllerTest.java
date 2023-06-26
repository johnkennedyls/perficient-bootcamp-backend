package com.bootcamp.rules_engine.integrationTests;

import com.bootcamp.rules_engine.dto.request.LoginDTO;
import com.bootcamp.rules_engine.dto.request.RoleDTO;
import com.bootcamp.rules_engine.dto.request.TokenDTO;
import com.bootcamp.rules_engine.enums.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles="test")
public class RoleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateARoleHappy_Path() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("lauramartinez@gmail.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create").content(
                                objectMapper.writeValueAsString(
                                        RoleDTO.builder()
                                                .description("Role for a person")
                                                .roleName("MANAGER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateARoleWithANameThatAlreadyExists() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("lauramartinez@gmail.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create").content(
                                objectMapper.writeValueAsString(
                                        RoleDTO.builder()
                                                .description("Role for a person")
                                                .roleName(UserRole.CONSULTANT.getRole())
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateARoleWithANullName() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("lauramartinez@gmail.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create").content(
                                objectMapper.writeValueAsString(
                                        RoleDTO.builder()
                                                .description("Role for a person")
                                                .roleName(null)
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateARoleWithANullDescription() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("lauramartinez@gmail.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create").content(
                                objectMapper.writeValueAsString(
                                        RoleDTO.builder()
                                                .description(null)
                                                .roleName("MANAGER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateARoleWithABlankName() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("lauramartinez@gmail.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create").content(
                                objectMapper.writeValueAsString(
                                        RoleDTO.builder()
                                                .description("Role for a person")
                                                .roleName("")
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateARoleWithABlankDescription() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("lauramartinez@gmail.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create").content(
                                objectMapper.writeValueAsString(
                                        RoleDTO.builder()
                                                .description("")
                                                .roleName("MANAGER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateARoleWhenLoggedUserIsNotAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("kerenlopez@gmail.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create").content(
                                objectMapper.writeValueAsString(
                                        RoleDTO.builder()
                                                .description("Role for a person")
                                                .roleName("MANAGER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testGetARole() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("lauramartinez@gmail.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/roles/{name}", UserRole.RESEARCHER.getRole())
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testGetARoleWhenLoggedUserIsNotAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("kerenlopez@gmail.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/roles/{name}", UserRole.RESEARCHER.getRole())
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testGetAllCategories() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("lauramartinez@gmail.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/roles/getRoles")
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

}
