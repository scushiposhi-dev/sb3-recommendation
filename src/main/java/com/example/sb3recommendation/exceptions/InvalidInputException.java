package com.example.sb3recommendation.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidInputException extends RuntimeException{
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputException(Throwable cause) {
        super(cause);
    }
}
