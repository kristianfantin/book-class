package com.glofox.book.exception;


import java.time.LocalDate;

public class NotFoundException extends RuntimeException {

    public NotFoundException(final Long id) {
        super("No record found for id " + id);
    }

    public NotFoundException(final String className, LocalDate localDate) {
        super(String.format("No record found for class name %s and date %s", className, localDate.toString()));
    }

}
