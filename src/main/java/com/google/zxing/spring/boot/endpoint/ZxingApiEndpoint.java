package com.google.zxing.spring.boot.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/qrcode/api/")
public class ZxingApiEndpoint {
	 
	@PostMapping("xx")
	@ResponseBody
	public ResponseEntity<String> qrcode(@RequestParam String content, @RequestParam int width, @RequestParam int height) {
		
		return ResponseEntity.ok("s");
	}
	
}
