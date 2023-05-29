package com.crewmeister.cmcodingchallenge.exception;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResponse {
    private String message;
    private LocalDateTime dateTime;
}
