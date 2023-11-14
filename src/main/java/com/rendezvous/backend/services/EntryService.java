package com.rendezvous.backend.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rendezvous.backend.dtos.EntryRequestDto;
import com.rendezvous.backend.exceptions.InvalidPermissionsException;
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
	
	public boolean compareUserIds(Long userIdFromRequest) {
		
		 // Retrieve the authentication object from the security context
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Extract claims from the authentication object
        Long userIdFromToken = (Long) authentication.getPrincipal(); // Assumes the principal is the userId  
        
        return userIdFromToken == userIdFromRequest;
	}

	public Entry createUserEntry(Entry entry) throws ResourceNotFoundException, InvalidPermissionsException {
		
		if(!compareUserIds(entry.getUserId())) throw new InvalidPermissionsException();
		
		Optional<Prompt> promptFound = promptRepo.findById(entry.getPrompt().getId());
		if(promptFound.isEmpty()) throw new ResourceNotFoundException("prompt");
		
		Date currentDate = new Date();
		Timestamp timestamp = new Timestamp(currentDate.getTime());
		
		entry.setCreatedAt(timestamp);
		
		return entryRepo.save(entry);
	}

	public Entry getUserEntry(Long entryId, Long userId) throws ResourceNotFoundException, InvalidPermissionsException {
		
		if(!compareUserIds(userId)) throw new InvalidPermissionsException();
		
		Optional<Entry> found = entryRepo.findByIdAndUserId(entryId, userId);
		
		// Throw a custom exception here
		if(found.isEmpty()) {
			throw new ResourceNotFoundException("entry");
		}
		
		
		return found.get();
	}

	public List<Entry> getUserEntries(Long userId) throws Exception {
		
		if(!compareUserIds(userId)) throw new InvalidPermissionsException();
		
		List<Entry> found = entryRepo.findAllByUserId(userId);
		
		return found;
	}

	public Entry updateUserEntry(EntryRequestDto entry ) throws ResourceNotFoundException, InvalidPermissionsException {
		
		Long userIdFromRequest = entry.getUserId();
		Long entryIdFromRequest = entry.getEntryId();
		
		// Need to check the userId stored within the token to ensure the proper user is trying to update the entry
		if (!compareUserIds(userIdFromRequest)) throw new InvalidPermissionsException();
		
		Optional<Entry> found = entryRepo.findByIdAndUserId(entryIdFromRequest, userIdFromRequest);
		if(found.isEmpty()) throw new ResourceNotFoundException("entry");
		
		Entry userEntry = found.get();    
		userEntry.setContent(entry.getContent());;
		
		Entry saved = entryRepo.save(userEntry);
		
		return saved;
	}

	public Entry deleteUserEntry(EntryRequestDto entry) throws InvalidPermissionsException, ResourceNotFoundException {
		
		Long userIdFromRequest = entry.getUserId();
		Long entryIdFromRequest = entry.getEntryId();
		
		if(!compareUserIds(userIdFromRequest)) throw new InvalidPermissionsException();
		
		Optional<Entry> found = entryRepo.findByIdAndUserId(entryIdFromRequest, userIdFromRequest);
		if(found.isEmpty()) throw new ResourceNotFoundException("entry");
		
		Entry deleted = found.get();
		entryRepo.delete(deleted);
		
		
		return deleted;
	}

}
