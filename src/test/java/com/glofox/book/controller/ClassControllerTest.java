package com.glofox.book.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glofox.book.config.FunctionalTest;
import com.glofox.book.controller.utils.ContentResultActions;
import com.glofox.book.controller.utils.MockRequestBuilderUtils;
import com.glofox.book.http.dto.ClassDTO;
import com.glofox.book.http.dto.ClassResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@FunctionalTest
class ClassControllerTest {

    private static final String URL_TEMPLATE = "/classes";
    private static final int EXPECTED_QTY_CLASS = 1;

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    private final ContentResultActions content;
    private final MockRequestBuilderUtils mockRequest;

    @Autowired
    ClassControllerTest(MockMvc mockMvc,
                        ObjectMapper mapper,
                        ContentResultActions content,
                        MockRequestBuilderUtils mockRequest) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
        this.content = content;
        this.mockRequest = mockRequest;
    }

    @Test
    void shouldCreateAClass() throws Exception {
        final ClassDTO build = build(
                "pilates",
                LocalDate.of(2021, 12, 1),
                LocalDate.of(2021, 12, 20),
                10);

        final MockHttpServletRequestBuilder request = mockRequest.post(build, URL_TEMPLATE);
        final ResultActions resultActions = mockMvc.perform(request);

        assertEquals(HttpStatus.CREATED.value(), resultActions.andReturn().getResponse().getStatus());

        final ClassResponseDTO responseDTO = mapper.readValue(content.getContentAsString(resultActions), ClassResponseDTO.class);
        assertEquals(build.getClassName(), responseDTO.getClassName());
        assertEquals(build.getStartDate(), responseDTO.getStartDate());
        assertEquals(build.getEndDate(), responseDTO.getEndDate());
        assertEquals(build.getCapacity(), responseDTO.getCapacity());
        assertTrue(responseDTO.getId() > 0);
    }

    @Test
    void shouldFindAClass() throws Exception {
        final ClassDTO build = build(
                "pilates",
                LocalDate.of(2021, 12, 1),
                LocalDate.of(2021, 12, 20),
                10);

        final MockHttpServletRequestBuilder request = mockRequest.post(build, URL_TEMPLATE);
        final ResultActions resultActions = mockMvc.perform(request);

        assertEquals(HttpStatus.CREATED.value(), resultActions.andReturn().getResponse().getStatus());

        final ClassResponseDTO responseDTO = mapper.readValue(content.getContentAsString(resultActions), ClassResponseDTO.class);

        Map<String, Object> params = new HashMap<>();
        params.put("id", responseDTO.getId());

        final MockHttpServletRequestBuilder getClass = mockRequest.get(params, URL_TEMPLATE + "/id");
        final ResultActions perform = mockMvc.perform(getClass);
        assertEquals(HttpStatus.OK.value(), perform.andReturn().getResponse().getStatus());

        final MockHttpServletRequestBuilder getAllClass = mockRequest.get(URL_TEMPLATE);
        final ResultActions performAll = mockMvc.perform(getAllClass);
        assertEquals(HttpStatus.OK.value(), performAll.andReturn().getResponse().getStatus());
        List<ClassResponseDTO> list = mapper.readValue(content.getContentAsString(performAll),
                new TypeReference<>() { });
        assertEquals(EXPECTED_QTY_CLASS, list.size());
    }

    @Test
    void shouldNotFindAClass() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 99);

        final MockHttpServletRequestBuilder getClass = mockRequest.get(params, URL_TEMPLATE + "/id");
        final ResultActions perform = mockMvc.perform(getClass);
        assertEquals(HttpStatus.NOT_FOUND.value(), perform.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnAnInvalidParameterException() throws Exception {
        final ClassDTO build = build(
                "pilates",
                LocalDate.of(2021, 12, 15),
                LocalDate.of(2021, 12, 14),
                10);

        final MockHttpServletRequestBuilder request = mockRequest.post(build, URL_TEMPLATE);
        final ResultActions resultActions = mockMvc.perform(request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    private ClassDTO build(String className, LocalDate startDate, LocalDate endDate, int capacity) {
        return ClassDTO.builder()
                .className(className)
                .startDate(startDate)
                .endDate(endDate)
                .capacity(capacity)
                .build();
    }

}
