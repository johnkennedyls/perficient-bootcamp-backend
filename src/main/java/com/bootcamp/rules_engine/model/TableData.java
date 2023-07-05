package com.bootcamp.rules_engine.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class TableData {
    @Id
    private UUID tableId;
    private String name;
    private String ownerEmail;
    @ElementCollection
    private List<String> headers;
    @ElementCollection
    private List<String[]> rows;
    @ElementCollection
    private List<String> types;
}
