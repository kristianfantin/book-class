package com.glofox.book.domain.service;

import com.glofox.book.domain.model.ClassEntity;
import com.glofox.book.domain.repository.ClassRepository;
import com.glofox.book.http.dto.ClassDTO;
import com.glofox.book.utils.ServiceExceptionBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClassService {

    private final ClassRepository repository;

    @Transactional
    public ClassEntity save(ClassDTO dto) {
        return repository.save(build(dto));
    }

    private ClassEntity build(ClassDTO dto) {
        return ClassEntity.builder()
                .className(dto.getClassName())
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
}
