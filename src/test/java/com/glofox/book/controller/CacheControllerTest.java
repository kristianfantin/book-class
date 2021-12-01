package com.glofox.book.controller;

import com.glofox.book.config.AbstractIntegrationTest;
import com.glofox.book.controller.utils.MockRequestBuilderUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CacheControllerTest extends AbstractIntegrationTest {

    private static final String URL_TEMPLATE = "/api/cache";

    private final MockMvc mockMvc;
    private final MockRequestBuilderUtils requestBuilderUtils;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    CacheControllerTest(MockMvc mockMvc,
                        MockRequestBuilderUtils requestBuilderUtils) {
        this.mockMvc = mockMvc;
        this.requestBuilderUtils = requestBuilderUtils;
    }

    @Test
    void shouldDeleteKeysFromCache() throws Exception {
        final MockHttpServletRequestBuilder request = requestBuilderUtils.delete(URL_TEMPLATE);
        final ResultActions resultActions = mockMvc.perform(request);

        assertEquals(HttpStatus.NO_CONTENT.value(), resultActions.andReturn().getResponse().getStatus());
        final String contentAsString = getContentAsString(resultActions);
        assertNotNull(contentAsString);
        assertEquals("", contentAsString);
    }

    private String getContentAsString(ResultActions resultActions) throws UnsupportedEncodingException {
        return resultActions.andReturn().getResponse().getContentAsString();
    }

}
