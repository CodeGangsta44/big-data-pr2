package edu.kpi.fict.service.persistence.impl;

import edu.kpi.fict.model.Configuration;
import edu.kpi.fict.repository.ConfigurationRepository;
import edu.kpi.fict.service.persistence.ConfigurationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultConfigurationService implements ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    public DefaultConfigurationService(final ConfigurationRepository configurationRepository) {

        this.configurationRepository = configurationRepository;
    }

    @Override
    public void save(final Configuration configuration) {

        configurationRepository.save(configuration);
    }

    @Override
    public Optional<Configuration> getConfiguration() {

        return configurationRepository.findAll()
                .stream()
                .findFirst();
    }
}
