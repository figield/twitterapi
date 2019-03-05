package com.twitter.repo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.twitter.domain.TwitterStorageService;
import com.twitter.domain.model.AccountReference;
import com.twitter.domain.model.Post;
import com.twitter.domain.model.PostReference;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InMemoryTwitterStorageService implements TwitterStorageService {

    private final Map<AccountReference, List<Post>> userPosts = new ConcurrentHashMap<>();

    private final Map<AccountReference, Set<AccountReference>> userFollowedFriends = new ConcurrentHashMap<>();

    private AtomicLong nextPostId = new AtomicLong(1L);


    public Post savePost(Post postToSave) {
        return saveAndGet(postToSave);
    }


    public List<Post> getWallPosts(AccountReference accountReference) {
        return new ArrayList<>(userPosts.getOrDefault(accountReference, new ArrayList<>()));
    }

    public Set<AccountReference> addFollowedFriend(AccountReference userReference, AccountReference userToFollow) {
        Set<AccountReference> followedFriends = userFollowedFriends.getOrDefault(userReference, new HashSet<>());
        followedFriends.add(userToFollow);
        userFollowedFriends.put(userReference, followedFriends);
        return followedFriends.stream()
                              .map(accountReference -> accountReference.clone())
                              .collect(Collectors.toSet());
    }

    public List<Post> getUserTimelinePosts(AccountReference accountReference) {
        return userFollowedFriends.getOrDefault(accountReference, new HashSet<>())
                                  .stream()
                                  .flatMap(u -> userPosts.getOrDefault(u, new ArrayList<>()).stream())
                                  .map(p -> p.clone())
                                  .collect(Collectors.toList());

    }

    public String health() {
        return "Twitter Repository is up and running";
    }


    private Post saveAndGet(Post postToSave) {
        Post savedPost = postToSave.toBuilder()
                                   .dateTime(postToSave.getDateTime() == null ? LocalDateTime.now() : postToSave.getDateTime())
                                   .postReference(PostReference.of(nextPostId.getAndAdd(1)))
                                   .build();

        userPosts.put(savedPost.getAccountReference(), updatePosts(savedPost));
        return savedPost.clone();
    }

    private List<Post> updatePosts(Post post) {
        List<Post> posts = userPosts.getOrDefault(post.getAccountReference(), new ArrayList<>());
        posts.add(post);
        return posts;
    }
}
