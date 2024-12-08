package com.insurance.insurance.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public String handleDataNotFoundException(DataNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error-page"; // error-page.html 렌더링
    }
}
