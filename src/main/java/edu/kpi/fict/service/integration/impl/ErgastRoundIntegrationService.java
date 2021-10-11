package edu.kpi.fict.service.integration.impl;

import edu.kpi.fict.dto.*;
import edu.kpi.fict.model.*;
import edu.kpi.fict.service.integration.RoundIntegrationService;
import edu.kpi.fict.service.persistence.CircuitService;
import edu.kpi.fict.service.persistence.ConstructorService;
import edu.kpi.fict.service.persistence.DriverService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static edu.kpi.fict.constants.FormulaOneProcessorConstants.Integration.ROUND_PLACEHOLDER;
import static edu.kpi.fict.constants.FormulaOneProcessorConstants.Integration.SEASON_PLACEHOLDER;

@Service
public class ErgastRoundIntegrationService implements RoundIntegrationService {

    private final CircuitService circuitService;
    private final DriverService driverService;
    private final ConstructorService constructorService;
    private final RestTemplate ergastRestTemplate;

    private final String roundUrl;
    private final String pitStopsUrl;

    private final Map<String, Circuit> circuitMap;
    private final Map<String, Driver> driverMap;
    private final Map<String, Constructor> constructorMap;

    public ErgastRoundIntegrationService(final CircuitService circuitService, final DriverService driverService,
                                         final ConstructorService constructorService, final RestTemplate ergastRestTemplate,
                                         @Value("${ergast.api.round.url}") final String roundUrl,
                                         @Value("${ergast.api.pit.stops.url}") final String pitStopsUrl) {

        this.circuitService = circuitService;
        this.driverService = driverService;
        this.constructorService = constructorService;
        this.ergastRestTemplate = ergastRestTemplate;
        this.roundUrl = roundUrl;
        this.pitStopsUrl = pitStopsUrl;

        circuitMap = new HashMap<>();
        driverMap = new HashMap<>();
        constructorMap = new HashMap<>();
    }

    @Override
    public Optional<Race> getRace(final String year, final String round) {

        return Optional.of(ergastRestTemplate.getForEntity(buildRoundIntegrationUrl(year, round), GenericResponseDto.class))
                .filter(response -> response.getStatusCode().is2xxSuccessful())
                .map(ResponseEntity::getBody)
                .flatMap(this::mapToRace);
    }

    private Optional<Race> mapToRace(final GenericResponseDto roundResultDto) {

        return Optional.ofNullable(roundResultDto)
                .map(GenericResponseDto::getMrData)
                .map(MRDto::getRaceTable)
                .map(RaceTableDto::getRaces)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::createRace)
                .findAny();
    }

    private Race createRace(final RaceDto raceDto) {

        return Race.builder()
                .season(Integer.parseInt(raceDto.getSeason()))
                .round(Integer.parseInt(raceDto.getRound()))
                .circuit(getCircuit(raceDto.getCircuit()))
                .results(createResults(raceDto.getResults()))
                .pitStops(createPitStops(raceDto))
                .build();
    }

    private synchronized Circuit getCircuit(final CircuitDto circuitDto) {

        return circuitMap.computeIfAbsent(circuitDto.getCircuitId(),
                key -> circuitService.getById(key)
                        .orElseGet(() -> createCircuit(circuitDto)));
    }

    private Circuit createCircuit(final CircuitDto circuitDto) {

        return Circuit.builder()
                .circuitId(circuitDto.getCircuitId())
                .circuitName(circuitDto.getCircuitName())
                .build();
    }

    private List<RaceResult> createResults(final List<RaceResultDto> raceResults) {

        return Optional.ofNullable(raceResults)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::createRaceResult)
                .collect(Collectors.toList());
    }

    private RaceResult createRaceResult(final RaceResultDto raceResultDto) {

        return RaceResult.builder()
                .position(Integer.parseInt(raceResultDto.getPosition()))
                .points(Double.parseDouble(raceResultDto.getPoints()))
                .driver(getDriver(raceResultDto.getDriver()))
                .constructor(getConstructor(raceResultDto.getConstructor()))
                .build();
    }

    private synchronized Driver getDriver(final DriverDto driverDto) {

        return driverMap.computeIfAbsent(driverDto.getDriverId(),
                key -> driverService.getById(key)
                        .orElseGet(() -> createDriver(driverDto)));
    }

    private Driver createDriver(final DriverDto driverDto) {

        return Driver.builder()
                .driverId(driverDto.getDriverId())
                .code(driverDto.getCode())
                .permanentNumber(driverDto.getPermanentNumber())
                .firstName(driverDto.getGivenName())
                .lastName(driverDto.getFamilyName())
                .nationality(driverDto.getNationality())
                .build();
    }

    private synchronized Constructor getConstructor(final ConstructorDto constructorDto) {

        return constructorMap.computeIfAbsent(constructorDto.getConstructorId(),
                key -> constructorService.getById(key)
                        .orElseGet(() -> createConstructor(constructorDto)));
    }

    private Constructor createConstructor(final ConstructorDto constructorDto) {

        return Constructor.builder()
                .constructorId(constructorDto.getConstructorId())
                .name(constructorDto.getName())
                .nationality(constructorDto.getNationality())
                .build();
    }

    private List<PitStop> createPitStops(final RaceDto raceDto) {

        return Optional.of(ergastRestTemplate.getForEntity(buildPitStopsIntegrationUrl(raceDto.getSeason(), raceDto.getRound()), GenericResponseDto.class))
                .filter(response -> response.getStatusCode().is2xxSuccessful())
                .map(ResponseEntity::getBody)
                .map(GenericResponseDto::getMrData)
                .map(MRDto::getRaceTable)
                .map(RaceTableDto::getRaces)
                .orElse(Collections.emptyList())
                .stream()
                .map(RaceDto::getPitStops)
                .flatMap(Collection::stream)
                .map(this::createPitStop)
                .collect(Collectors.toList());
    }

    private PitStop createPitStop(final PitStopDto pitStopDto) {

        return PitStop.builder()
                .driver(driverMap.get(pitStopDto.getDriverId()))
                .stop(pitStopDto.getStop())
                .lap(pitStopDto.getLap())
                .duration(pitStopDto.getDuration())
                .build();
    }

    private String buildRoundIntegrationUrl(final String year, final String round) {

        return roundUrl.replace(SEASON_PLACEHOLDER, year)
                .replace(ROUND_PLACEHOLDER, round);
    }

    private String buildPitStopsIntegrationUrl(final String year, final String round) {

        return pitStopsUrl.replace(SEASON_PLACEHOLDER, year)
                .replace(ROUND_PLACEHOLDER, round);
    }
}
