package edu.kpi.fict.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaceResultDto {

    private String position;
    private String points;

    @JsonProperty("Driver")
    private DriverDto driver;

    @JsonProperty("Constructor")
    private ConstructorDto constructor;
}
