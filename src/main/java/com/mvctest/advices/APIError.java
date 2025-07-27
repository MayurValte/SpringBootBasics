package com.mvctest.advices;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class APIError {
    private HttpStatus httpStatus;
    private String message;
}
