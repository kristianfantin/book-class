package com.glofox.book.domain.repository;

import com.glofox.book.domain.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    List<BookEntity> findByBookingDate(LocalDate localDate);

    List<BookEntity> findByClassId(Long classId);
}
