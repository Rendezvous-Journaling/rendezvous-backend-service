package com.rendezvous.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rendezvous.backend.models.Prompt;
import com.rendezvous.backend.services.PromptService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class PromptController {
	
	@Autowired
	private PromptService promptService;
	
	
	@GetMapping("/prompts")
	public ResponseEntity<?> getAllPrompts(){
		
		List<Prompt> response = promptService.getAllPrompts();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/prompt")
	public ResponseEntity<?> getRandomPrompt() throws Exception{
		
		Prompt response = promptService.getRandomPrompt();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("/prompt")
	public ResponseEntity<?> createPrompt(@RequestBody @Valid Prompt prompt){
		
		Prompt response = promptService.createPrompt(prompt);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PostMapping ("/prompts")
	public ResponseEntity<?> createManyPrompts(@RequestBody @Valid List<Prompt> prompts){
		
		List<Prompt> response = promptService.createManyPrompts(prompts);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
