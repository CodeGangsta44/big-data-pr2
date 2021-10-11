package edu.kpi.fict.service.persistence;

import edu.kpi.fict.model.Configuration;

import java.util.Optional;

public interface ConfigurationService {

    void save(final Configuration configuration);

    Optional<Configuration> getConfiguration();
}
