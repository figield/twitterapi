package com.twitter.repo.service

import com.github.javafaker.Faker
import com.twitter.domain.TwitterStorageService
import com.twitter.domain.model.AccountReference
import com.twitter.domain.model.Post
import com.twitter.domain.model.PostReference
import spock.lang.Specification
import spock.lang.Subject

class InMemoryTwitterStorageServiceSpec extends Specification {

    @Subject
    TwitterStorageService storage = new InMemoryTwitterStorageService()


    def "Should find previously stored post by a user account reference"() {
        given:
            storage.savePost(post)
        when:
            List<Post> result = storage.getWallPosts(accountReference)
        then:
            result.size() == 1
            with(result[0]) {
                message == message
                postReference == PostReference.of(1L)
                accountReference == accountReference
            }
        where:
            accountReference = AccountReference.of("username1")
            message = new Faker(Locale.UK).lorem().characters(140)
            post = Post.builder()
                       .postReference(PostReference.of(1L))
                       .accountReference(accountReference)
                       .message(message)
                       .build()

    }

    def "Should not find previously not stored post by a user account reference"() {
        given:
            storage.savePost(post)
        when:
            List<Post> result = storage.getWallPosts(accountReference2)
        then:
            result.size() == 0
        where:
            accountReference = AccountReference.of("username1")
            accountReference2 = AccountReference.of("username2")

            message = new Faker(Locale.UK).lorem().characters(140)
            post = Post.builder()
                       .postReference(PostReference.of(1L))
                       .accountReference(accountReference)
                       .message(message)
                       .build()

    }

    def "Should add new users to followed list"() {
        given:
            storage.addFollowedFriend(user1, user2)
            storage.addFollowedFriend(user1, user2)
        when:
            Set<AccountReference> result = storage.addFollowedFriend(user1, user3)
        then:
            result.size() == 2
        where:
            user1 = AccountReference.of("username1")
            user2 = AccountReference.of("username2")
            user3 = AccountReference.of("username3")

    }

    def "Should get posts of the followed users"() {
        given:
            storage.addFollowedFriend(user1, user2)
            storage.addFollowedFriend(user1, user3)
        when:
            storage.savePost(post1)
            storage.savePost(post2)
            List<Post> result = storage.getUserTimelinePosts(user1)
        then:
            result.size() == 2
            with(result[0]) {
                message == "message1"
                accountReference.reference == "username2"
            }
            with(result[1]) {
                message == "message2"
                accountReference.reference == "username3"
            }

        where:
            user1 = AccountReference.of("username1")
            user2 = AccountReference.of("username2")
            user3 = AccountReference.of("username3")
            post1 = Post.builder()
                        .accountReference(user2)
                        .message("message1")
                        .build()
            post2 = Post.builder()
                        .accountReference(user3)
                        .message("message2")
                        .build()

    }

    def "Should check if the health check message is correct"() {
        when:
            String result = storage.health()
        then:
            result == "Twitter Repository is up and running"

    }

}