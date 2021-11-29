package com.glofox.book.domain.repository;

import com.glofox.book.domain.model.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {

    List<ClassEntity> findByAuxClassName(String className);

    @Query(value = "SELECT *" +
                   "  FROM CLASS_ENTITY" +
                   " WHERE ?1 BETWEEN START_DATE AND END_DATE",
            nativeQuery = true)
    List<ClassEntity> findAllBetweenStartDateAndEndDate(LocalDate date);
}
