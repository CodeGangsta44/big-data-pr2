package edu.kpi.fict.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaceDto {

    private String season;
    private String round;

    @JsonProperty("Circuit")
    private CircuitDto circuit;

    @JsonProperty("Results")
    private List<RaceResultDto> results;

    @JsonProperty("PitStops")
    private List<PitStopDto> pitStops;
}
