package edu.kpi.fict.service.persistence.impl;

import edu.kpi.fict.model.Constructor;
import edu.kpi.fict.repository.ConstructorRepository;
import edu.kpi.fict.service.persistence.ConstructorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultConstructorService implements ConstructorService {

    private final ConstructorRepository constructorRepository;

    public DefaultConstructorService(final ConstructorRepository constructorRepository) {

        this.constructorRepository = constructorRepository;
    }

    @Override
    public void save(final Constructor constructor) {

        constructorRepository.save(constructor);
    }

    @Override
    public void save(final List<Constructor> constructors) {

        constructorRepository.saveAll(constructors);
    }

    @Override
    public Optional<Constructor> getById(final String constructorId) {

        return Optional.ofNullable(constructorRepository.findByConstructorId(constructorId));
    }

    @Override
    public List<Constructor> getNotLoaded() {

        return constructorRepository.findAllByUploadedFalse();
    }
}
