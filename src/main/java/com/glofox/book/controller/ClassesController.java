package com.glofox.book.controller;

import com.glofox.book.aspect.ValidateDate;
import com.glofox.book.domain.model.ClassEntity;
import com.glofox.book.domain.service.BookService;
import com.glofox.book.domain.service.ClassService;
import com.glofox.book.http._makers.MakeClass;
import com.glofox.book.http.dto.BookingClassResponseDTO;
import com.glofox.book.http.dto.ClassDTO;
import com.glofox.book.http.dto.ClassResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.glofox.book.http._makers.MakeClass.toResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/classes")
@Api(tags = "Classes for studio where members can attend classes.")
public class ClassesController {

    private final ClassService service;
    private final BookService bookService;

    @ValidateDate
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ClassResponseDTO createClass(HttpServletRequest request,
                                        @Validated
                                        @RequestBody ClassDTO dto) {
        final ClassEntity classEntity = service.save(dto);
        return toResponse(classEntity, bookService.findByClass(classEntity));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ClassResponseDTO> findAll(HttpServletRequest request) {
        return service.findAll()
                .stream()
                .map(entity -> MakeClass.toResponse(entity, bookService.findByClass(entity)))
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/id")
    public ClassResponseDTO findById(HttpServletRequest request, @RequestParam Long id) {
        ClassEntity entity = service.findById(id);
        return toResponse(entity, bookService.findByClass(entity));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/class-name")
    public List<ClassResponseDTO> findByClassName(HttpServletRequest request, @RequestParam String className) {
        return service.findByName(className)
                .stream()
                .map(entity -> MakeClass.toResponse(entity, bookService.findByClass(entity)))
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/date")
    public List<ClassResponseDTO> findByDate(HttpServletRequest request,
                                             @RequestParam
                                             @Parameter(description = "Search Date", example = "yyyy-MM-dd")
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                             LocalDate localDate) {
        return service.findByDate(localDate)
                .stream()
                .map(entity -> MakeClass.toResponse(entity, bookService.findByClass(entity)))
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{className}/{date}")
    public List<ClassResponseDTO> findByClassNameAndDate(HttpServletRequest request,
                                                         @RequestParam String className,
                                                         @RequestParam
                                                             @Parameter(description = "Search Date", example = "yyyy-MM-dd")
                                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                     LocalDate date) {
        return service.findByName(className)
                .stream()
                .map(entity -> MakeClass.toResponse(entity, bookService.findByClass(entity)))
                .filter(dto -> isDateOnPeriod(date, dto) && getBookingClass(date, dto))
                .map(i -> MakeClass.toResponse(i, date))
                .collect(Collectors.toList());
    }

    private boolean isDateOnPeriod(LocalDate date, ClassResponseDTO dto) {
        return dto.getStartDate().compareTo(date) <= 0 && dto.getEndDate().compareTo(date) >= 0;
    }

    private boolean getBookingClass(LocalDate date, ClassResponseDTO dto) {
        final List<BookingClassResponseDTO> bookingClass = dto.getBookingClass();
        final List<BookingClassResponseDTO> collect = bookingClass.stream()
                .filter(responseDTO -> responseDTO.getDate().equals(date))
                .collect(Collectors.toList());
        return !collect.isEmpty();
    }

}
