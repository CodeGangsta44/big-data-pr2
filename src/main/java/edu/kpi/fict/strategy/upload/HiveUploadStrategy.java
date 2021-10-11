package edu.kpi.fict.strategy.upload;

import edu.kpi.fict.dao.HiveDao;
import edu.kpi.fict.dao.PartitionedHiveDao;
import edu.kpi.fict.model.*;
import edu.kpi.fict.service.persistence.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class HiveUploadStrategy {

    private static final int BATCH_SIZE = 50;

    private final DriverService driverService;
    private final ConstructorService constructorService;
    private final CircuitService circuitService;
    private final RaceService raceService;
    private final RaceResultService raceResultService;
    private final PitStopService pitStopService;

    private final HiveDao<Driver> driverHiveDao;
    private final HiveDao<Constructor> constructorHiveDao;
    private final HiveDao<Circuit> circuitHiveDao;

    private final PartitionedHiveDao<Race> racePartitionedHiveDao;
    private final PartitionedHiveDao<RaceResult> raceResultPartitionedHiveDao;
    private final PartitionedHiveDao<PitStop> pitStopPartitionedHiveDao;

    private final String hiveConnectionUrl;
    private final String hiveUsername;
    private final String hivePassword;

    public HiveUploadStrategy(final DriverService driverService,
                              final ConstructorService constructorService,
                              final CircuitService circuitService,
                              final RaceService raceService,
                              final RaceResultService raceResultService,
                              final PitStopService pitStopService,
                              final HiveDao<Driver> driverHiveDao,
                              final HiveDao<Constructor> constructorHiveDao,
                              final HiveDao<Circuit> circuitHiveDao,
                              final PartitionedHiveDao<Race> racePartitionedHiveDao,
                              final PartitionedHiveDao<RaceResult> raceResultPartitionedHiveDao,
                              final PartitionedHiveDao<PitStop> pitStopPartitionedHiveDao,
                              @Value("${hive.connection.url}") final String hiveConnectionUrl,
                              @Value("${hive.username}") final String hiveUsername,
                              @Value("${hive.password}") final String hivePassword) {

        this.driverService = driverService;
        this.constructorService = constructorService;
        this.circuitService = circuitService;
        this.raceService = raceService;
        this.raceResultService = raceResultService;
        this.pitStopService = pitStopService;
        this.driverHiveDao = driverHiveDao;
        this.constructorHiveDao = constructorHiveDao;
        this.circuitHiveDao = circuitHiveDao;
        this.racePartitionedHiveDao = racePartitionedHiveDao;
        this.raceResultPartitionedHiveDao = raceResultPartitionedHiveDao;
        this.pitStopPartitionedHiveDao = pitStopPartitionedHiveDao;

        this.hiveConnectionUrl = emptyOnNull(hiveConnectionUrl);
        this.hiveUsername = emptyOnNull(hiveUsername);
        this.hivePassword = emptyOnNull(hivePassword);
    }

    public void execute() {

        final List<Driver> driversToLoad = driverService.getNotLoaded();
        final List<Constructor> constructorsToLoad = constructorService.getNotLoaded();
        final List<Circuit> circuitsToLoad = circuitService.getNotLoaded();

        try (var connection = createConnection()) {

            loadDrivers(driversToLoad, connection);
            loadConstructors(constructorsToLoad, connection);
            loadCircuits(circuitsToLoad, connection);

            loadRaces(connection);

        } catch (final SQLException e) {

            e.printStackTrace();
        }
    }

    private void loadDrivers(final List<Driver> drivers, final Connection connection) throws SQLException {

        if  (!drivers.isEmpty()) {

            System.out.print("Loading " + drivers.size() + " drivers... ");

            driverHiveDao.save(connection, drivers);

            drivers.forEach(driver -> driver.setUploaded(Boolean.TRUE));

            driverService.save(drivers);

            System.out.println("[DONE]");

        } else {

            System.out.println("No new drivers to upload.");
        }
    }

    private void loadConstructors(final List<Constructor> constructors, final Connection connection) throws SQLException {

        if (!constructors.isEmpty()) {

            System.out.print("Loading " + constructors.size() + " constructors... ");

            constructorHiveDao.save(connection, constructors);

            constructors.forEach(constructor -> constructor.setUploaded(Boolean.TRUE));

            constructorService.save(constructors);

            System.out.println("[DONE]");

        } else {

            System.out.println("No new constructors to upload.");
        }
    }

    private void loadCircuits(final List<Circuit> circuits, final Connection connection) throws SQLException {

        if (!circuits.isEmpty()) {

            System.out.print("Loading " + circuits.size() + " circuits... ");

            circuitHiveDao.save(connection, circuits);

            circuits.forEach(circuit -> circuit.setUploaded(Boolean.TRUE));

            circuitService.save(circuits);

            System.out.println("[DONE]");

        } else {

            System.out.println("No new circuits to upload.");
        }

    }

    private void loadRaces(final Connection connection) throws SQLException {

        Pageable pageable = PageRequest.of(0, BATCH_SIZE, Sort.by("id"));

        Page<Race> page = raceService.findPage(pageable);

        if (!page.hasContent()) {

            System.out.println("No new races to upload.");
        }

        while (page.hasContent()) {

            final List<Race> races = page.getContent();

            System.out.print("Loading " + races.size() + " races... ");

            Map<Integer, List<Race>> groupedRaces = races.stream()
                    .collect(Collectors.groupingBy(Race::getSeason));

            for (var entry : groupedRaces.entrySet()) {

                loadPartitionedRaces(connection, entry.getKey(), entry.getValue());
            }

            System.out.println("[DONE]");

            page = raceService.findPage(pageable);
        }
    }

    private void loadPartitionedRaces(final Connection connection, final int partition, final List<Race> races) throws SQLException {

        final List<RaceResult> raceResultsToLoad = races.stream()
                .flatMap(race -> race.getResults().stream())
                .collect(Collectors.toList());

        final List<PitStop> pitStopsToLoad = races.stream()
                .flatMap(race -> race.getPitStops().stream())
                .collect(Collectors.toList());

        racePartitionedHiveDao.save(connection, races, partition);

        if (!raceResultsToLoad.isEmpty()) {

            raceResultPartitionedHiveDao.save(connection, raceResultsToLoad, partition);
            raceResultService.deleteAll(raceResultsToLoad);
        }

        if (!pitStopsToLoad.isEmpty()) {

            pitStopPartitionedHiveDao.save(connection, pitStopsToLoad, partition);
            pitStopService.deleteAll(pitStopsToLoad);
        }

        raceService.deleteAll(races);
    }

    private Connection createConnection() throws SQLException {

        return DriverManager.getConnection(hiveConnectionUrl, hiveUsername, hivePassword);
    }

    private String emptyOnNull(final String value) {

        return Optional.ofNullable(value)
                .orElse("");
    }
}
