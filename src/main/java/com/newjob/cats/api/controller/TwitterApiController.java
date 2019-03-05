package com.newjob.cats.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Validated
public class TwitterApiController {

    @RequestMapping("/health")
    @GetMapping()
    public String healthCheck() {
        return "I am healthy";
    }



}
