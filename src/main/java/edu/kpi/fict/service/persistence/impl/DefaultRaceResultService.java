package edu.kpi.fict.service.persistence.impl;

import edu.kpi.fict.model.RaceResult;
import edu.kpi.fict.repository.RaceResultRepository;
import edu.kpi.fict.service.persistence.RaceResultService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultRaceResultService implements RaceResultService {

    private final RaceResultRepository raceResultRepository;

    public DefaultRaceResultService(final RaceResultRepository raceResultRepository) {

        this.raceResultRepository = raceResultRepository;
    }

    @Override
    public void save(final RaceResult raceResult) {

        raceResultRepository.save(raceResult);
    }

    @Override
    public void save(final List<RaceResult> raceResults) {

        raceResultRepository.saveAll(raceResults);
    }

    @Override
    public void deleteAll(final List<RaceResult> raceResults) {

        raceResultRepository.deleteAllInBatch(raceResults);
    }
}
