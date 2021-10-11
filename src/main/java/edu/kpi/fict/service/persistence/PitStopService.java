package edu.kpi.fict.service.persistence;

import edu.kpi.fict.model.PitStop;

import java.util.List;

public interface PitStopService {

    void save(final PitStop pitStop);

    void save(final List<PitStop> pitStops);

    void deleteAll(final List<PitStop> pitStops);
}
