package com.twitter.api.controller

import com.github.javafaker.Faker
import com.twitter.domain.TwitterStorageService
import com.twitter.domain.model.AccountReference
import com.twitter.domain.model.Post
import com.twitter.domain.model.PostReference
import com.twitter.domain.service.TwitterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll
import spock.mock.DetachedMockFactory

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import static groovy.json.JsonOutput.toJson
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [TwitterApiController])
class TwitterApiControllerSpec extends Specification {

    @Autowired
    protected MockMvc mvc

    @Autowired
    TwitterStorageService twitterStorageService

    @Autowired
    TwitterService twitterService

    def "should get REST health status"() {

        when:
            def results = mvc.perform(get('/rest-health'))

        then:
            results.andExpect(status().isOk())

        and:
            results.andExpect(content().string("Twitter REST API is up and running"))

    }

    def "should get domain health status"() {
        given:
            twitterService.health() >> "Twitter Domain is up and running"

        when:
            def results = mvc.perform(get('/domain-health'))

        then:
            results.andExpect(status().isOk())

        and:
            results.andExpect(content().string("Twitter Domain is up and running"))

    }

    def "should get repository health status"() {
        given:
            twitterStorageService.health() >> "Twitter Repository is up and running"

        when:
            def results = mvc.perform(get('/repository-health'))

        then:
            results.andExpect(status().isOk())

        and:
            results.andExpect(content().string("Twitter Repository is up and running"))

    }

    def "Should follow new users"() {
        given:
            Set<AccountReference> user1Friends = new HashSet<>(Arrays.asList(user2))
            twitterService.follow(_, _) >> user1Friends

        when:
            def results = mvc.perform(get('/follow/username1/username2'))

        then:
            results.andExpect(status().isOk())

        and:
            results.andExpect(jsonPath('$.usernames').value(["username2"]))
        where:
            user1 = AccountReference.of("username1")
            user2 = AccountReference.of("username2")

    }

    def "Should publish new post"() {
        given:
            Post post1 = Post.builder()
                             .accountReference(user1)
                             .message(message)
                             .postReference(PostReference.of(1L))
                             .dateTime(date)
                             .build()
            twitterService.publish(_) >> post1
            Map request = [
                    message: message
            ]
        when:
            def results = mvc.perform(
                    post('/create_post/username1')
                            .accept(APPLICATION_JSON)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))

        then:
            results.andExpect(status().isOk())

        and:
            results.andExpect(jsonPath('$.postReference').value("1"))
            results.andExpect(jsonPath('$.username').value(user1.reference))
            results.andExpect(jsonPath('$.message').value(message))
            results.andExpect(jsonPath('$.date').value(date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        where:
            user1 = AccountReference.of("username1")
            message = "message1"
            date = LocalDateTime.now()
    }

    @Unroll
    def "Should throw exception when trying to publish too long post"() {
        given:
            Map request = [
                    message: new Faker(Locale.UK).lorem().characters(messageLen)
            ]
        when:
            def results = mvc.perform(
                    post('/create_post/username1')
                            .accept(APPLICATION_JSON)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))

        then:
            results.andExpect(status().is4xxClientError())

        and:
            results.andExpect(jsonPath('$.messages')
                    .value(errorMesssages))

        where:
            messageLen || errorMesssages
            141        || ["message length must be between 1 and 140 characters"]
            0          || ["message length must be between 1 and 140 characters"]

    }

    def "Should get wall posts"() {
        given:
            Post post1 = Post.builder()
                             .accountReference(user1)
                             .message(message)
                             .postReference(PostReference.of(1L))
                             .dateTime(date)
                             .build()

            twitterService.getWallPosts(_) >> Arrays.asList(post1)

        when:
            def results = mvc.perform(get('/wall/username1'))

        then:
            results.andExpect(status().isOk())

        and:
            results.andExpect(jsonPath('$[0].postReference').value("1"))
            results.andExpect(jsonPath('$[0].username').value(user1.reference))
            results.andExpect(jsonPath('$[0].message').value(message))
            results.andExpect(jsonPath('$[0].date').value(date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        where:
            user1 = AccountReference.of("username1")
            message = "message1"
            date = LocalDateTime.now()
    }


    def "Should get timeline posts"() {
        given:
            Post post1 = Post.builder()
                             .accountReference(user1)
                             .message(message)
                             .postReference(PostReference.of(1L))
                             .dateTime(date)
                             .build()

            twitterService.getTimelinePosts(_) >> Arrays.asList(post1)

        when:
            def results = mvc.perform(get('/timeline/username1'))

        then:
            results.andExpect(status().isOk())

        and:
            results.andExpect(jsonPath('$[0].postReference').value("1"))
            results.andExpect(jsonPath('$[0].username').value(user1.reference))
            results.andExpect(jsonPath('$[0].message').value(message))
            results.andExpect(jsonPath('$[0].date').value(date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        where:
            user1 = AccountReference.of("username1")
            message = "message1"
            date = LocalDateTime.now()
    }

    def "Should return 404 for incorrect url"() {

        when:
            def results = mvc.perform(get('/incorrect/')
                    .accept(APPLICATION_JSON))

        then:
            results.andExpect(status().is4xxClientError())

    }

    def "Should return bad request response if body has incorrect format"() {

        when:
            def results = mvc.perform(
                    post('/create_post/username1')
                            .accept(APPLICATION_JSON)
                            .contentType(APPLICATION_JSON)
                            .content("{"))

        then:
            results.andExpect(status().is4xxClientError())

        and:
            results.andExpect(jsonPath('$.messages')
                    .value("Server is unable to process the request"))
    }

    @TestConfiguration
    static class StubConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()


        @Bean
        TwitterService twitterService() {
            return detachedMockFactory.Stub(TwitterService)

        }

        @Bean
        TwitterStorageService twitterStorageService() {
            return detachedMockFactory.Stub(TwitterStorageService)
        }

    }

}
