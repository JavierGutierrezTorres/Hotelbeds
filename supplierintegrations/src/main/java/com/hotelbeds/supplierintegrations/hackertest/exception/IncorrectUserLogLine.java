package com.hotelbeds.supplierintegrations.hackertest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IncorrectUserLogLine extends RuntimeException {

    public IncorrectUserLogLine(String message) {
        super(message);
    }
}
