package edu.kpi.fict.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PitStopDto {

    private String driverId;
    private String stop;
    private String lap;
    private String duration;
}
