package com.twitter.domain.service

import com.github.javafaker.Faker
import com.twitter.domain.model.AccountReference
import com.twitter.domain.model.Post
import com.twitter.repo.service.InMemoryTwitterStorageService
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class TwitterServiceSpec extends Specification {


    @Subject
    TwitterService service = new TwitterService(new InMemoryTwitterStorageService())


    def "Should check if the health check message is correct"() {
        when:
            String result = service.health()
        then:
            result == "Twitter Domain is up and running"

    }

    def "Should be able to publish post"() {
        when:
            Post result = service.publish(post)
        then:
            with(result) {
                message == message
                postReference.reference == 1L
                accountReference == accountReference
                dateTime != null
            }
        where:
            accountReference = AccountReference.of("username1")
            message = new Faker(Locale.UK).lorem().characters(140)
            post = Post.builder()
                       .accountReference(accountReference)
                       .message(message)
                       .build()
    }

    def "Should be able to publish and get wall posts"() {
        when:
            service.publish(post)
        and:
            List<Post> result = service.getWallPosts(accountReference)
        then:
            result.size() == 1
            with(result[0]) {
                message == message
                postReference.reference == 1L
                accountReference == accountReference
                dateTime != null
            }
        where:
            accountReference = AccountReference.of("username1")
            message = new Faker(Locale.UK).lorem().characters(140)
            post = Post.builder()
                       .accountReference(accountReference)
                       .message(message)
                       .build()
    }

    def "Should get wall posts in reverse chronological order"() {
        when:
            service.publish(post1)
        and:
            service.publish(post2)
        and:
            List<Post> result = service.getWallPosts(accountReference)
        then:
            result.size() == 2
            result[0].postReference.reference > result[1].postReference.reference
            result[0].dateTime.isAfter(result[1].dateTime)

        where:
            accountReference = AccountReference.of("username1")
            post1 = Post.builder()
                        .accountReference(accountReference)
                        .dateTime(LocalDateTime.now())
                        .message("message")
                        .build()
            post2 = Post.builder()
                        .accountReference(accountReference)
                        .dateTime(LocalDateTime.now().plusSeconds(1))
                        .message("message")
                        .build()
    }

    def "Should follow new users"() {
        given:
            service.follow(user1, user2)
            service.follow(user1, user2)
        when:
            Set<AccountReference> result = service.follow(user1, user3)
        then:
            result.size() == 2
        where:
            user1 = AccountReference.of("username1")
            user2 = AccountReference.of("username2")
            user3 = AccountReference.of("username3")
    }


    def "Should get time line posts in reverse chronological order"() {
        given:
            service.follow(user1, user2)
            service.follow(user1, user3)
        when:
            service.publish(post1)
            service.publish(post2)
        and:
            List<Post> result = service.getTimelinePosts(user1)
        then:
            result.size() == 2
        and:
            with(result[0]) {
                message == "message2"
                accountReference.reference == "username3"
            }
            with(result[1]) {
                message == "message1"
                accountReference.reference == "username2"
            }
        and:
            result[0].postReference.reference > result[1].postReference.reference
            result[0].dateTime.isAfter(result[1].dateTime)
        where:
            user1 = AccountReference.of("username1")
            user2 = AccountReference.of("username2")
            user3 = AccountReference.of("username3")
            post1 = Post.builder()
                        .accountReference(user2)
                        .dateTime(LocalDateTime.now())
                        .message("message1")
                        .build()
            post2 = Post.builder()
                        .accountReference(user3)
                        .dateTime(LocalDateTime.now().plusSeconds(1))
                        .message("message2")
                        .build()
    }

}
