package com.newjob.cats.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Post {

    private Date date;
    private String message;
    private User user;

}
