package com.twitter.api.dto;

import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CreatePostRequest {

    @Size(min = 1, max = 140, message = "length must be between 1 and 140 characters")
    private String message;
}
