package com.twitter.api.dto;

import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;

import com.twitter.domain.model.AccountReference;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowResponse {

    @NotBlank
    private Set<String> usernames;

    public static FollowResponse from(Set<AccountReference> followedAccountReference) {
        return FollowResponse.builder()
                             .usernames(followedAccountReference.stream()
                                                                .map(accountReference -> accountReference.getReference())
                                                                .collect(Collectors.toSet()))
                             .build();
    }
}
