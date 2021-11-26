package com.glofox.book.controller.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class ContentResultActions {

    public String getContentAsString(ResultActions resultActions) throws UnsupportedEncodingException {
        return resultActions.andReturn().getResponse().getContentAsString();
    }

}
