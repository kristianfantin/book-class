package com.glofox.book.utils;

import com.glofox.book.exception.NotFoundException;
import com.glofox.book.exception.ServiceException;

import java.security.InvalidParameterException;
import java.time.LocalDate;

public class ServiceExceptionBuilder {

    private ServiceExceptionBuilder() {}

    public static ServiceException getServiceException(final Throwable cause) {
        return new ServiceException(cause);
    }

    public static ServiceException throwNotFoundException(final Long id) {
        return getServiceException(new NotFoundException(id));
    }

    public static ServiceException throwNotFoundException(final String className, final LocalDate localDate) {
        return getServiceException(new NotFoundException(className, localDate));
    }

    public static ServiceException throwInvalidParameterException(final String s) {
        return getServiceException(new InvalidParameterException(s));
    }

}
