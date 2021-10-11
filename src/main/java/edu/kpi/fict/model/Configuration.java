package edu.kpi.fict.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "configuration")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int lastDownloadedSeason;

    @Column
    private int lastDownloadedRound;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getLastDownloadedSeason() {
        return lastDownloadedSeason;
    }

    public void setLastDownloadedSeason(final int lastDownloadedSeason) {
        this.lastDownloadedSeason = lastDownloadedSeason;
    }

    public int getLastDownloadedRound() {
        return lastDownloadedRound;
    }

    public void setLastDownloadedRound(final int lastDownloadedRound) {
        this.lastDownloadedRound = lastDownloadedRound;
    }
}
