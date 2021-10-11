package edu.kpi.fict.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity(name = "races")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int season;

    @Column
    private int round;

    @ManyToOne
    private Circuit circuit;

    @OneToMany(mappedBy = "race",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<RaceResult> results;

    @OneToMany(mappedBy = "race",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<PitStop> pitStops;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }

    public List<RaceResult> getResults() {
        return results;
    }

    public void setResults(List<RaceResult> results) {
        this.results = results;
    }

    public List<PitStop> getPitStops() {
        return pitStops;
    }

    public void setPitStops(final List<PitStop> pitStops) {
        this.pitStops = pitStops;
    }
}
