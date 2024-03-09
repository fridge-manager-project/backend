package com.challenger.fridge.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.dto.sign.SignUpResponse;
import com.challenger.fridge.service.SignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/*@WebMvcTest(SignController.class)
@MockBean(JpaMetamodelMappingContext.class)*/
@SpringBootTest
@AutoConfigureMockMvc
class SignControllerTest {

    @Autowired
    private ObjectMapper objectMapper; // response 를 json 으로 바꿔주기 위해 필요한 의존성 주입

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignService signService;

   /* @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private StorageRepository storageRepository;*/

    @Test
    @DisplayName("POST 회원 등록 컨트롤러 로직 확인")
    void signUpTest() throws Exception {
        String nickname = "jjw";
        SignUpRequest request = createSatisfiedSignUpRequest();
        SignUpResponse response = createSignUpResponse(nickname);
        ApiResponse apiResponse = createApiSuccessResponse(response);

        given(signService.registerMember(any(SignUpRequest.class))).willReturn(response);

        String requestJson = objectMapper.writeValueAsString(request);
        String responseJson = objectMapper.writeValueAsString(apiResponse);

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST - 회원 등록 컨트롤러 : 잘못된 이메일 요청")
    void signUpTestWithWrongRequest() throws Exception {
        String wrongEmail = "wrongEmail";
        String password = "jjwPassword1!";
        String nickname = "jjw";
        SignUpRequest requestWithWrongEmail = createSignUpRequest(wrongEmail, password, nickname);

        ApiResponse apiResponseWithWrongEmail = createApiFailResponse("이메일 형식에 맞지 않습니다.");
        String requestJsonWithWrongEmail = objectMapper.writeValueAsString(requestWithWrongEmail);
        String responseJsonWithWrongEmail = objectMapper.writeValueAsString(apiResponseWithWrongEmail);

        mockMvc.perform(post("/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJsonWithWrongEmail)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseJsonWithWrongEmail))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST - 회원 등록 컨트롤러 : 잘못된 비밀번호 요청")
    void signUpTestWithWrongPassword() throws Exception {
        String email = "jjw@test.com";
        String wrongPassword = "wrongpw";
        String nickname = "jjw";
        SignUpRequest requestWithWrongPassword = createSignUpRequest(email, wrongPassword, nickname);

        ApiResponse apiResponseWithWrongPassword = createApiFailResponse("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8 ~20자의 비밀번호여야 합니다.");
        String requestJsonWithWrongPassword = objectMapper.writeValueAsString(requestWithWrongPassword);
        String responseJsonWithWrongPassword = objectMapper.writeValueAsString(apiResponseWithWrongPassword);

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJsonWithWrongPassword)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseJsonWithWrongPassword))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST - 회원 등록 컨트롤러 : 잘못된 닉네임 요청")
    void signUpTestWithWrongNickname() throws Exception {
        String email = "jjw@test.com";
        String password = "jjwPassword1!";
        String wrongNickname = "틀린닉네임";
        SignUpRequest requestWithWrongNickname = createSignUpRequest(email, password, wrongNickname);

        ApiResponse apiResponseWithWrongNickname = createApiFailResponse("닉네임은 영어랑 숫자만 가능합니다.");
        String requestJsonWithWrongNickname = objectMapper.writeValueAsString(requestWithWrongNickname);
        String responseJsonWithWrongNickname = objectMapper.writeValueAsString(apiResponseWithWrongNickname);

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJsonWithWrongNickname)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseJsonWithWrongNickname))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET 이메일 중복 확인 컨틀로러 로직 확인 - 중복 X")
    void checkUniqueEmail() throws Exception {
        String requestEmail = "jjw@test.com";
        boolean response = true;
        ApiResponse apiResponse = createApiSuccessResponse(null);

        given(signService.checkDuplicateEmail(anyString())).willReturn(response);

        String responseJson = objectMapper.writeValueAsString(apiResponse);

        mockMvc.perform(get("/sign-up")
                        .param("email", requestEmail))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET 이메일 중복 확인 컨틀로러 로직 확인 - 중복 O")
    void checkDuplicateEmail() throws Exception {
        String requestEmail = "jjw@test.com";
        String errorMessage = "이미 사용중인 이메일입니다";

        ApiResponse apiResponse = createApiErrorResponse(errorMessage);

        given(signService.checkDuplicateEmail(anyString())).willThrow(new IllegalArgumentException(errorMessage));

        String responseJson = objectMapper.writeValueAsString(apiResponse);

        mockMvc.perform(get("/sign-up")
                        .param("email", requestEmail))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private ApiResponse createApiErrorResponse(String message) {
        return ApiResponse.error(message);
    }

    private ApiResponse createApiSuccessResponse(SignUpResponse response) {
        return ApiResponse.success(response);
    }

    private ApiResponse createApiFailResponse(String message) {
        return ApiResponse.fail(message);
    }

    private SignUpResponse createSignUpResponse(String name) {
        return new SignUpResponse(name);
    }

    private SignUpRequest createSignUpRequest(String email, String password, String nickname) {
        return new SignUpRequest(email, password, nickname);
    }

    private SignUpRequest createSatisfiedSignUpRequest() {
        return createSignUpRequest("jjw@test.com", "jjwPassword1!", "jjw");
    }
}
