package com.ra.demo.model.dto;
import lombok.*;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String fullname;
    private String phone;
    private String avatar;
    private boolean status;
    private Set<String> roles;
    private Date createdAt;
}