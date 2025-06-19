package com.ra.demo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginRequestDTO {
    @NotBlank
    @Size(min = 6, max = 100, message = "Tên đăng nhập phải từ 6 đến 100 ký tự")
    private String username;

    @NotBlank
    @Size(min = 8, max = 100, message = "Mật khẩu phải từ 8 đến 100 ký tự")
    private String password;
}