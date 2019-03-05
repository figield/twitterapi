package com.twitter.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Post {

    private PostReference postReference;
    private AccountReference accountReference;
    private LocalDateTime dateTime;
    private String message;


    public Post clone() {
        return Post.builder()
                   .postReference(this.getPostReference())
                   .accountReference(this.getAccountReference())
                   .dateTime(this.dateTime)
                   .message(this.getMessage())
                   .build();
    }

}
