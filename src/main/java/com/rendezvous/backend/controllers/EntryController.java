package com.rendezvous.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rendezvous.backend.models.Entry;
import com.rendezvous.backend.services.EntryService;

@RestController
@RequestMapping("/api")
public class EntryController {
	
	@Autowired
	private EntryService entryService;
	
	// Get the entries of a specific user
	@GetMapping("/entries/{userId}")
	public ResponseEntity<?> getUserEntries(@PathVariable Long userId){
		
		List<Entry> response = entryService.getUserEntries(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	// Get one entry of a specific user
	@GetMapping("/entry/{entryId}/user/{userId}")
	public ResponseEntity<?> getUserEntry(@PathVariable Long entryId, @PathVariable Long userId){
		
		Entry response = entryService.getUserEntry(entryId, userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	// Create an entry linked to a specific user
	@PostMapping("/entry")
	public ResponseEntity<?> createUserEntry(@RequestBody Entry entry){
		
		Entry response = entryService.createUserEntry(entry);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	// Update an entry of a specific user
	
	// Delete an entry from a specific user

}
