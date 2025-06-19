package com.ra.demo.model.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRegisterRequestDTO {
    @NotBlank
    @Size(min = 6, max = 100, message = "Tên đăng nhập phải từ 6 đến 100 ký tự")
    private String username;

    @NotBlank
    @Email
    @Size(max = 255, message = "Email không được vượt quá 100 ký tự")
    private String email;

    @NotBlank
    @Size(min = 8, max = 100, message = "Mật khẩu phải từ 8 đến 100 ký tự")
    private String password;

    @NotBlank
    @Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự")
    private String fullname;

    @NotBlank
    @Pattern(regexp = "^0[0-9]{9}$", message = "Số điện thoại phải có 10 số, bắt đầu bằng 0")
    private String phone;

    private MultipartFile avatar;
}