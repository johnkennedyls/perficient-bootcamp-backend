package com.bootcamp.rules_engine.integrationTests;

import com.bootcamp.rules_engine.dto.request.LoginDTO;
import com.bootcamp.rules_engine.dto.request.RoleDTO;
import com.bootcamp.rules_engine.dto.request.RuleDTO;
import com.bootcamp.rules_engine.dto.request.TokenDTO;
import com.bootcamp.rules_engine.enums.UserRole;
import com.bootcamp.rules_engine.service.RuleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles="test")
public class RuleControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private RuleService ruleService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateRule() throws Exception {
        RuleDTO ruleDTO = defaultRuleDTO();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/rules/create")
                    .content(objectMapper.writeValueAsString(ruleDTO))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.name").value("testRule"))
                //.andExpect(jsonPath("$.rule").value("testHeader1 = testHeader2"))
                .andReturn();
        System.out.println("CREATE RULE RESPONSE: " + result.getResponse().getContentAsString());
    }

    @Test
    public void testGetRule() throws Exception {
        when(ruleService.getRule("RuleTest1")).thenReturn(
            new RuleDTO("RuleTest1", "1<2"));

        mockMvc.perform(MockMvcRequestBuilders.get("/rules/{name}", "RuleTest1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("RuleTest1"))
                .andExpect(jsonPath("$.rule").value("1<2"));
    }

    @Test
    public void testGetAllRules() throws Exception {
        when(ruleService.getAllRules()).thenReturn(Arrays.asList(
            new RuleDTO("RuleTest1", "1<2"),
            new RuleDTO("RuleTest2", "1>2"),
            new RuleDTO("RuleTest3", "1=2")
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/rules/getRules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("RuleTest1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("RuleTest2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("RuleTest3"));
    }

    @Test
    public void testEvaluateRuleToRegister() throws Exception {
        when(ruleService.evaluateRuleToRegister("RuleTest1", "tableTest1", 0))
            .thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/rules/evaluate/register/{ruleName}/{tableName}/{rowPosition}", "RuleTest1", "tableTest1", 0))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testEvaluateRuleToTable() throws Exception {
        when(ruleService.evaluateRuleToTable("RuleTest1", "tableTest1"))
            .thenReturn(Arrays.asList(true, false));

        mockMvc.perform(MockMvcRequestBuilders.get("/rules/evaluate/table/{ruleName}/{tableName}", "RuleTest1", "tableTest1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value(true))
                .andExpect(jsonPath("$[1]").value(false));
    }

    @Test
    public void testEvaluateRuleToRegistersList() throws Exception {
        when(ruleService.evaluateRuleToRegistersList("RuleTest1", "tableTest1", 
            Arrays.asList(0,1)))
            .thenReturn(Arrays.asList(true, false));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/rules/evaluate/List/{ruleName}/{tableName}/{rowPositions}", "rule1", "table1", "0,1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                //.andExpect(jsonPath("$[0]").value(true))
                //.andExpect(jsonPath("$[1]").value(false))
                .andReturn();
        System.out.println("EVALUATE RULE RESPONSE: " + result.getResponse().getContentAsString());

    }

    private RuleDTO defaultRuleDTO() {
        return RuleDTO.builder()
                .name("testRule")
                .rule("testHeader1 = testHeader2")
                .build();
    }
}
