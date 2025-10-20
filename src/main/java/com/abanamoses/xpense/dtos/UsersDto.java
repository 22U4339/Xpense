package com.abanamoses.xpense.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersDto {
    private String email;
    private String password;
}
