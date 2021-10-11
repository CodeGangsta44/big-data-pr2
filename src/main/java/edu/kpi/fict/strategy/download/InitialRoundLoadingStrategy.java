package edu.kpi.fict.strategy.download;

import edu.kpi.fict.model.Configuration;
import edu.kpi.fict.service.integration.RoundIntegrationService;
import edu.kpi.fict.service.integration.SeasonIntegrationService;
import edu.kpi.fict.service.persistence.*;
import org.springframework.stereotype.Component;

@Component
public class InitialRoundLoadingStrategy extends AbstractRoundLoadingStrategy {

    public InitialRoundLoadingStrategy(final SeasonIntegrationService seasonIntegrationService,
                                       final RoundIntegrationService roundIntegrationService, final RaceService raceService,
                                       final ConfigurationService configurationService, final RaceResultService raceResultService,
                                       final CircuitService circuitService, final DriverService driverService, final ConstructorService constructorService,
                                       final PitStopService pitStopService) {

        super(seasonIntegrationService, roundIntegrationService, raceService, configurationService, raceResultService,
                circuitService, driverService, constructorService, pitStopService);
    }

    public void execute() {

        getSeasonIntegrationService().getSeasons()
                .forEach(this::loadRacesForSeason);

        updateConfiguration(new Configuration());
    }
}
