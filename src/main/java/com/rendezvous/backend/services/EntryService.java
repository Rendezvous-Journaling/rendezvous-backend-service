package com.rendezvous.backend.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rendezvous.backend.exceptions.ResourceNotFoundException;
import com.rendezvous.backend.models.Entry;
import com.rendezvous.backend.models.Prompt;
import com.rendezvous.backend.repositories.EntryRepo;
import com.rendezvous.backend.repositories.PromptRepo;

@Service
public class EntryService {
	
	@Autowired
	EntryRepo entryRepo;
	
	@Autowired 
	PromptRepo promptRepo;

	public Entry createUserEntry(Entry entry) throws ResourceNotFoundException {
		
		Optional<Prompt> promptFound = promptRepo.findById(entry.getPrompt().getId());
		
		if(promptFound.isEmpty()) {
			throw new ResourceNotFoundException("prompt");
		}
		
		Date currentDate = new Date();
		
		Timestamp timestamp = new Timestamp(currentDate.getTime());
		
		entry.setCreatedAt(timestamp);
		
		return entryRepo.save(entry);
	}

	public Entry getUserEntry(Long entryId, Long userId) throws ResourceNotFoundException {
		
		Optional<Entry> found = entryRepo.findByIdAndUserId(entryId, userId);
		
		// Throw a custom exception here
		if(found.isEmpty()) {
			throw new ResourceNotFoundException("entry");
		}
		
		
		return found.get();
	}

	public List<Entry> getUserEntries(Long userId) {
		
		List<Entry> found = entryRepo.findAllByUserId(userId);
		
		return found;
	}

}
