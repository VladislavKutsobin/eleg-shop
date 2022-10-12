package com.shop.eleg.dto.request;

import lombok.Data;

@Data
public class SignupRequestDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
