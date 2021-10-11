package edu.kpi.fict.service.persistence;

import edu.kpi.fict.model.RaceResult;

import java.util.List;

public interface RaceResultService {

    void save(final RaceResult raceResult);

    void save(final List<RaceResult> raceResults);

    void deleteAll(final List<RaceResult> raceResults);
}
