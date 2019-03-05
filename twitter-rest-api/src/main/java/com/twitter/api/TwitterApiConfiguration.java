package com.twitter.api;

import com.twitter.domain.TwitterStorageService;
import com.twitter.domain.service.TwitterService;
import com.twitter.repo.service.InMemoryTwitterStorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.twitter.api.Profiles.DEFAULT;


@ComponentScan
@Configuration
public class TwitterApiConfiguration {

    @Profile(DEFAULT)
    @Bean
    public TwitterStorageService defaultDomainConfiguration() {
        return new InMemoryTwitterStorageService();
    }


    @Bean
    public TwitterService twitterService(TwitterStorageService twitterStorageService) {
        return new TwitterService(twitterStorageService);
    }


}
