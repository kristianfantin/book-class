package com.glofox.book.http._makers;

import com.glofox.book.domain.model.BookEntity;
import com.glofox.book.domain.model.ClassEntity;
import com.glofox.book.http.dto.BookingClassResponseDTO;
import com.glofox.book.http.dto.BookingDTO;
import com.glofox.book.http.dto.BookingResponseDTO;
import com.glofox.book.http.dto.ClassResponseDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MakeClass {

    public static ClassResponseDTO toResponse(ClassEntity entity, List<BookEntity> book) {
        return ClassResponseDTO.builder()
                .id(entity.getId())
                .className(entity.getClassName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .capacity(entity.getCapacity())
                .totalNumberOfBookings((long) book.size())
                .bookingClass(getBookingClass(entity, book))
                .build();
    }

    public static ClassResponseDTO toResponse(ClassResponseDTO dto, LocalDate date) {
        final List<BookingClassResponseDTO> collect = getCollect(dto, date);

        return ClassResponseDTO.builder()
                .id(dto.getId())
                .className(dto.getClassName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .capacity(dto.getCapacity())
                .totalNumberOfBookings(Long.valueOf(getValue(collect)))
                .bookingClass(collect)
                .build();
    }

    private static List<BookingClassResponseDTO> getBookingClass(ClassEntity classEntity, List<BookEntity> bookList) {
        final List<BookingClassResponseDTO> collect = new ArrayList<>();
        getBookings(classEntity, bookList).forEach((key, value) -> collect.add(buildBookingClass(key, value)));
        collect.sort(MakeClass::compareDate);
        return collect;
    }

    private static BookingClassResponseDTO buildBookingClass(LocalDate key, List<BookingResponseDTO> value) {
        return BookingClassResponseDTO.builder()
                .date(key)
                .numberOfBookings((long) value.size())
                .bookings(value)
                .build();
    }

    private static Map<LocalDate, List<BookingResponseDTO>> getBookings(ClassEntity classEntity,
                                                                        List<BookEntity> bookList) {
        return bookList.stream()
                .map(item -> MakeBook.toResponse(item, classEntity))
                .collect(Collectors.groupingBy(BookingDTO::getBookingDate));
    }

    private static int compareDate(BookingClassResponseDTO o1, BookingClassResponseDTO o2) {
        return o1.getDate().compareTo(o2.getDate());
    }

    private static List<BookingClassResponseDTO> getCollect(ClassResponseDTO dto, LocalDate date) {
        return dto.getBookingClass().stream()
                .filter(bookingClass -> bookingClass.getDate().equals(date))
                .collect(Collectors.toList());
    }

    private static Integer getValue(List<BookingClassResponseDTO> collect) {
        return collect.stream()
                .map(a -> a.getBookings().size())
                .collect(Collectors.toList())
                .stream()
                .reduce(0, Integer::sum);
    }
}
