package com.rendezvous.backend.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rendezvous.backend.models.Prompt;
import com.rendezvous.backend.services.PromptService;

@WebMvcTest(controllers = PromptController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PromptControllerTests {
	
	private static final String STARTING_URI = "http://localhost:8080/api";
	
	@Autowired 
	MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	PromptService promptService;

	@InjectMocks
	PromptController promptController;
	
	
	@Test
	public void getAllPromptsTest() throws Exception {
		
		List<Prompt> prompts = new ArrayList<>();
		prompts.add(new Prompt(1L,"What are you grateful for today?"));
		
		
		when(promptService.getAllPrompts()).thenReturn(prompts);
		
		mvc.perform(get(STARTING_URI + "/prompt"))
			.andDo(print())
			.andExpect(status().isOk());
		
		verify(promptService, times(1)).getAllPrompts();
		verifyNoMoreInteractions(promptService);
		
		
	}
	
	@Test
	public void testCreatePrompt() throws Exception{
		
		// Arrange
		Prompt mockRequest = new Prompt(1L,"What are you grateful for today?");
		String uri = STARTING_URI + "/prompt";
		
		when(promptService.createPrompt(Mockito.any(Prompt.class))).thenReturn(mockRequest);
		
		mvc.perform(post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(objectMapper.writeValueAsString(mockRequest)))
				.andDo(print())
				.andExpect(status().isCreated());
				
		verify(promptService,times(1)).createPrompt(Mockito.any(Prompt.class));
		verifyNoMoreInteractions(promptService);
	}
	
}
