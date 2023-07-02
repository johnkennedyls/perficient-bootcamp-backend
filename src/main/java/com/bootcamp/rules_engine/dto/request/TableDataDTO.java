package com.bootcamp.rules_engine.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableDataDTO {
    private MultipartFile file;
}
