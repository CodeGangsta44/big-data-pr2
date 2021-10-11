package edu.kpi.fict.service.persistence.impl;

import edu.kpi.fict.model.Driver;
import edu.kpi.fict.repository.DriverRepository;
import edu.kpi.fict.service.persistence.DriverService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultDriverService implements DriverService {

    private final DriverRepository driverRepository;

    public DefaultDriverService(final DriverRepository driverRepository) {

        this.driverRepository = driverRepository;
    }

    @Override
    public void save(final Driver driver) {

        driverRepository.save(driver);
    }

    @Override
    public void save(final List<Driver> drivers) {

        driverRepository.saveAll(drivers);
    }

    @Override
    public Optional<Driver> getById(final String driverId) {

        return Optional.ofNullable(driverRepository.findByDriverId(driverId));
    }

    @Override
    public List<Driver> getNotLoaded() {

        return driverRepository.findAllByUploadedFalse();
    }
}
