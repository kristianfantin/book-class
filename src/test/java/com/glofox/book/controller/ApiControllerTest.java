package com.glofox.book.controller;

import com.glofox.book.config.FunctionalTest;
import com.glofox.book.controller.utils.ContentResultActions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

@FunctionalTest
class ApiControllerTest {

    private static final String URL_TEMPLATE = "/api";

    private final MockMvc mockMvc;
    private final ContentResultActions content;

    @Autowired
    ApiControllerTest(MockMvc mockMvc, ContentResultActions content) {
        this.mockMvc = mockMvc;
        this.content = content;
    }

    @Test
    void shouldValidateApiOnLine() throws Exception {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL_TEMPLATE);
        final ResultActions resultActions = mockMvc.perform(request);

        assertEquals("We are online", content.getContentAsString(resultActions));
    }

}
