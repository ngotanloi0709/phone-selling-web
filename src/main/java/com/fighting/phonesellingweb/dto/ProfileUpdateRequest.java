package com.fighting.phonesellingweb.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileUpdateRequest {
    private String name;

    private String address;

    private String phone;

    private MultipartFile avatar;
}
