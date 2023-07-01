package com.bootcamp.rules_engine.service;

import java.util.List;
import java.util.Stack;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bootcamp.rules_engine.dto.request.RuleDTO;
import com.bootcamp.rules_engine.enums.ErrorCode;
import com.bootcamp.rules_engine.enums.UserRole;
import com.bootcamp.rules_engine.error.exception.DetailBuilder;
import com.bootcamp.rules_engine.mapper.RuleMapper;
import com.bootcamp.rules_engine.model.Rule;
import com.bootcamp.rules_engine.repository.RuleRepository;
import com.bootcamp.rules_engine.repository.TableDataRepository;
import com.bootcamp.rules_engine.security.RulesEngineSecurityContext;

import static com.bootcamp.rules_engine.error.util.RulesEngineExceptionBuilder.createRulesEngineException;


import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RuleService{
    
    private final RuleRepository repository;
    private final RuleMapper mapper;

    public Rule createRule(RuleDTO ruleDTO){
        checkPermissions();
        if(repository.isNameInUse(ruleDTO.getName())){
            throw createRulesEngineException(
                    "Another rule already has this name.",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "Rule", "name", ruleDTO.getName())
            ).get();
        }
        Rule newRule = mapper.fromRuleDTO(ruleDTO); 
        newRule.setId(UUID.randomUUID());
        return this.repository.save(newRule);
    }

    public RuleDTO getRule(String ruleName) {
        checkPermissions();
        return mapper.fromRuleToRuleDTO(
                repository.findByName(ruleName).orElseThrow(
                        createRulesEngineException(
                                "The rule with the specified name does not exists.",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404, "Rule", "name", ruleName)
                        )
                )
        );
    }

    public List<RuleDTO> getAllRules() {
        return repository.findAll()
                .stream()
                .map(mapper::fromRuleToRuleDTO)
                .collect(Collectors.toList());
    }

    public boolean evaluateRule(String ruleName, String[] headers, String[] row) {
        String expression= getRule(ruleName).getRule();
        String[] elements = expression.split(" ");
        Stack<String> stack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        for (String element : elements) {
            if (element == "(")  {

                operatorStack.push(element);
            } else if (element == ")") {

                while (!operatorStack.isEmpty() && operatorStack.peek() != "(") {
                    processOperator(stack, operatorStack);
                }

                if (!operatorStack.isEmpty() && operatorStack.peek() == "(") {
                    operatorStack.pop();
                }
            } else if (element == "!=" || element == ">" || element == "<" || element == "="
                || element == ">=" || element == "<=") {

                operatorStack.push(element);
            } else if (element == "&" || element == "|") {

                processOperator(stack, operatorStack);
                operatorStack.push(element);
            } else {                
                //TODO Reemplazar valores del registro de la tabla

                if(!(isBoolean(element) || isNumber(element))){
                    int columnPosition = isColumn(element, headers);
                    if(columnPosition != -1){
                        element = row[columnPosition];
                    }
                }

                stack.push(element);
            }
        }

        while (!operatorStack.isEmpty()) {
            processOperator(stack, operatorStack);
        }

        return Boolean.parseBoolean(stack.pop());
    }

    public void processOperator(Stack<String> stack, Stack<String> operatorStack) {
        String operator = operatorStack.pop();
        String element2 = stack.pop();
        String element1 = stack.pop();
        String result;

        if(isBoolean(element1) && isBoolean(element2)){
            boolean operand2 = Boolean.parseBoolean(element2);
            boolean operand1 = Boolean.parseBoolean(element1);
            result = "" + performBooleanOperation(operand1, operand2, operator);
        }else if (isNumber(element1) && isNumber(element2)){
            double operand2 = Double.parseDouble(element2);
            double operand1 = Double.parseDouble(element1);
            result = "" + performNumericOperation(operand1, operand2, operator);
        } else {
            String operand2 = element2;
            String operand1 = element1;
            result = "" + performTextOperation(operand1, operand2, operator); 
        }

        
        stack.push(result);

    }

    public boolean performBooleanOperation(boolean operand1, boolean operand2, String operator) {
        switch (operator) {
            case "=":
                return operand1 == operand2;
            case "!=":
                return operand1 != operand2;
            case "&":
                return operand1 && operand2;
            case "|":
                return operand1 || operand2;
            default:
                return false;
        }
    }

    public boolean performNumericOperation(double operand1, double operand2, String operator) {
        switch (operator) {
            case "=":
                return operand1 == operand2;
            case "!=":
                return operand1 != operand2;
            case "<":
                return operand1 < operand2;
            case "<=":
                return operand1 <= operand2;
            case ">":
                return operand1 > operand2;
            case ">=":
                return operand1 >= operand2;
            default:
                return false;
        }
    }

    public boolean performTextOperation(String operand1, String operand2, String operator) {
        switch (operator) {
            case "=":
                return operand1 == operand2;
            case "!=":
                return operand1 != operand2;
            default:
                return false;
        }
    }

    public boolean isBoolean(String cadena){
        return cadena.equalsIgnoreCase("true") || cadena.equalsIgnoreCase("false");
    }

    public boolean isNumber(String cadena){
        try {
        Double.parseDouble(cadena);
        return true;
        } catch (NumberFormatException e) {
        return false;
        }
    }

    public int isColumn(String element, String[] headers){
        int result=-1;
        for (int i=0;i < headers.length;i++) {
            if(element.equals(headers[i])){
                result = i;
                break;
            }
        }
        return result;
    }

    public void checkPermissions() {
        if(!RulesEngineSecurityContext.getCurrentUserRole().equals(UserRole.RESEARCHER.getRole())){
            throw createRulesEngineException(
                    "Only an RESEARCHER user can create new rules",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403)
            ).get();
        }
    }
    
}


