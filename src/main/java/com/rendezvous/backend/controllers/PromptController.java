package com.rendezvous.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rendezvous.backend.exceptions.ErrorDetails;
import com.rendezvous.backend.exceptions.RateExceededException;
import com.rendezvous.backend.models.Prompt;
import com.rendezvous.backend.services.PromptService;

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
@Tag(name = "Prompt Controller", description = "Endpoints for managing prompts")
public class PromptController {
	
	@Autowired
	private PromptService promptService;
	
	@Operation(summary = "Get a list of all the prompts.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The list of prompts were retrieved. The list can be empty", 
					content = {
							@Content(mediaType = "application/json", 
									array = @ArraySchema(schema = @Schema(implementation = Prompt.class))
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
	@GetMapping("/prompts")
	public ResponseEntity<?> getAllPrompts() throws RateExceededException {
		
		List<Prompt> response = promptService.getAllPrompts();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	
	@Operation(summary = "Get a random prompt.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A random prompt was successfully retrieved.",
					content = {
							@Content(mediaType = "application/json", 
									schema = @Schema(implementation = Prompt.class)
							)}
			),
			@ApiResponse(responseCode = "400", description = "Bad Request",
			content = {
					@Content(mediaType = "application/json", 
							schema = @Schema(implementation = ErrorDetails.class)
					)}
			),
			@ApiResponse(responseCode = "404", description = "Prompt not found",
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
	@GetMapping("/prompt")
	public ResponseEntity<?> getRandomPrompt() throws Exception{
		
		Prompt response = promptService.getRandomPrompt();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@Operation(summary = "Create a prompt.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Prompt was created.",
					content = {
							@Content(mediaType = "application/json",
									schema = @Schema(implementation = Prompt.class)
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
	@PostMapping("/prompt")
	public ResponseEntity<?> createPrompt(@RequestBody @Valid Prompt prompt) throws RateExceededException{
		
		Prompt response = promptService.createPrompt(prompt);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@Operation(summary = "Create many prompts")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Prompts were created.",
					content = {
							@Content(mediaType = "application/json",
									schema = @Schema(implementation = Prompt.class)
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
	@PostMapping ("/prompts")
	public ResponseEntity<?> createManyPrompts(@RequestBody @Valid List<Prompt> prompts) throws RateExceededException{
		
		List<Prompt> response = promptService.createManyPrompts(prompts);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
