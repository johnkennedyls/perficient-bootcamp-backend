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
public class TableData {
    @Id
    private UUID tableId;
    private String name;
    @Transient
    private List<String> headers;
    @Transient
    private List<String[]> rows;
    @ElementCollection
    private List<String> types;
}
