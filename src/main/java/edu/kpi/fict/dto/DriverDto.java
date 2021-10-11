package edu.kpi.fict.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverDto {

    private String driverId;
    private String code;
    private String permanentNumber;
    private String givenName;
    private String familyName;
    private String nationality;
}
