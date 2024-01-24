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
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.StorageRepository;
import com.challenger.fridge.service.SignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
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
        String name = "jjw";
        SignUpRequest request = createSignUpRequest(name);
        SignUpResponse response = createSignUpResponse(name);
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
        ApiResponse apiResponse = ApiResponse.error(message);
        return apiResponse;
    }

    private ApiResponse createApiSuccessResponse(SignUpResponse response) {
        return ApiResponse.success(response);
    }

    private SignUpResponse createSignUpResponse(String name) {
        return new SignUpResponse(name);
    }

    private SignUpRequest createSignUpRequest(String name) {
        return new SignUpRequest("jjw@test.com", "1234", name);
    }
}
