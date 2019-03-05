package com.twitter.domain;


import java.util.List;
import java.util.Set;

import com.twitter.domain.model.AccountReference;
import com.twitter.domain.model.Post;

public interface TwitterStorageService {

    String health();

    Post savePost(Post post);

    List<Post> getWallPosts(AccountReference accountReference);

    Set<AccountReference> addFollowedFriend(AccountReference userReference, AccountReference userReferenceToFollow);

    List<Post> getUserTimelinePosts(AccountReference accountReference);
}
