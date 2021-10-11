package edu.kpi.fict.dao.impl;

import edu.kpi.fict.dao.HiveDao;
import edu.kpi.fict.model.Driver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static edu.kpi.fict.util.StringUtils.emptyIfNull;

@Component
public class DriverHiveDao extends HiveDao<Driver> {

    private static final String SAVE_QUERY = "INSERT INTO TABLE drivers(driver_id, code, first_name, last_name, nationality, permanent_number) VALUES ";

    private static final String VALUE_TEMPLATE = "(\"{driver_id}\", \"{code}\", \"{first_name}\", \"{last_name}\", \"{nationality}\", \"{permanent_number}\")";

    private static final String DRIVER_ID_PLACEHOLDER = "{driver_id}";
    private static final String CODE_PLACEHOLDER = "{code}";
    private static final String FIRST_NAME_PLACEHOLDER = "{first_name}";
    private static final String LAST_NAME_PLACEHOLDER = "{last_name}";
    private static final String NATIONALITY_PLACEHOLDER = "{nationality}";
    private static final String PERMANENT_NUMBER_PLACEHOLDER = "{permanent_number}";

    @Override
    protected String getSaveQuery(final List<Driver> models) {

        return SAVE_QUERY + getValuesString(models);
    }

    private String getValuesString(final List<Driver> models) {

        return models.stream()
                .map(this::getTuple)
                .collect(Collectors.joining(","));
    }

    private String getTuple(final Driver model) {

        return VALUE_TEMPLATE
                .replace(DRIVER_ID_PLACEHOLDER, emptyIfNull(model.getDriverId()))
                .replace(CODE_PLACEHOLDER, emptyIfNull(model.getCode()))
                .replace(FIRST_NAME_PLACEHOLDER, emptyIfNull(model.getFirstName()))
                .replace(LAST_NAME_PLACEHOLDER, emptyIfNull(model.getLastName()))
                .replace(NATIONALITY_PLACEHOLDER, emptyIfNull(model.getNationality()))
                .replace(PERMANENT_NUMBER_PLACEHOLDER, emptyIfNull(model.getPermanentNumber()));
    }
}
