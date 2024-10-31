package com.prometheus.money.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class FileUploadController {
	@GetMapping("/files")
	public ResponseEntity<String> uploadImage(String base64Image) throws IOException {

		System.out.println(base64Image);
		return null;
	}
}
