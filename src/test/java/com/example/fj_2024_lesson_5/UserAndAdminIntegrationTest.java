package com.example.fj_2024_lesson_5;

import com.example.fj_2024_lesson_5.dto.SignInRequest;
import com.example.fj_2024_lesson_5.dto.SignUpRequest;
import com.example.fj_2024_lesson_5.entity.Role;
import com.example.fj_2024_lesson_5.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAndAdminIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerUserSuccessTest() throws Exception {
        SignUpRequest request = new SignUpRequest("newUser", "newEmail@example.com", "password123");

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
        {
            "username": "%s",
            "email": "%s",
            "password": "%s"
        }
        """.formatted(request.getUsername(), request.getPassword(), request.getEmail())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists());

        User savedUser = userRepository.findByUsername("newUser").orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("newUser");
    }

    @Test
    void loginUserSuccessTest() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testUser@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(Role.USER);
        userRepository.save(user);

        SignInRequest request = new SignInRequest();
        request.setUsername("testUser");
        request.setPassword("password");
        request.setRememberedUser(true);

        mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "username": "%s",
                    "password": "%s",
                    "rememberedUser": %s
                }
                """.formatted(request.getUsername(), request.getPassword(), request.isRememberedUser())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void accessAdminEndpointWithAdminRole() throws Exception {
        mockMvc.perform(post("/admin/some-endpoint")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void accessAdminEndpointWithUserRoleForbidden() throws Exception {
        mockMvc.perform(post("/admin/some-endpoint")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessAdminEndpointWithAnonymousUserShouldFail() throws Exception {
        mockMvc.perform(post("/admin/some-endpoint")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
