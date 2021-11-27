package com.glofox.book.controller;

import com.glofox.book.aspect.ValidateDate;
import com.glofox.book.domain.model.ClassEntity;
import com.glofox.book.domain.service.ClassService;
import com.glofox.book.http._makers.MakeClass;
import com.glofox.book.http.dto.ClassDTO;
import com.glofox.book.http.dto.ClassResponseDTO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.glofox.book.http._makers.MakeClass.toResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/classes")
@Api(tags = "Classes for studio where members can attend classes.")
public class ClassesController {

    private final ClassService service;

    @ValidateDate
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ClassResponseDTO createClass(HttpServletRequest request,
                                        @Validated
                                        @RequestBody ClassDTO dto) {
        return toResponse(service.save(dto));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ClassResponseDTO> findAll(HttpServletRequest request) {
        return service.findAll()
                .stream()
                .map(MakeClass::toResponse)
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/id")
    public ClassResponseDTO findById(HttpServletRequest request, @RequestParam Long id) {
        ClassEntity entity = service.findById(id);
        return toResponse(entity);
    }

}
