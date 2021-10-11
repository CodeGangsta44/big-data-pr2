package edu.kpi.fict.service.persistence;

import edu.kpi.fict.model.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RaceService {

    void save(final Race race);

    void save(final List<Race> races);

    Optional<Race> getBySeasonAndRound(final String season, final String round);

    Optional<Integer> findLastLoadedSeason();

    Optional<Integer> findLastLoadedRound(final int season);

    Page<Race> findPage(final Pageable pageable);

    void deleteAll(final List<Race> races);
}
