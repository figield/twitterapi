package com.twitter.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.twitter.domain.TwitterStorageService;
import com.twitter.domain.model.AccountReference;
import com.twitter.domain.model.Post;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class TwitterService {

    TwitterStorageService twitterStorageService;

    public Post publish(Post post) {
        Post postToSave = post.toBuilder()
                              .dateTime(post.getDateTime() == null ? LocalDateTime.now() : post.getDateTime())
                              .build();
        return twitterStorageService.savePost(postToSave);
    }

    public List<Post> getWallPosts(AccountReference accountReference) {

        List<Post> posts = twitterStorageService.getWallPosts(accountReference);
        postSorter(posts);
        return posts;
    }

    public String health() {
        return "Twitter Domain is up and running";
    }

    public Set<AccountReference> follow(AccountReference userReference, AccountReference userReferenceToFollow) {
        return twitterStorageService.addFollowedFriend(userReference, userReferenceToFollow);
    }


    public List<Post> getTimelinePosts(AccountReference userReference) {
        List<Post> posts = twitterStorageService.getUserTimelinePosts(userReference);
        postSorter(posts);
        return posts;
    }

    private void postSorter(List<Post> posts) {
        posts.sort((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));
    }
}
