package com.example.fj_2024_lesson_5.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
    private Boolean rememberedUser;

    public Boolean isRememberedUser() {
        return rememberedUser != null ? rememberedUser : Boolean.FALSE;
    }
}