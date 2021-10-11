package edu.kpi.fict.service.integration;

import edu.kpi.fict.model.Race;

import java.util.Optional;

public interface RoundIntegrationService {

    Optional<Race> getRace(final String year, final String round);
}
