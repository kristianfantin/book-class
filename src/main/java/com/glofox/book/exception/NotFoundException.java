package com.glofox.book.exception;


public class NotFoundException extends RuntimeException {

    public NotFoundException(final Long id) {
        super("No record found for id " + id);
    }

}
