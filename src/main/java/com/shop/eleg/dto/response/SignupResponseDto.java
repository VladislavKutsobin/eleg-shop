package com.shop.eleg.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
