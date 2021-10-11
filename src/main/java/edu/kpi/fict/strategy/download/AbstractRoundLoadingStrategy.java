package edu.kpi.fict.strategy.download;

import edu.kpi.fict.model.*;
import edu.kpi.fict.service.integration.RoundIntegrationService;
import edu.kpi.fict.service.integration.SeasonIntegrationService;
import edu.kpi.fict.service.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractRoundLoadingStrategy {

    private final SeasonIntegrationService seasonIntegrationService;
    private final RoundIntegrationService roundIntegrationService;
    private final RaceService raceService;
    private final ConfigurationService configurationService;
    private final RaceResultService raceResultService;
    private final CircuitService circuitService;
    private final DriverService driverService;
    private final ConstructorService constructorService;
    private final PitStopService pitStopService;
    private final ForkJoinPool pool;

    public AbstractRoundLoadingStrategy(final SeasonIntegrationService seasonIntegrationService,
                                        final RoundIntegrationService roundIntegrationService, final RaceService raceService,
                                        final ConfigurationService configurationService, final RaceResultService raceResultService,
                                        final CircuitService circuitService, final DriverService driverService,
                                        final ConstructorService constructorService, final PitStopService pitStopService) {

        this.seasonIntegrationService = seasonIntegrationService;
        this.roundIntegrationService = roundIntegrationService;
        this.raceService = raceService;
        this.configurationService = configurationService;
        this.raceResultService = raceResultService;
        this.circuitService = circuitService;
        this.driverService = driverService;
        this.constructorService = constructorService;
        this.pitStopService = pitStopService;
        pool = new ForkJoinPool(20);
    }

    protected void loadRacesForSeason(final String season) {

        System.out.println("Starting loading season " + season + "... ");
        System.out.print("Rounds:");

        final Runnable runnable = () ->
            saveRaces(seasonIntegrationService.getRoundsOfSeason(season)
                    .stream()
                    .parallel()
                    .map(round -> roundIntegrationService.getRace(season, round))
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList()));

        try {

            pool.submit(runnable).get();

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();
        }

        System.out.println(" [DONE]");
    }

    protected void loadRace(final String season, final String round) {

        roundIntegrationService.getRace(season, round)
                .ifPresent(this::saveRace);
    }

    private synchronized void saveRace(final Race race) {

        System.out.print(" " + race.getRound());

        race.getResults().forEach(raceResultService::save);
        raceService.save(race);
    }

    private synchronized void saveRaces(final List<Race> races) {

        System.out.print(" races qty=" + races.size());

        final List<Circuit> circuits = new ArrayList<>();
        final List<Driver> drivers = new ArrayList<>();
        final List<Constructor> constructors = new ArrayList<>();

        final List<RaceResult> results = races.stream()
                .peek(race -> {
                    if (race.getCircuit().getId() == null) {
                        circuits.add(race.getCircuit());
                    }
                })
                .flatMap(this::getResultsForRace)
                .peek(result -> {
                    if (result.getDriver().getId() == null) {
                        drivers.add(result.getDriver());
                    }
                    if (result.getConstructor().getId() == null) {
                        constructors.add(result.getConstructor());
                    }
                })
                .collect(Collectors.toList());

        final List<PitStop> pitStops = races.stream()
                .flatMap(this::getPitStopsForRace)
                .collect(Collectors.toList());

        circuitService.save(circuits);
        driverService.save(drivers);
        constructorService.save(constructors);
        raceService.save(races);
    }

    private Stream<RaceResult> getResultsForRace(final Race race) {

        return race.getResults().stream()
                .peek(result -> result.setRace(race));
    }

    private Stream<PitStop> getPitStopsForRace(final Race race) {

        return race.getPitStops().stream()
                .peek(pitStop -> pitStop.setRace(race));
    }

    protected void updateConfiguration(final Configuration configuration)
    {
        final Optional<Integer> lastLoadedSeason = raceService.findLastLoadedSeason();
        final Optional<Integer> lastLoadedRound = lastLoadedSeason.flatMap(raceService::findLastLoadedRound);

        lastLoadedSeason.ifPresent(configuration::setLastDownloadedSeason);
        lastLoadedRound.ifPresent(configuration::setLastDownloadedRound);

        configurationService.save(configuration);
    }

    public SeasonIntegrationService getSeasonIntegrationService() {
        return seasonIntegrationService;
    }

    public RoundIntegrationService getRoundIntegrationService() {
        return roundIntegrationService;
    }

    public RaceService getRaceService() {
        return raceService;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public RaceResultService getRaceResultService() {
        return raceResultService;
    }
}
