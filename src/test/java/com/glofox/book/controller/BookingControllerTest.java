package com.glofox.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glofox.book.config.FunctionalTest;
import com.glofox.book.controller.utils.ContentResultActions;
import com.glofox.book.controller.utils.MockRequestBuilderUtils;
import com.glofox.book.http.dto.BookingDTO;
import com.glofox.book.http.dto.BookingResponseDTO;
import com.glofox.book.http.dto.ClassDTO;
import com.glofox.book.http.dto.ClassResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@FunctionalTest
class BookingControllerTest {

    private static final String URL_TEMPLATE = "/bookings";

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    private final ContentResultActions content;
    private final MockRequestBuilderUtils mockRequest;

    private ClassResponseDTO classDto;

    @Autowired
    BookingControllerTest(MockMvc mockMvc,
                          ObjectMapper mapper,
                          ContentResultActions content,
                          MockRequestBuilderUtils mockRequest) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
        this.content = content;
        this.mockRequest = mockRequest;
    }

    @BeforeEach
    void setup() throws Exception {
        classDto = createClass();
    }

    @Test
    void shouldCreateABook() throws Exception {
        final BookingDTO dto = BookingDTO.builder()
                .className(classDto.getClassName())
                .bookingDate(LocalDate.of(2021, 12, 1))
                .memberName("kristian")
                .build();

        final MockHttpServletRequestBuilder request = mockRequest.post(dto, URL_TEMPLATE);
        final ResultActions resultActions = mockMvc.perform(request);
        assertEquals(HttpStatus.CREATED.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldNotFoundClassToCreateABookBecauseDate() throws Exception {
        final BookingDTO dto = BookingDTO.builder()
                .className(classDto.getClassName())
                .bookingDate(LocalDate.of(2021, 12, 21))
                .memberName("kristian")
                .build();

        final MockHttpServletRequestBuilder request = mockRequest.post(dto, URL_TEMPLATE);
        final ResultActions resultActions = mockMvc.perform(request);

        assertEquals(HttpStatus.NOT_FOUND.value(), resultActions.andReturn().getResponse().getStatus());
        assertTrue(content.getContentAsString(resultActions)
                .contains("No record found for class name kung-fu and date 2021-12-21"));
    }

    @Test
    void shouldNotFoundClassToCreateABookBecauseName() throws Exception {
        final BookingDTO dto = BookingDTO.builder()
                .className("pilates")
                .bookingDate(LocalDate.of(2021, 12, 1))
                .memberName("steffanie")
                .build();

        final MockHttpServletRequestBuilder request = mockRequest.post(dto, URL_TEMPLATE);
        final ResultActions resultActions = mockMvc.perform(request);

        assertEquals(HttpStatus.NOT_FOUND.value(), resultActions.andReturn().getResponse().getStatus());
        assertTrue(content.getContentAsString(resultActions)
                .contains("No record found for class name pilates and date 2021-12-01"));
    }

    @Test
    void shouldCreateABookEvenWithUpperClassName() throws Exception {
        final BookingDTO dto = BookingDTO.builder()
                .className("kUng-FU")
                .bookingDate(LocalDate.of(2021, 12, 1))
                .memberName("kristian")
                .build();

        final MockHttpServletRequestBuilder request = mockRequest.post(dto, URL_TEMPLATE);
        final ResultActions resultActions = mockMvc.perform(request);
        assertEquals(HttpStatus.CREATED.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldDeleteABook() throws Exception {
        final BookingDTO dto = BookingDTO.builder()
                .className(classDto.getClassName())
                .bookingDate(LocalDate.of(2021, 12, 1))
                .memberName("kristian")
                .build();

        final MockHttpServletRequestBuilder request = mockRequest.post(dto, URL_TEMPLATE);
        final ResultActions resultActions = mockMvc.perform(request);
        assertEquals(HttpStatus.CREATED.value(), resultActions.andReturn().getResponse().getStatus());

        final BookingResponseDTO responseDTO = mapper.readValue(
                content.getContentAsString(resultActions), BookingResponseDTO.class);

        Map<String, Object> params = new HashMap<>();
        params.put("id", responseDTO.getId());

        final MockHttpServletRequestBuilder delete = mockRequest.delete(params, URL_TEMPLATE + "/id");
        final ResultActions perform = mockMvc.perform(delete);
        assertEquals(HttpStatus.NO_CONTENT.value(), perform.andReturn().getResponse().getStatus());
    }

    private ClassResponseDTO createClass() throws Exception {
        final ClassDTO build = ClassDTO.builder()
                .className("kung-fu")
                .startDate(LocalDate.of(2021, 12, 1))
                .endDate(LocalDate.of(2021, 12, 20))
                .capacity(10)
                .build();

        final MockHttpServletRequestBuilder request = mockRequest.post(build, "/classes");
        final ResultActions resultActions = mockMvc.perform(request);
        assertEquals(HttpStatus.CREATED.value(), resultActions.andReturn().getResponse().getStatus());
        return mapper.readValue(content.getContentAsString(resultActions), ClassResponseDTO.class);
    }

}
