package edu.kpi.fict.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "circuits")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Circuit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String circuitId;

    @Column
    private String circuitName;

    @Column
    private boolean uploaded;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(String circuitId) {
        this.circuitId = circuitId;
    }

    public String getCircuitName() {
        return circuitName;
    }

    public void setCircuitName(String circuitName) {
        this.circuitName = circuitName;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(final boolean uploaded) {
        this.uploaded = uploaded;
    }
}
