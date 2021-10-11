package edu.kpi.fict.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConstructorDto {

    private String constructorId;
    private String name;
    private String nationality;
}
