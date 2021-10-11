package edu.kpi.fict.repository;

import edu.kpi.fict.model.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RaceRepository extends JpaRepository<Race, Long> {

    Race findBySeasonAndRound(final int season, final int round);

    @Query(value = "SELECT max(season) FROM races")
    Optional<Integer> findLastLoadedSeason();

    @Query(value = "SELECT max(round) FROM races WHERE season = :seasonParameter")
    Optional<Integer> findLastLoadedRound(@Param("seasonParameter") final int season);
}
