package com.bootcamp.rules_engine.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rule {
    
    @Id
    private UUID id;

    private String name;

    private String rule;

    public boolean evaluate(){
        
        return true;
    }
}
