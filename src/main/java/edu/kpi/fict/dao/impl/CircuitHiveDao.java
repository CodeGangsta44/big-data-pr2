package edu.kpi.fict.dao.impl;

import edu.kpi.fict.dao.HiveDao;
import edu.kpi.fict.model.Circuit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static edu.kpi.fict.util.StringUtils.emptyIfNull;

@Component
public class CircuitHiveDao extends HiveDao<Circuit> {

    private static final String SAVE_QUERY = "INSERT INTO TABLE circuits(circuit_id, name) VALUES ";

    private static final String VALUE_TEMPLATE = "(\"{circuit_id}\", \"{name}\")";

    private static final String CIRCUIT_ID_TEMPLATE = "{driver_id}";
    private static final String NAME_PLACEHOLDER = "{name}";

    @Override
    protected String getSaveQuery(final List<Circuit> models) {

        return SAVE_QUERY + getValuesString(models);
    }

    private String getValuesString(final List<Circuit> models) {

        return models.stream()
                .map(this::getTuple)
                .collect(Collectors.joining(","));
    }

    private String getTuple(final Circuit model) {

        return VALUE_TEMPLATE
                .replace(CIRCUIT_ID_TEMPLATE, emptyIfNull(model.getCircuitId()))
                .replace(NAME_PLACEHOLDER, emptyIfNull(model.getCircuitName()));
    }
}
