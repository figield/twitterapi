package com.twitter.api.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.OK;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ResponseEntities {

    static <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.status(OK)
                             .body(body);
    }
}
