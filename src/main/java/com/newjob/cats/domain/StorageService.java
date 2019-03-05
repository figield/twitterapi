package com.newjob.cats.domain;

import com.newjob.cats.domain.dto.Post;
import com.newjob.cats.domain.dto.User;

import java.util.List;

public interface StorageService {

    String savePost(Post post);

    String deletePost(Post post);

    String saveFollower(User user, User userToFollow);

    String deleteFollower(User user, User userToFollow);

    List<Post> getUserPosts(User user);

    List<Post> getUserTimelinePosts(User user);
}
