package com.glofox.book.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glofox.book.config.AbstractIntegrationTest;
import com.glofox.book.config.FunctionalTest;
import com.glofox.book.controller.utils.ContentResultActions;
import com.glofox.book.controller.utils.MockRequestBuilderUtils;
import com.glofox.book.http.dto.BookingDTO;
import com.glofox.book.http.dto.ClassDTO;
import com.glofox.book.http.dto.ClassResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@FunctionalTest
class ClassControllerTest extends AbstractIntegrationTest {

    private static final String URL_TEMPLATE = "/classes";
    private static final int EXPECTED_QTY_CLASS = 1;

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    private final ContentResultActions content;
    private final MockRequestBuilderUtils mockRequest;

    @MockBean
    private RestTemplate restTemplate;

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
        final ClassDTO build = ClassDTO.builder()
                .className("taekwondo")
                .startDate(LocalDate.of(2021, 12, 1))
                .endDate(LocalDate.of(2021, 12, 20))
                .capacity(10)
                .build();

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
    void shouldNotCreateAClassWithSameNameInAcrossPeriod() throws Exception {
        shouldCreateAClass();

        final ClassDTO build = ClassDTO.builder()
                .className("taekwondo")
                .startDate(LocalDate.of(2021, 12, 15))
                .endDate(LocalDate.of(2021, 12, 25))
                .capacity(15)
                .build();

        final MockHttpServletRequestBuilder request = mockRequest.post(build, URL_TEMPLATE);
        final ResultActions resultActions = mockMvc.perform(request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
        assertTrue(content.getContentAsString(resultActions).contains("taekwondo already exists in same period"));
    }

    @Test
    void shouldFindAClass() throws Exception {
        final ClassDTO build = ClassDTO.builder()
                .className("pilates")
                .startDate(LocalDate.of(2021, 12, 1))
                .endDate(LocalDate.of(2021, 12, 20))
                .capacity(10)
                .build();

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
        final ClassDTO build = ClassDTO.builder()
                .className("tennis")
                .startDate(LocalDate.of(2021, 12, 15))
                .endDate(LocalDate.of(2021, 12, 14))
                .capacity(10)
                .build();

        final MockHttpServletRequestBuilder request = mockRequest.post(build, URL_TEMPLATE);
        final ResultActions resultActions = mockMvc.perform(request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldFindAClassByName() throws Exception {
        shouldCreateAClass();

        Map<String, Object> params = new HashMap<>();
        params.put("className", "TAEKWONDO");

        final MockHttpServletRequestBuilder getClass = mockRequest.get(params, URL_TEMPLATE + "/class-name");
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
    void shouldNotFindAClassByName() throws Exception {
        shouldCreateAClass();

        Map<String, Object> params = new HashMap<>();
        params.put("className", "karate");

        final MockHttpServletRequestBuilder getClass = mockRequest.get(params, URL_TEMPLATE + "/class-name");
        final ResultActions perform = mockMvc.perform(getClass);
        assertEquals(HttpStatus.OK.value(), perform.andReturn().getResponse().getStatus());
        List<ClassResponseDTO> list = mapper.readValue(content.getContentAsString(perform),
                new TypeReference<>() { });
        assertEquals(0, list.size());
    }

    @Test
    void shouldFindAClassByDate() throws Exception {
        shouldCreateAClass();

        Map<String, Object> params = new HashMap<>();
        params.put("localDate", LocalDate.of(2021,12,5));

        final MockHttpServletRequestBuilder getClass = mockRequest.get(params, URL_TEMPLATE + "/date");
        final ResultActions perform = mockMvc.perform(getClass);
        assertEquals(HttpStatus.OK.value(), perform.andReturn().getResponse().getStatus());

        List<ClassResponseDTO> list = mapper.readValue(content.getContentAsString(perform),
                new TypeReference<>() { });
        assertEquals(EXPECTED_QTY_CLASS, list.size());
    }

    @Test
    void shouldNotFindAClassByDate() throws Exception {
        shouldCreateAClass();

        Map<String, Object> params = new HashMap<>();
        params.put("localDate", LocalDate.of(2021,12,21));

        final MockHttpServletRequestBuilder getClass = mockRequest.get(params, URL_TEMPLATE + "/date");
        final ResultActions perform = mockMvc.perform(getClass);
        assertEquals(HttpStatus.OK.value(), perform.andReturn().getResponse().getStatus());

        List<ClassResponseDTO> list = mapper.readValue(content.getContentAsString(perform),
                new TypeReference<>() { });
        assertEquals(0, list.size());
    }

    @Test
    void shouldNotFindAClassByNameAndDateWhenThereIsNoBook() throws Exception {
        shouldCreateAClass();

        Map<String, Object> params = new HashMap<>();
        params.put("className", "taekwondo");
        params.put("date", LocalDate.of(2021,12,5));

        final MockHttpServletRequestBuilder getClass = mockRequest.get(params, URL_TEMPLATE + "/taekwondo/2021-12-05" );
        final ResultActions perform = mockMvc.perform(getClass);
        assertEquals(HttpStatus.OK.value(), perform.andReturn().getResponse().getStatus());

        List<ClassResponseDTO> list = mapper.readValue(content.getContentAsString(perform),
                new TypeReference<>() { });
        assertEquals(0, list.size());
    }

    @Test
    void shouldFindAClassByNameAndDateWhenThereIsBook() throws Exception {
        shouldCreateAClass();
        createBook(BookingDTO.builder()
                .className("taekwondo")
                .bookingDate(LocalDate.of(2021, 12, 5))
                .memberName("kristian")
                .build());

        Map<String, Object> params = new HashMap<>();
        params.put("className", "taekwondo");
        params.put("date", LocalDate.of(2021,12,5));

        final MockHttpServletRequestBuilder getClass = mockRequest.get(params, URL_TEMPLATE + "/taekwondo/2021-12-05" );
        final ResultActions perform = mockMvc.perform(getClass);
        assertEquals(HttpStatus.OK.value(), perform.andReturn().getResponse().getStatus());

        List<ClassResponseDTO> list = mapper.readValue(content.getContentAsString(perform),
                new TypeReference<>() { });
        assertEquals(EXPECTED_QTY_CLASS, list.size());
    }

    @Test
    void shouldFindAClassByNameAndDateWhenThereIsALotOfBooks() throws Exception {
        shouldCreateAClass();
        createBook(BookingDTO.builder()
                .className("taekwondo")
                .bookingDate(LocalDate.of(2021, 12, 5))
                .memberName("kristian")
                .build());
        createBook(BookingDTO.builder()
                .className("taekwondo")
                .bookingDate(LocalDate.of(2021, 12, 5))
                .memberName("john")
                .build());

        Map<String, Object> params = new HashMap<>();
        params.put("className", "taekwondo");
        params.put("date", LocalDate.of(2021,12,5));

        final MockHttpServletRequestBuilder getClass = mockRequest.get(params, URL_TEMPLATE + "/taekwondo/2021-12-05" );
        final ResultActions perform = mockMvc.perform(getClass);
        assertEquals(HttpStatus.OK.value(), perform.andReturn().getResponse().getStatus());

        List<ClassResponseDTO> list = mapper.readValue(content.getContentAsString(perform),
                new TypeReference<>() { });
        assertEquals(EXPECTED_QTY_CLASS, list.size());
        assertEquals(2, list.get(0).getBookingClass().get(0).getBookings().size());
    }

    private void createBook(BookingDTO dto) throws Exception {
        final MockHttpServletRequestBuilder request = mockRequest.post(dto, "/bookings");
        final ResultActions resultActions = mockMvc.perform(request);
        assertEquals(HttpStatus.CREATED.value(), resultActions.andReturn().getResponse().getStatus());
    }

}
