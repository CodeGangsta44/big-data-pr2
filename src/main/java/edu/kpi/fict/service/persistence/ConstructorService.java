package edu.kpi.fict.service.persistence;

import edu.kpi.fict.model.Constructor;

import java.util.List;
import java.util.Optional;

public interface ConstructorService {

    void save(final Constructor constructor);

    void save(final List<Constructor> constructors);

    Optional<Constructor> getById(final String constructorId);

    List<Constructor> getNotLoaded();
}
