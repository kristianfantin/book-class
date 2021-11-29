package com.glofox.book.domain.service;

import com.glofox.book.domain.model.BookEntity;
import com.glofox.book.domain.model.ClassEntity;
import com.glofox.book.domain.repository.BookRepository;
import com.glofox.book.http.dto.BookingDTO;
import com.glofox.book.utils.ServiceExceptionBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.glofox.book.utils.StringUtils.removeAccents;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository repository;
    private final ClassService classService;

    @Transactional
    public BookEntity save(BookingDTO dto) {
        ClassEntity classEntity = classService.findByNameAndBetweenStartDateAndEndDate(dto.getClassName(), dto.getBookingDate());
        return repository.save(build(dto, classEntity));
    }

    public List<BookEntity> findByDate(LocalDate localDate) {
        return repository.findByBookingDate(localDate);
    }

    private BookEntity build(BookingDTO dto, ClassEntity classEntity) {
        return BookEntity.builder()
                .classId(classEntity.getId())
                .bookingDate(dto.getBookingDate())
                .memberName(dto.getMemberName())
                .auxMemberName(removeAccents(dto.getMemberName()).toUpperCase())
                .build();
    }

    public List<BookEntity> findByClass(ClassEntity classEntity) {
        return repository.findByClassId(classEntity.getId());
    }

    public void delete(Long id) {
        final BookEntity bookEntity = repository.findById(id)
                .orElseThrow(() -> ServiceExceptionBuilder.throwNotFoundException(id));

        repository.delete(bookEntity);
    }
}
