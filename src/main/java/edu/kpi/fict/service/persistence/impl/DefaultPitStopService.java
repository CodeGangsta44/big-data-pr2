package edu.kpi.fict.service.persistence.impl;

import edu.kpi.fict.model.PitStop;
import edu.kpi.fict.repository.PitStopRepository;
import edu.kpi.fict.service.persistence.PitStopService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultPitStopService implements PitStopService {

    private final PitStopRepository pitStopRepository;

    public DefaultPitStopService(final PitStopRepository pitStopRepository) {

        this.pitStopRepository = pitStopRepository;
    }

    @Override
    public void save(final PitStop pitStop) {

        pitStopRepository.save(pitStop);
    }

    @Override
    public void save(final List<PitStop> pitStops) {

        pitStopRepository.saveAll(pitStops);
    }

    @Override
    public void deleteAll(final List<PitStop> pitStops) {

        pitStopRepository.deleteAllInBatch(pitStops);
    }
}
