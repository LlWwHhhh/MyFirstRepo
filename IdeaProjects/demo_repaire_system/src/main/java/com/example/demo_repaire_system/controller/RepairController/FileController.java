package com.example.demo_repaire_system.controller.RepairController;

import com.example.demo_repaire_system.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/file")
public class FileController {
    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Value("${file.upload.url-prefix}")
    private String urlPrefix;

    @PostMapping("/upload")
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) {
        Map<String, String> result = new HashMap<>();
        try {
            String fileName = fileUploadUtil.upload(file);
            String imageUrl = urlPrefix + fileName;

            // ✅ 关键：返回 { url: "http://xxxx" } 格式
            result.put("url", imageUrl);
            return result;
        } catch (Exception e) {
            result.put("url", "");
            return result;
        }
    }
}