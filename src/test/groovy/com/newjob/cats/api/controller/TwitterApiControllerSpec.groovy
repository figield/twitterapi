//package com.newjob.cats.api.controller
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.boot.test.context.TestConfiguration
//import org.springframework.test.web.servlet.MockMvc
//import spock.lang.Specification
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//
//@WebMvcTest(controllers = [TwitterApiController])
//class TwitterApiControllerSpec extends Specification {
//
//    @Autowired
//    protected MockMvc mvc
//
//    def "should get health status"() {
//
//        when:
//        def results = mvc.perform(get('/health'))
//
//        then:
//        results.andExpect(status().isOk())
//
//        and:
//        results.andExpect(content().string("I am healthy"))
//
//    }
//    @TestConfiguration
//    static class StubConfig {
//       // DetachedMockFactory detachedMockFactory = new DetachedMockFactory()
//
//    }
//
//}
