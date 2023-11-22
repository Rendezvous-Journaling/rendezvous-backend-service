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

import com.google.common.util.concurrent.RateLimiter;
import com.rendezvous.backend.dtos.EntryRequestDto;
import com.rendezvous.backend.exceptions.ErrorDetails;
import com.rendezvous.backend.exceptions.InvalidPermissionsException;
import com.rendezvous.backend.exceptions.RateExceededException;
import com.rendezvous.backend.exceptions.ResourceNotFoundException;
import com.rendezvous.backend.models.Entry;
import com.rendezvous.backend.services.EntryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Entry Controller", description = "Endpoints for managing user entries")
public class EntryController {
	
	@Autowired
	private EntryService entryService;
	
	// Get the entries of a specific user
	@Operation(summary = "Get a list of a user's entries.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The list of entries were retrieved", 
					content = {
							@Content(mediaType = "application/json", 
									array = @ArraySchema(schema = @Schema(implementation = Entry.class))
					)}),
			@ApiResponse(responseCode = "400", description = "Bad Request",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
			@ApiResponse(responseCode = "404", description = "Not Found",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
			@ApiResponse(responseCode = "403", description = "The rate limit was exceeded.",
					content = {
							@Content(mediaType = "application/json", 
									schema = @Schema(implementation = ErrorDetails.class)
							)}
			),
			@ApiResponse(responseCode = "403", description = "The request contains an invalid token",
					content = {
							@Content(mediaType = "application/json", 
									schema = @Schema(implementation = ErrorDetails.class)
							)}
			)
	})
	@GetMapping("/entries/{userId}")
	public ResponseEntity<?> getUserEntries(@PathVariable Long userId) throws Exception{
		
		List<Entry> response = entryService.getUserEntries(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	// Get one entry of a specific user
	@Operation(summary = "Get an entry belonging to a user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The user's entry was retrieved.",
					content = {
							@Content(mediaType = "application/json", 
									schema = @Schema(implementation = Entry.class)
							)}
			),
			@ApiResponse(responseCode = "400", description = "Bad Request",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
			@ApiResponse(responseCode = "404", description = "Entry not found",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
			@ApiResponse(responseCode = "403", description = "Rate limit exceeded",
					content = {
							@Content(mediaType = "application/json", 
									schema = @Schema(implementation = ErrorDetails.class)
							)}
			)
	})
	@GetMapping("/entry/{entryId}/user/{userId}")
	public ResponseEntity<?> getUserEntry(@PathVariable Long entryId, @PathVariable Long userId) throws ResourceNotFoundException, InvalidPermissionsException, RateExceededException{
		
		Entry response = entryService.getUserEntry(entryId, userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	// Create an entry linked to a specific user
	@Operation(summary = "Create an entry.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User entry was created.",
					content = {
							@Content(mediaType = "application/json",
									schema = @Schema(implementation = Entry.class)
							)
					}
			),
			@ApiResponse(responseCode = "400", description = "Bad Request",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
			@ApiResponse(responseCode = "403", description = "Invalid token",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
			@ApiResponse(responseCode = "403", description = "Rate limit exceeded",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
	)
	})
	@PostMapping("/entry")
	public ResponseEntity<?> createUserEntry(@RequestBody Entry entry) throws ResourceNotFoundException, InvalidPermissionsException, RateExceededException{
		
		Entry response = entryService.createUserEntry(entry);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	// Update an entry of a specific user
	@Operation(summary = "Update an entry belonging to the current user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Entry was updated.",
					content = {
							@Content(mediaType = "application/json",
									schema = @Schema(implementation = Entry.class)
							)
					}
			),
			@ApiResponse(responseCode = "400", description = "Bad Request",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
			@ApiResponse(responseCode = "403", description = "Invalid token",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
			@ApiResponse(responseCode = "403", description = "Rate limit exceeded",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
			@ApiResponse(responseCode = "404", description = "Entry was not found",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
	})
	@PutMapping("/entry")
	public ResponseEntity<?> updateUserEntry(@Valid @RequestBody EntryRequestDto entry) throws ResourceNotFoundException, InvalidPermissionsException, RateExceededException {
		Entry response = entryService.updateUserEntry(entry);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	// Delete an entry from a specific user
	@Operation(summary = "Delete an entry belonging to the current user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Entry was deleted.",
					content = {
							@Content(mediaType = "application/json",
									schema = @Schema(implementation = Entry.class)
							)
					}
			),
			@ApiResponse(responseCode = "400", description = "Bad Request",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
			@ApiResponse(responseCode = "403", description = "Invalid token",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
			@ApiResponse(responseCode = "403", description = "Rate limit exceeded",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
			@ApiResponse(responseCode = "404", description = "Entry was not found",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
	})
	@DeleteMapping("/entry")
	public ResponseEntity<?> deleteUserEntry(@Valid @RequestBody EntryRequestDto entry) throws InvalidPermissionsException, ResourceNotFoundException, RateExceededException{
		
		Entry response = entryService.deleteUserEntry(entry);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
	}
}
