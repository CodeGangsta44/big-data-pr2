package edu.kpi.fict.repository;

import edu.kpi.fict.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

}
