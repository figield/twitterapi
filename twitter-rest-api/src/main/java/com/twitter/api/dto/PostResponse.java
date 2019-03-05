package com.twitter.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;

import com.twitter.domain.model.Post;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PostResponse {

    @NotBlank
    private Long postReference;
    @NotBlank
    private String username;
    @NotBlank
    private LocalDateTime date;
    @NotBlank
    private String message;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                           .postReference(post.getPostReference().getReference())
                           .message(post.getMessage())
                           .date(post.getDateTime())
                           .username(post.getAccountReference().getReference())
                           .build();

    }

    public static List<PostResponse> from(List<Post> posts) {
        return posts.stream().map(
            post -> PostResponse.builder()
                                .postReference(post.getPostReference().getReference())
                                .message(post.getMessage())
                                .date(post.getDateTime())
                                .username(post.getAccountReference().getReference())
                                .build()).collect(Collectors.toList());
    }


}
