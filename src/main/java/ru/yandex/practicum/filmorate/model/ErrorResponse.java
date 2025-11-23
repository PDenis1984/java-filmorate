package ru.yandex.practicum.filmorate.model;

import java.time.LocalDateTime;

public class ErrorResponse {
    private String message;
    private int httpCode;
    private LocalDateTime timestamp;


    public ErrorResponse(String message, int httpCode) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.httpCode = httpCode;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
