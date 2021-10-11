package edu.kpi.fict.dao.impl;

import edu.kpi.fict.dao.PartitionedHiveDao;
import edu.kpi.fict.model.RaceResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RaceResultHiveDao extends PartitionedHiveDao<RaceResult> {

    private static final String SAVE_QUERY = "INSERT INTO race_results PARTITION(year={year}) (race_id, constructor_id, driver_id, circuit_id, position, points) VALUES ";

    private static final String VALUE_TEMPLATE = "(\"{race_id}\", \"{constructor_id}\", \"{driver_id}\", \"{circuit_id}\", {position}, {points})";

    private static final String YEAR_PARTITION_PLACEHOLDER = "{year}";

    private static final String RACE_ID_PLACEHOLDER = "{race_id}";
    private static final String CONSTRUCTOR_ID_PLACEHOLDER = "{constructor_id}";
    private static final String DRIVER_ID_PLACEHOLDER = "{driver_id}";
    private static final String CIRCUIT_ID_PLACEHOLDER = "{circuit_id}";
    private static final String POSITION_PLACEHOLDER = "{position}";
    private static final String POINTS_PLACEHOLDER = "{points}";


    @Override
    protected String getSaveQuery(final int partition, final List<RaceResult> models) {

        return SAVE_QUERY.replace(YEAR_PARTITION_PLACEHOLDER, String.valueOf(partition)) + getValuesString(models);
    }

    private String getValuesString(final List<RaceResult> models) {

        return models.stream()
                .map(this::getValueString)
                .collect(Collectors.joining(","));
    }

    private String getValueString(final RaceResult model) {

        return VALUE_TEMPLATE
                .replace(RACE_ID_PLACEHOLDER, model.getRace().getSeason() + "_" + model.getRace().getRound())
                .replace(CONSTRUCTOR_ID_PLACEHOLDER, model.getConstructor().getConstructorId())
                .replace(DRIVER_ID_PLACEHOLDER, model.getDriver().getDriverId())
                .replace(CIRCUIT_ID_PLACEHOLDER, model.getRace().getCircuit().getCircuitId())
                .replace(POSITION_PLACEHOLDER, String.valueOf(model.getPosition()))
                .replace(POINTS_PLACEHOLDER, String.valueOf(model.getPoints()));
    }
}
