package com.glofox.book.domain.service;

import com.glofox.book.domain.model.ClassEntity;
import com.glofox.book.domain.repository.ClassRepository;
import com.glofox.book.http.dto.ClassDTO;
import com.glofox.book.utils.ServiceExceptionBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.glofox.book.utils.StringUtils.removeAccents;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Service
public class ClassService {

    private final ClassRepository repository;

    @Transactional
    public ClassEntity save(ClassDTO dto) {
        validate(dto);
        return repository.save(build(dto));
    }

    private ClassEntity build(ClassDTO dto) {
        return ClassEntity.builder()
                .className(dto.getClassName())
                .auxClassName(removeAccents(dto.getClassName()).toUpperCase())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .capacity(dto.getCapacity())
                .build();
    }

    public List<ClassEntity> findAll() {
        return repository.findAll();
    }

    public ClassEntity findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> ServiceExceptionBuilder.throwNotFoundException(id)
        );
    }

    public ClassEntity findByNameAndBetweenStartDateAndEndDate(String className, LocalDate bookingDate) {
        return findByName(className)
                .stream()
                .filter(classEntity -> betweenStartAndEndDate(bookingDate, classEntity))
                .findFirst()
                .orElseThrow(() -> ServiceExceptionBuilder.throwNotFoundException(className, bookingDate));
    }

    public List<ClassEntity> findByName(String className) {
        return repository.findByAuxClassName(removeAccents(className).toUpperCase());
    }

    public List<ClassEntity> findByDate(LocalDate date) {
        return repository.findAllBetweenStartDateAndEndDate(date);
    }

    private void validate(ClassDTO dto) {
        final List<ClassEntity> list = findByName(dto.getClassName());
        if (!list.isEmpty()
                && (nonNull(find(dto.getStartDate(), list)) || (nonNull(find(dto.getEndDate(), list))))) {
            throw ServiceExceptionBuilder.throwInvalidParameterException(
                    dto.getClassName() + " already exists in same period");
        }
    }

    private ClassEntity find(LocalDate localDate, List<ClassEntity> list) {
        return list.stream()
                .filter(classEntity -> betweenStartAndEndDate(localDate, classEntity))
                .findFirst()
                .orElse(null);
    }

    private boolean betweenStartAndEndDate(LocalDate localDate, ClassEntity classEntity) {
        return localDate.compareTo(classEntity.getStartDate()) >= 0
                && localDate.compareTo(classEntity.getEndDate()) <= 0;
    }

}
