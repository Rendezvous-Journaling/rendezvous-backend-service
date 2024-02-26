package com.rendezvous.backend.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
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
import com.rendezvous.backend.dtos.EntryRequestDto;
import com.rendezvous.backend.filter.JwtAuthenticationFilter;
import com.rendezvous.backend.models.Entry;
import com.rendezvous.backend.models.Prompt;
import com.rendezvous.backend.services.EntryService;
import com.rendezvous.backend.utilities.JwtUtil;

@WebMvcTest(controllers = EntryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EntryControllerTests {

	
	
private static final String STARTING_URI = "http://localhost:8080/api";
	
	@Autowired 
	MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	JwtUtil jwtUtil;
	
	@MockBean
	JwtAuthenticationFilter jwtFilter;
	
	@MockBean
	EntryService entryService;

	@InjectMocks
	EntryController entryController;
	
	
	@Test 
	public void testGetUserEntries() throws Exception {
		
		List<Entry> entries = new ArrayList<>();
		Prompt prompt = new Prompt(1L,"What are you grateful for today?");
		entries.add(new Entry(1L, "I like pie", 1L, prompt,  new Timestamp(0)));
		
		when(entryService.getUserEntries(1L)).thenReturn(entries);
		
		mvc.perform(get(STARTING_URI + "/entries/1"))
			.andDo(print())
			.andExpect(status().isOk());
		
		verify(entryService, times(1)).getUserEntries(1L);
//		verifyNoInteractions(entryService);
	}
	
	@Test
	public void testGetUserEntry() throws Exception {
		Prompt prompt = new Prompt(1L,"What are you grateful for today?");
		Entry entry = new Entry(1L, "I like pie", 1L, prompt,  new Timestamp(0));
		
		when(entryService.getUserEntry(1L, 1L)).thenReturn(entry);
		
		mvc.perform(get(STARTING_URI + "/entry/1/user/1"))
			.andDo(print())
			.andExpect(status().isOk());
		
		verify(entryService, times(1)).getUserEntry(1L, 1L);
//		verifyNoInteractions(entryService);
	}
	
	@Test
	public void testCreateUserEntry() throws Exception {
		Prompt prompt = new Prompt(1L,"What are you grateful for today?");
		Entry entry = new Entry(1L, "I like pie", 1L, prompt,  new Timestamp(0));
		
		when(entryService.createUserEntry(Mockito.any(Entry.class))).thenReturn(entry);
		
		mvc.perform(post(STARTING_URI + "/entry")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(entry)))
			.andDo(print())
			.andExpect(status().isCreated());
		
		verify(entryService, times(1)).createUserEntry(Mockito.any(Entry.class));
	}
	
	@Test
	public void testUpdateUserEntry() throws Exception {
		Prompt prompt = new Prompt(1L,"What are you grateful for today?");
		EntryRequestDto entryRequest = new EntryRequestDto(1L, 1L, "I like updating");
		Entry entry = new Entry(1L, "I like pie", 1L, prompt,  new Timestamp(0));
		Entry updatedEntry = new Entry(1L, "I like updating", 1L, prompt, entry.getCreatedAt());
		
		when(entryService.updateUserEntry(Mockito.any(EntryRequestDto.class))).thenReturn(updatedEntry);
		
		mvc.perform(put(STARTING_URI + "/entry")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(entryRequest)))
		.andDo(print())
		.andExpect(status().isCreated());
		
		verify(entryService, times(1)).updateUserEntry(Mockito.any(EntryRequestDto.class));
	}
	
	@Test
	public void testDeleteUserEntry() throws Exception {
		Prompt prompt = new Prompt(1L,"What are you grateful for today?");
		EntryRequestDto entryRequest = new EntryRequestDto(1L, 1L, "");
		Entry entry = new Entry(1L, "I like pie", 1L, prompt,  new Timestamp(0));
		Entry deletedEntry = new Entry(1L, "I like updating", 1L, prompt, entry.getCreatedAt());
		
		when(entryService.deleteUserEntry(Mockito.any(EntryRequestDto.class))).thenReturn(deletedEntry);
		
		mvc.perform(delete(STARTING_URI + "/entry")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(entryRequest)))
		.andDo(print())
		.andExpect(status().isNoContent());
		
		verify(entryService, times(1)).deleteUserEntry(Mockito.any(EntryRequestDto.class));
	}
}
