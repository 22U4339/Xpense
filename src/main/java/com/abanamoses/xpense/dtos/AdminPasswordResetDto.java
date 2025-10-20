package com.abanamoses.xpense.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminPasswordResetDto {
    private String email;
    private String password;
}
