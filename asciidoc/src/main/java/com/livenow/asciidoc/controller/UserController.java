package com.livenow.asciidoc.controller;

import com.livenow.asciidoc.controller.dto.UserRequest;
import com.livenow.asciidoc.controller.dto.UserResponse;
import com.livenow.asciidoc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signUp(@RequestBody UserRequest userRequest) {
        final UserResponse userResponse = userService.signUp(userRequest);
        log.info("유저 가입 성공! 유저 이름: {}", userResponse.getName());
        return userResponse;
    }
}
