package com.twitter.api.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.twitter.api.dto.CreatePostRequest;
import com.twitter.api.dto.FollowResponse;
import com.twitter.api.dto.PostResponse;
import com.twitter.domain.TwitterStorageService;
import com.twitter.domain.model.AccountReference;
import com.twitter.domain.model.Post;
import com.twitter.domain.service.TwitterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@Validated
public class TwitterApiController {

    final TwitterService twitterService;

    final TwitterStorageService twitterStorageService;

    @RequestMapping("/domain-health")
    @GetMapping()
    public String domainHealthCheck() {
        return twitterService.health();
    }

    @RequestMapping("/repository-health")
    @GetMapping()
    public String repositoryHealthCheck() {
        return twitterStorageService.health();
    }

    @RequestMapping("/rest-health")
    @GetMapping()
    public String restHealthCheck() {
        return "Twitter REST API is up and running";
    }

    @PostMapping(value = "/create_post/{accountReference}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createPost(
        @PathVariable("accountReference") @NotBlank String accountReference,
        @RequestBody @Valid CreatePostRequest createPostRequest) {

        return ResponseEntities.ok(
            PostResponse.from(twitterService.publish(
                Post.builder()
                    .accountReference(AccountReference.of(accountReference))
                    .message(createPostRequest.getMessage())
                    .build())));
    }

    @GetMapping(value = "/wall/{accountReference}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity wallPosts(
        @PathVariable("accountReference") @NotBlank String accountReference) {

        return ResponseEntities.ok(
            PostResponse.from(twitterService.getWallPosts(
                AccountReference.of(accountReference))));
    }

    @GetMapping(value = "/timeline/{accountReference}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity timelinePosts(
        @PathVariable("accountReference") @NotBlank String accountReference) {

        return ResponseEntities.ok(
            PostResponse.from(twitterService.getTimelinePosts(
                AccountReference.of(accountReference))));
    }

    @GetMapping(value = "/follow/{accountReference}/{followedUserReference}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity followUser(
        @PathVariable("accountReference") @NotBlank String accountReference,
        @PathVariable("followedUserReference") @NotBlank String followedUserReference) {

        return ResponseEntities.ok(
            FollowResponse.from(
                twitterService.follow(
                    AccountReference.of(accountReference),
                    AccountReference.of(followedUserReference))));
    }

}
