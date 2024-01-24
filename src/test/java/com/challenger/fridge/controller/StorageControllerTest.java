package com.challenger.fridge.controller;


import com.challenger.fridge.common.StorageMethod;
import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.storage.request.StorageRequest;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.StorageRepository;
import com.challenger.fridge.service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StorageControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    StorageService storageService;

    @Test
    @DisplayName("POST 보관소 추가 컨트롤러 동작")
    @WithMockUser
    public void 보관소추가컨트롤러() throws Exception {
        ApiResponse apiSuccessResponse = createApiSuccessResponse(null);
        String userEmail = "123@naver.com";
        StorageRequest storageRequest = new StorageRequest("냉장고", StorageMethod.FRIDGE);
        given(storageService.saveStorage(storageRequest, userEmail)).willReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/storage")
                        .content(asJsonString(storageRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(apiSuccessResponse)));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private ApiResponse createApiErrorResponse(String message) {
        ApiResponse apiResponse = ApiResponse.error(message);
        return apiResponse;
    }

    private ApiResponse createApiSuccessResponse(Object obj) {
        return ApiResponse.success(obj);
    }
}


