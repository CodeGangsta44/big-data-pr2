package edu.kpi.fict.dao.impl;

import edu.kpi.fict.dao.HiveDao;
import edu.kpi.fict.model.Constructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static edu.kpi.fict.util.StringUtils.emptyIfNull;

@Component
public class ConstructorHiveDao extends HiveDao<Constructor> {

    private static final String SAVE_QUERY = "INSERT INTO TABLE constructors(constructor_id, name, nationality) VALUES ";

    private static final String VALUE_TEMPLATE = "(\"{constructor_id}\", \"{name}\", \"{nationality}\")";

    private static final String CONSTRUCTOR_ID_TEMPLATE = "{constructor_id}";
    private static final String NAME_PLACEHOLDER = "{name}";
    private static final String NATIONALITY_PLACEHOLDER = "{nationality}";

    @Override
    protected String getSaveQuery(final List<Constructor> models) {

        return SAVE_QUERY + getValuesString(models);
    }

    private String getValuesString(final List<Constructor> models) {

        return models.stream()
                .map(this::getTuple)
                .collect(Collectors.joining(","));
    }

    private String getTuple(final Constructor model) {

        return VALUE_TEMPLATE
                .replace(CONSTRUCTOR_ID_TEMPLATE, emptyIfNull(model.getConstructorId()))
                .replace(NAME_PLACEHOLDER, emptyIfNull(model.getName()))
                .replace(NATIONALITY_PLACEHOLDER, emptyIfNull(model.getNationality()));
    }
}
