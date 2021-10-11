package edu.kpi.fict.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "pitstops")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PitStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stop;
    private String lap;
    private String duration;

    @ManyToOne
    private Driver driver;

    @ManyToOne
    private Race race;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(final String stop) {
        this.stop = stop;
    }

    public String getLap() {
        return lap;
    }

    public void setLap(final String lap) {
        this.lap = lap;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(final String duration) {
        this.duration = duration;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(final Driver driver) {
        this.driver = driver;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(final Race race) {
        this.race = race;
    }
}
