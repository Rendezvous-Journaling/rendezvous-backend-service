package com.rendezvous.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rendezvous.backend.dtos.EntryRequestDto;
import com.rendezvous.backend.exceptions.InvalidPermissionsException;
import com.rendezvous.backend.exceptions.ResourceNotFoundException;
import com.rendezvous.backend.models.Entry;
import com.rendezvous.backend.services.EntryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class EntryController {
	
	@Autowired
	private EntryService entryService;
	
	// Get the entries of a specific user
	@GetMapping("/entries/{userId}")
	public ResponseEntity<?> getUserEntries(@PathVariable Long userId) throws Exception{
		
		List<Entry> response = entryService.getUserEntries(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	// Get one entry of a specific user
	@GetMapping("/entry/{entryId}/user/{userId}")
	public ResponseEntity<?> getUserEntry(@PathVariable Long entryId, @PathVariable Long userId) throws ResourceNotFoundException, InvalidPermissionsException{
		
		Entry response = entryService.getUserEntry(entryId, userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	// Create an entry linked to a specific user
	@PostMapping("/entry")
	public ResponseEntity<?> createUserEntry(@RequestBody Entry entry) throws ResourceNotFoundException, InvalidPermissionsException{
		
		Entry response = entryService.createUserEntry(entry);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	// Update an entry of a specific user
	@PutMapping("/entry")
	public ResponseEntity<?> updateUserEntry(@Valid @RequestBody EntryRequestDto entry) throws ResourceNotFoundException, InvalidPermissionsException {
		Entry response = entryService.updateUserEntry(entry);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	// Delete an entry from a specific user
	@DeleteMapping("/entry")
	public ResponseEntity<?> deleteUserEntry(@Valid @RequestBody EntryRequestDto entry) throws InvalidPermissionsException, ResourceNotFoundException{
		
		Entry response = entryService.deleteUserEntry(entry);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
	}
}
