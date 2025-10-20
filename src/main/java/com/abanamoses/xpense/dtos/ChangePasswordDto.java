package com.abanamoses.xpense.dtos;

import lombok.*;

@Getter
@Setter
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
}
