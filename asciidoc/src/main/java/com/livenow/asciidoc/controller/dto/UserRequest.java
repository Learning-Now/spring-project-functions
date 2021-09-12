package com.livenow.asciidoc.controller.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRequest {

    private String name;
    private int age;

    public UserRequest(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
