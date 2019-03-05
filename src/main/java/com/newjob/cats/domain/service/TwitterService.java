package com.newjob.cats.domain.service;

import com.newjob.cats.domain.StorageService;
import com.newjob.cats.domain.dto.Post;
import com.newjob.cats.domain.dto.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
public class TwitterService {

    @Autowired
    StorageService storageService;

    public String publish(Post post) {
        return storageService.savePost(post);
    }

    public String deletePost(Post post) {
        return storageService.deletePost(post);
    }

    public String follow(User user, User userToFollow) {
        return storageService.saveFollower(user, userToFollow);
    }

    public String unFollow(User user, User userToFollow) {
        return storageService.deleteFollower(user, userToFollow);
    }

    public List<Post> getUserWallPosts(User user) {
        return storageService.getUserPosts(user);
    }

    public List<Post> getUserTimelinePosts(User user) {
        return storageService.getUserTimelinePosts(user);
    }
}
