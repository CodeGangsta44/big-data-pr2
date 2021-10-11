package edu.kpi.fict.dao.impl;

import edu.kpi.fict.dao.PartitionedHiveDao;
import edu.kpi.fict.model.PitStop;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static edu.kpi.fict.util.StringUtils.emptyIfNull;

@Component
public class PitStopHiveDao extends PartitionedHiveDao<PitStop> {

    private static final String SAVE_QUERY = "INSERT INTO pit_stops PARTITION(year={year}) (race_id, driver_id, lap, stop, duration) VALUES ";

    private static final String VALUE_TEMPLATE = "(\"{race_id}\", \"{driver_id}\", {lap}, {stop}, {duration})";

    private static final String YEAR_PARTITION_PLACEHOLDER = "{year}";

    private static final String RACE_ID_PLACEHOLDER = "{race_id}";
    private static final String DRIVER_ID_PLACEHOLDER = "{driver_id}";
    private static final String LAP_PLACEHOLDER = "{lap}";
    private static final String STOP_PLACEHOLDER = "{stop}";
    private static final String DURATION_PLACEHOLDER = "{duration}";

    @Override
    protected String getSaveQuery(final int partition, final List<PitStop> models) {

        return SAVE_QUERY.replace(YEAR_PARTITION_PLACEHOLDER, String.valueOf(partition)) + getValuesString(models);
    }

    private String getValuesString(final List<PitStop> models) {

        return models.stream()
                .map(this::getValueString)
                .collect(Collectors.joining(","));
    }

    private String getValueString(final PitStop model) {

        return VALUE_TEMPLATE
                .replace(RACE_ID_PLACEHOLDER, model.getRace().getSeason() + "_" + model.getRace().getRound())
                .replace(DRIVER_ID_PLACEHOLDER, emptyIfNull(model.getDriver().getDriverId()))
                .replace(LAP_PLACEHOLDER, model.getLap())
                .replace(STOP_PLACEHOLDER, model.getStop())
                .replace(DURATION_PLACEHOLDER, getDuration(model));
    }

    private String getDuration(final PitStop model) {

        String result = "0";

        try {

            result = String.valueOf(Double.valueOf(Double.parseDouble(model.getDuration()) * 1000).longValue());

        } catch (Exception e) {

            // ignore
        }

        return result;
    }
}
