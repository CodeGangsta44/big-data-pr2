package edu.kpi.fict.service.persistence;

import edu.kpi.fict.model.Driver;

import java.util.List;
import java.util.Optional;

public interface DriverService {

    void save(final Driver driver);

    void save(final List<Driver> drivers);

    Optional<Driver> getById(final String driverId);

    List<Driver> getNotLoaded();
}
