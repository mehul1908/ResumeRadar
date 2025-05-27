package com.resumeradar.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.resumeradar.model.ApiResponse;
import com.resumeradar.service.ResumeService;
import com.resumeradar.service.UserService;


@RestController
@RequestMapping("/candidate")
public class CandidateController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ResumeService resumeService;
	
	@Value("${file.upload-dir}")
    private String uploadDir;
	
	@PostMapping("/uploadresume")
	public ResponseEntity<ApiResponse> uploadResume(@RequestParam("file") MultipartFile file){
		try {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
	}
}
