package com.ra.demo.model.dto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginResponse {
    private String username;
    private String accessToken;
    private String typeToken;
    private List<String> roles;
    private String avatar;
}