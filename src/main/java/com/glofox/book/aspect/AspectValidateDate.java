package com.glofox.book.aspect;

import com.glofox.book.http.dto.ClassDTO;
import com.glofox.book.utils.ServiceExceptionBuilder;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Aspect
@Component
public class AspectValidateDate {

    @Before("@annotation(com.glofox.book.aspect.ValidateDate) " +
            "&& args(request, dto)")
    public void validateDate(HttpServletRequest request,
                             ClassDTO dto) {

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw ServiceExceptionBuilder.throwInvalidParameterException("Start Date can't be greater than End Date");
        }

    }


}
