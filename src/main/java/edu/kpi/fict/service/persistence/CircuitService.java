package edu.kpi.fict.service.persistence;

import edu.kpi.fict.model.Circuit;

import java.util.List;
import java.util.Optional;

public interface CircuitService {

    void save(final Circuit circuit);

    void save(final List<Circuit> circuits);

    Optional<Circuit> getById(final String circuitId);

    List<Circuit> getNotLoaded();
}
