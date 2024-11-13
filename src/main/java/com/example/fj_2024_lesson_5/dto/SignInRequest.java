package com.example.fj_2024_lesson_5.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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