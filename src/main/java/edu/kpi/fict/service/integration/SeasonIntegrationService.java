package edu.kpi.fict.service.integration;

import java.util.List;

public interface SeasonIntegrationService {

    List<String> getSeasons();

    List<String> getRoundsOfSeason(final String season);
}
