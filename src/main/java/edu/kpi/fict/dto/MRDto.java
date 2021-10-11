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
public class MRDto {

    @JsonProperty("RaceTable")
    private RaceTableDto raceTable;

    @JsonProperty("SeasonTable")
    private SeasonTableDto seasonTableDto;
}
