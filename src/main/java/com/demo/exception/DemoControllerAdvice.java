package com.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class DemoControllerAdvice {

    Logger logger = LoggerFactory.getLogger(DemoControllerAdvice.class);

    @ExceptionHandler(value = Exception.class)
    public String handleException(HttpServletRequest request, Exception ex) {

        logger.error("Request " + request.getRequestURL() + " Threw an Exception", ex);
        return "error";
    }

}
