package com.rendezvous.backend.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rendezvous.backend.models.Prompt;
import com.rendezvous.backend.repositories.PromptRepo;

import jakarta.validation.Valid;

@Service
public class PromptService {
	
	@Autowired
	private PromptRepo promptRepo;

	public List<Prompt> getAllPrompts() {
		
		List<Prompt> prompts = promptRepo.findAll();
		
		
		return prompts;
	}

	public Prompt createPrompt(@Valid Prompt prompt) {
		
		return promptRepo.save(prompt);
	}

	
	public List<Prompt> createManyPrompts(@Valid List<Prompt> prompts) {
		
		List<Prompt> created = new ArrayList<>();
		for(Prompt prompt : prompts) {
			
			created.add(prompt);
			promptRepo.save(prompt);
		}
		
		
		return created;
	}


	
	
}
