package com.glofox.book.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Api(tags = "Controller to verify if the API is online")
public class ApiController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public String validateApi(HttpServletRequest request) {
        return "We are online";
    }

}
