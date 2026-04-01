package com.example.demo_repaire_system.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
public class FileUploadUtil {

    @Value("${file.upload.path}")
    private String uploadPath;

    public String upload(MultipartFile file) throws Exception {
        // 1. 如果目录不存在，自动创建
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 2. 获取原始文件名 a.jpg
        String originalFilename = file.getOriginalFilename();

        // 3. 生成唯一名字（防止覆盖）
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueName = UUID.randomUUID() + suffix;

        // 4. 保存到 D 盘
        File dest = new File(dir, uniqueName);
        file.transferTo(dest);

        // 5. 返回唯一文件名
        return uniqueName;
    }
}
