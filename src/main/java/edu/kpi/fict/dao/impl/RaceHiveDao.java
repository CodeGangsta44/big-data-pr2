package edu.kpi.fict.dao.impl;

import edu.kpi.fict.dao.PartitionedHiveDao;
import edu.kpi.fict.model.Driver;
import edu.kpi.fict.model.Race;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static edu.kpi.fict.util.StringUtils.emptyIfNull;

@Component
public class RaceHiveDao extends PartitionedHiveDao<Race> {

    private static final String SAVE_QUERY = "INSERT INTO races PARTITION(year={year}) (race_id, round, circuit_id) VALUES ";

    private static final String VALUE_TEMPLATE = "(\"{race_id}\", \"{round}\", \"{circuit_id}\")";

    private static final String YEAR_PARTITION_PLACEHOLDER = "{year}";

    private static final String RACE_ID_PLACEHOLDER = "{race_id}";
    private static final String ROUND_PLACEHOLDER = "{round}";
    private static final String CIRCUIT_ID_PLACEHOLDER = "{circuit_id}";

    @Override
    protected String getSaveQuery(final int partition, final List<Race> models) {

        return SAVE_QUERY.replace(YEAR_PARTITION_PLACEHOLDER, String.valueOf(partition)) + getValuesString(models);
    }

    private String getValuesString(final List<Race> models) {

        return models.stream()
                .map(this::getValueString)
                .collect(Collectors.joining(","));
    }

    private String getValueString(final Race model) {

        return VALUE_TEMPLATE
                .replace(RACE_ID_PLACEHOLDER, model.getSeason() + "_" + model.getRound())
                .replace(ROUND_PLACEHOLDER, String.valueOf(model.getRound()))
                .replace(CIRCUIT_ID_PLACEHOLDER, emptyIfNull(model.getCircuit().getCircuitId()));
    }
}
