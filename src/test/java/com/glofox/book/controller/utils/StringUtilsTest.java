package com.glofox.book.controller.utils;

import org.junit.jupiter.api.Test;

import static com.glofox.book.utils.StringUtils.removeAccents;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

    @Test
    void shouldRemoveAccents() {
        assertEquals("Fiancee", removeAccents("Fiancée"));
        assertEquals("Fiance", removeAccents("Fiancé"));
        assertEquals("Facade", removeAccents("Façade"));
    }

    @Test
    void shouldReturnSameString() {
        String input = "John Smith";
        assertEquals(input, removeAccents(input));
    }

}
