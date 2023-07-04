package com.bootcamp.rules_engine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.bootcamp.rules_engine.model.TableData;
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
    private final TableDataRepository tableRepository;

    public RuleDTO createRule(RuleDTO ruleDTO){
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
        this.repository.save(newRule);
        return mapper.fromRuleToRuleDTO(newRule);

    }

    public RuleDTO getRule(String ruleName) {
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

    public void deleteRule(String ruleName){
        checkPermissions();
        Rule rule = repository.findByName(ruleName).orElseThrow(
                        createRulesEngineException(
                                "The rule with the specified name does not exists.",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404, "Rule", "name", ruleName)
                        )
                    );
        this.repository.deleteById(rule.getId());
    }

    public List<RuleDTO> getAllRules() {
        return repository.findAll()
                .stream()
                .map(mapper::fromRuleToRuleDTO)
                .collect(Collectors.toList());
    }

    public List<Boolean> evaluateRuleToTable(String rulename, String tableName){
        List<Boolean> results = new ArrayList<Boolean>();
        Rule rule = repository.findByName(rulename).orElseThrow(
                        createRulesEngineException(
                                "The rule with the specified name does not exists.",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404, "Rule", "name", rulename)
                        )
                    );
        String expression = rule.getRule();
        TableData table = tableRepository.findByName(tableName).orElseThrow(
                        createRulesEngineException(
                                "The table with the specified name does not exists.",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404, "TableData", "name", tableName)
                        )
                    );

        List<String> headers = table.getHeaders();
        List<String[]> rows = table.getRows();
        for(String[] row : rows){
            results.add(evaluateRule(expression, headers, row));
        }
        return results;
    }

    public List<Boolean> evaluateRuleToRegistersList(String rulename, String tableName, List<Integer> rowsPosition){
        List<Boolean> results = new ArrayList<Boolean>();
        List<String[]> rowsToEvaluate = new ArrayList<String[]>();
        Rule rule = repository.findByName(rulename).orElseThrow(
                        createRulesEngineException(
                                "The rule with the specified name does not exists.",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404, "Rule", "name", rulename)
                        )
                    );
        String expression = rule.getRule();
        TableData table = tableRepository.findByName(tableName).orElseThrow(
                        createRulesEngineException(
                                "The table with the specified name does not exists.",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404, "TableData", "name", tableName)
                        )
                    );
        List<String> headers = table.getHeaders();
        List<String[]> rows = table.getRows();
        for(Integer elementIndex : rowsPosition){
            rowsToEvaluate.add(rows.get(elementIndex));
        }
        for(String[] row : rowsToEvaluate){
            results.add(evaluateRule(expression, headers, row));
        }
        return results;

    }

    public boolean evaluateRuleToRegister(String rulename, String tableName, int rowPosition){
        Rule rule = repository.findByName(rulename).orElseThrow(
                        createRulesEngineException(
                                "The rule with the specified name does not exists.",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404, "Rule", "name", rulename)
                        )
                    );
        String expression = rule.getRule();
        TableData table = tableRepository.findByName(tableName).orElseThrow(
                        createRulesEngineException(
                                "The table with the specified name does not exists.",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404, "TableData", "name", tableName)
                        )
                    );
        List<String> headers = table.getHeaders();
        String[] row = table.getRows().get(rowPosition);
        return evaluateRule(expression, headers, row);
    }

    public boolean evaluateRule(String expression, List<String> headers, String[] row) {
        String[] elements = expression.split(" ");
        Stack<String> stack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        for (String element : elements) {
            if (element.equals("("))  {

                operatorStack.push(element);
            } else if (element.equals(")")) {

                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    processOperator(stack, operatorStack);
                }

                if (!operatorStack.isEmpty() && operatorStack.peek().equals("(")) {
                    operatorStack.pop();
                }
            } else if (element.equals("!=") || element.equals(">") || element.equals("<") || element.equals("=")
                || element.equals(">=") || element.equals("<=")) {

                operatorStack.push(element);
            } else if (element.equals("&") || element.equals("|")) {

                processOperator(stack, operatorStack);
                operatorStack.push(element);
            } else {
                if(!(isBoolean(element) || isNumber(element))){
                    int columnPosition = isColumn(element, headers);
                    if(columnPosition != -1){
                        element = ""+row[columnPosition];
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
                return operand1.equals(operand2);
            case "!=":
                return !(operand1.equals(operand2));
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

    public int isColumn(String element, List<String> headers){
        int result=-1;
        for (int i=0;i < headers.size();i++) {
            if(element.equals(headers.get(i))){
                result = i;
                break;
            }
        }
        return result;
    }

    public void checkPermissions() {
        if(!RulesEngineSecurityContext.getCurrentUserRole().equals(UserRole.RESEARCHER.getRole())){
            throw createRulesEngineException(
                    "Only an RESEARCHER user can modify rules data",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403)
            ).get();
        }
    }
    
}


