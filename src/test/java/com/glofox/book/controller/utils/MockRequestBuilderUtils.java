package com.glofox.book.controller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class MockRequestBuilderUtils {

    private final ObjectMapper mapper;

    public MockHttpServletRequestBuilder get(Map<String, Object> params,
                                             String url)  {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);

        params.forEach((nameParam, valueParam) -> request.param(nameParam, valueParam.toString()));

        return request;
    }

    public MockHttpServletRequestBuilder get(String url)  {
        return MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder delete(String urlTemplate) {
        return MockMvcRequestBuilders.delete(urlTemplate);
    }

    public MockHttpServletRequestBuilder delete(Map<String, Object> params, String urlTemplate) {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON);

        params.forEach((nameParam, valueParam) -> request.param(nameParam, valueParam.toString()));

        return request;
    }

    public MockHttpServletRequestBuilder post(Object object, String urlTemplate)
            throws JsonProcessingException {

        return MockMvcRequestBuilders.post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(object));
    }

}