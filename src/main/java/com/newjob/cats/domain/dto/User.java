package com.newjob.cats.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class User {

    private String username;
    private List<User> follows;
}
