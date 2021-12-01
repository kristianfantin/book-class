package com.glofox.book.controller;

import com.glofox.book.domain.model.BookEntity;
import com.glofox.book.domain.service.BookService;
import com.glofox.book.domain.service.ClassService;
import com.glofox.book.domain.service.RedisService;
import com.glofox.book.http.dto.BookingDTO;
import com.glofox.book.http.dto.BookingResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.glofox.book.http._makers.MakeBook.toResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
@Api(tags = "Booking a class.")
public class BookingController {

    private final BookService service;
    private final ClassService classService;
    private final RedisService redisService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookingResponseDTO createBook(HttpServletRequest request,
                                         @Validated
                                         @RequestBody BookingDTO dto) {
        final BookEntity bookEntity = service.save(dto);
        redisService.clean();
        return toResponse(bookEntity, classService.findById(bookEntity.getClassId()));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/id")
    public ResponseEntity<?> delete(HttpServletRequest request,
                       @Parameter(description = "Booking ID - ID from Booking you would like remove")
                       @RequestParam
                               Long id) {
        service.delete(id);
        redisService.clean();
        return ResponseEntity.noContent().build();
    }

}
