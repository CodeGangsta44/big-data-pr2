package edu.kpi.fict.repository;

import edu.kpi.fict.model.Constructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConstructorRepository extends JpaRepository<Constructor, Long> {

    Constructor findByConstructorId(final String constructorId);

    List<Constructor> findAllByUploadedFalse();
}
