package edu.kpi.fict.service.persistence.impl;

import edu.kpi.fict.model.Circuit;
import edu.kpi.fict.repository.CircuitRepository;
import edu.kpi.fict.service.persistence.CircuitService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultCircuitService implements CircuitService {

    private final CircuitRepository circuitRepository;

    public DefaultCircuitService(final CircuitRepository circuitRepository) {

        this.circuitRepository = circuitRepository;
    }

    @Override
    public void save(final Circuit circuit) {

        circuitRepository.save(circuit);
    }

    @Override
    public void save(final List<Circuit> circuits) {

        circuitRepository.saveAll(circuits);
    }

    @Override
    public Optional<Circuit> getById(final String circuitId) {

        return Optional.ofNullable(circuitRepository.findByCircuitId(circuitId));
    }

    @Override
    public List<Circuit> getNotLoaded() {

        return circuitRepository.findAllByUploadedFalse();
    }
}
