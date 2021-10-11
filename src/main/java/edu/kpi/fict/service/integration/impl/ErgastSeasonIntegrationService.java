package edu.kpi.fict.service.integration.impl;

import edu.kpi.fict.dto.*;
import edu.kpi.fict.service.integration.SeasonIntegrationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.kpi.fict.constants.FormulaOneProcessorConstants.Integration.SEASON_PLACEHOLDER;

@Service
public class ErgastSeasonIntegrationService implements SeasonIntegrationService {

    private final RestTemplate ergastRestTemplate;

    private final String allSeasonsUrl;
    private final String roundsOfSeasonUrl;

    public ErgastSeasonIntegrationService(final RestTemplate ergastRestTemplate,
                                          @Value("${ergast.api.all.seasons.url}") final String allSeasonsUrl,
                                          @Value("${ergast.api.rounds.of.season.url}") final String roundsOfSeasonUrl) {
        this.ergastRestTemplate = ergastRestTemplate;
        this.allSeasonsUrl = allSeasonsUrl;
        this.roundsOfSeasonUrl = roundsOfSeasonUrl;
    }

    @Override
    public List<String> getSeasons() {

        return Optional.of(ergastRestTemplate.getForEntity(allSeasonsUrl, GenericResponseDto.class))
                .filter(response -> response.getStatusCode().is2xxSuccessful())
                .map(ResponseEntity::getBody)
                .map(GenericResponseDto::getMrData)
                .map(MRDto::getSeasonTableDto)
                .map(SeasonTableDto::getSeasons)
                .orElse(Collections.emptyList())
                .stream()
                .map(SeasonDto::getSeason)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRoundsOfSeason(final String season) {

        return Optional.of(ergastRestTemplate.getForEntity(buildRoundsOfSeasonUrl(season), GenericResponseDto.class))
                .filter(response -> response.getStatusCode().is2xxSuccessful())
                .map(ResponseEntity::getBody)
                .map(GenericResponseDto::getMrData)
                .map(MRDto::getRaceTable)
                .map(RaceTableDto::getRaces)
                .orElse(Collections.emptyList())
                .stream()
                .map(RaceDto::getRound)
                .collect(Collectors.toList());
    }

    private String buildRoundsOfSeasonUrl(final String season) {

        return roundsOfSeasonUrl.replace(SEASON_PLACEHOLDER, season);
    }
}
