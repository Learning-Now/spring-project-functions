package com.livenow.asciidoc.controller;

import com.livenow.asciidoc.ApiDocument;
import com.livenow.asciidoc.controller.dto.UserRequest;
import com.livenow.asciidoc.controller.dto.UserResponse;
import com.livenow.asciidoc.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 컨트롤러 mock api 테스트 ")
@WebMvcTest(UserController.class)
class UserControllerTest extends ApiDocument {

    @MockBean
    private UserService userService;

    @Test
    void user_sign_up() throws Exception {
        //given
        final UserRequest signUpRequest = new UserRequest("검프", 23);
        final UserResponse signUpResponse = new UserResponse("검프");
        //when
        willReturn(signUpResponse).given(userService).signUp(any(UserRequest.class));
        final ResultActions response = 유저_회원가입_요청(signUpRequest);
        //then
        유저_회원가입_성공함(signUpResponse, response);
    }

    private ResultActions 유저_회원가입_요청(UserRequest signUpRequest) throws Exception {
        return mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(signUpRequest)));
    }

    private void 유저_회원가입_성공함(UserResponse signUpResponse, ResultActions response) throws Exception {
        response.andExpect(status().isCreated())
                .andExpect(content().json(toJson(signUpResponse)))
                .andDo(print())
                .andDo(toDocument("user-signup"));
    }
}