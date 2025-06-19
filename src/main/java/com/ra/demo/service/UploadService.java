package com.ra.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Tệp rỗng hoặc null");
        }

        String originalFileName = file.getOriginalFilename();
        String publicId = UUID.randomUUID().toString(); // public_id duy nhất

        if (originalFileName != null && originalFileName.contains(".")) {
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            publicId = publicId + extension;
        }

        Map uploadParams = ObjectUtils.asMap(
                "public_id", publicId,
                "resource_type", "image"
        );

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Tải tệp lên Cloudinary thất bại", e);
        }
    }
}