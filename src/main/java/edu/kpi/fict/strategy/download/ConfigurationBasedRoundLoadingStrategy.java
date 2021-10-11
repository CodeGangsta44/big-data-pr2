package edu.kpi.fict.strategy.download;

import edu.kpi.fict.model.Configuration;
import edu.kpi.fict.service.integration.RoundIntegrationService;
import edu.kpi.fict.service.integration.SeasonIntegrationService;
import edu.kpi.fict.service.persistence.*;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationBasedRoundLoadingStrategy extends AbstractRoundLoadingStrategy {

    public ConfigurationBasedRoundLoadingStrategy(final SeasonIntegrationService seasonIntegrationService, final RoundIntegrationService roundIntegrationService,
                                                  final RaceService raceService, final ConfigurationService configurationService, final RaceResultService raceResultService,
                                                  final CircuitService circuitService, final DriverService driverService, final ConstructorService constructorService,
                                                  final PitStopService pitStopService) {

        super(seasonIntegrationService, roundIntegrationService, raceService, configurationService, raceResultService,
                circuitService, driverService, constructorService, pitStopService);
    }

    public void execute(final Configuration configuration) {

        continueLoadingForSeason(configuration);
        continueLoading(configuration);
        updateConfiguration(configuration);
    }

    private void continueLoadingForSeason(final Configuration configuration) {

        final String season = String.valueOf(configuration.getLastDownloadedSeason());

        System.out.println("Starting loading season " + season + "... ");
        System.out.print("Rounds:");

        getSeasonIntegrationService().getRoundsOfSeason(season)
                .stream()
                .filter(round -> Integer.parseInt(round) > configuration.getLastDownloadedRound())
                .forEach(round -> loadRace(season, round));

        System.out.println(" [DONE]");
    }

    private void continueLoading(final Configuration configuration) {

        getSeasonIntegrationService().getSeasons()
                .stream()
                .filter(season -> configuration.getLastDownloadedSeason() < Integer.parseInt(season))
                .forEach(this::loadRacesForSeason);
    }
}
