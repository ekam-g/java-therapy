package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {

	@GetMapping("/bot")
	public AiReponse api(@RequestParam(value = "text", defaultValue = "none") String text) {
		if (text.isBlank()) {
			return new AiReponse("");
		}
		return new AiReponse("Placeholder");
	}
}
