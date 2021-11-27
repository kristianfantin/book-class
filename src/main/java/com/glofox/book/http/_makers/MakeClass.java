package com.glofox.book.http._makers;

import com.glofox.book.domain.model.ClassEntity;
import com.glofox.book.http.dto.ClassResponseDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MakeClass {

    public static ClassResponseDTO toResponse(ClassEntity entity) {
        return ClassResponseDTO.builder()
                .id(entity.getId())
                .className(entity.getClassName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .capacity(entity.getCapacity())
                .build();
    }

}
