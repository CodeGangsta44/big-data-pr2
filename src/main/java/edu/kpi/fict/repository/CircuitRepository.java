package edu.kpi.fict.repository;

import edu.kpi.fict.model.Circuit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CircuitRepository extends JpaRepository<Circuit, Long> {

    Circuit findByCircuitId(final String circuitId);

    List<Circuit> findAllByUploadedFalse();
}
