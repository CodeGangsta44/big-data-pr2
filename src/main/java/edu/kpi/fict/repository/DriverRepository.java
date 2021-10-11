package edu.kpi.fict.repository;

import edu.kpi.fict.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Driver findByDriverId(final String driverId);

    List<Driver> findAllByUploadedFalse();
}
