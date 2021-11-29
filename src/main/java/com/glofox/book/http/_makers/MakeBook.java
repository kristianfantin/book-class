package com.glofox.book.http._makers;

import com.glofox.book.domain.model.BookEntity;
import com.glofox.book.domain.model.ClassEntity;
import com.glofox.book.http.dto.BookingResponseDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MakeBook {

    public static BookingResponseDTO toResponse(BookEntity entity, ClassEntity classEntity) {
        return BookingResponseDTO.builder()
                .id(entity.getId())
                .className(classEntity.getClassName())
                .bookingDate(entity.getBookingDate())
                .memberName(entity.getMemberName())
                .build();
    }

}
