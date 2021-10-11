package edu.kpi.fict.service.persistence.impl;

import edu.kpi.fict.model.Race;
import edu.kpi.fict.repository.RaceRepository;
import edu.kpi.fict.service.persistence.RaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultRaceService implements RaceService {

    private final RaceRepository raceRepository;

    public DefaultRaceService(final RaceRepository raceRepository) {

        this.raceRepository = raceRepository;
    }

    @Override
    public void save(final Race race) {

        raceRepository.save(race);
    }

    @Override
    public void save(final List<Race> races) {

        raceRepository.saveAll(races);
    }

    @Override
    public Optional<Race> getBySeasonAndRound(final String season, final String round) {

        return Optional.ofNullable(raceRepository.findBySeasonAndRound(Integer.parseInt(season), Integer.parseInt(round)));
    }

    @Override
    public Optional<Integer> findLastLoadedSeason() {

        return raceRepository.findLastLoadedSeason();
    }

    @Override
    public Optional<Integer> findLastLoadedRound(final int season) {

        return raceRepository.findLastLoadedRound(season);
    }

    @Override
    public Page<Race> findPage(final Pageable pageable) {

        return raceRepository.findAll(pageable);
    }

    @Override
    public void deleteAll(final List<Race> races) {

        raceRepository.deleteAllInBatch(races);
    }
}
