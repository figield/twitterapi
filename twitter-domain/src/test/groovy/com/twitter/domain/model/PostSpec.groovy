package com.twitter.domain.model

import spock.lang.Specification

class PostSpec extends Specification {


    def "Should clone post"() {

        when:
            Post clonedPost = post.clone()
        then:
            clonedPost.equals(post)
            !clonedPost.is(post)
        where:
            user = AccountReference.of("username")
            post = Post.builder()
                       .accountReference(user)
                       .message("message1")
                       .build()

    }

}
