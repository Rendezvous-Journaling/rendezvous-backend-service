package com.rendezvous.backend.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.RateLimiter;
import com.rendezvous.backend.dtos.EntryRequestDto;
import com.rendezvous.backend.exceptions.InvalidPermissionsException;
import com.rendezvous.backend.exceptions.RateExceededException;
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
	
	private final RateLimiter rateLimiter = RateLimiter.create(50);
	
	private static final Logger logger = LoggerFactory.getLogger(EntryService.class);
	
	public boolean compareUserIds(Long userIdFromRequest) {
		
		 // Retrieve the authentication object from the security context
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Extract claims from the authentication object
        Long userIdFromToken = (Long) authentication.getPrincipal(); // Assumes the principal is the userId  
        
        logger.info("Authentication={}, userIdFromToken={}, userIdFromRequest={}", authentication, userIdFromToken, userIdFromRequest);
        logger.debug("Comparing user IDs: Token={}, Request={}", userIdFromToken, userIdFromRequest);
        
        return userIdFromToken == userIdFromRequest;
	}

	public Entry createUserEntry(Entry entry) throws ResourceNotFoundException, InvalidPermissionsException, RateExceededException {
		
		Long userId = entry.getUserId();
		Long promptId = entry.getPrompt().getId();
		
		// If the entry trying to be created does not belong to the correct user
		if(!compareUserIds(userId)) {
			logger.error("createUserEntry: Invalid permissions to view entries of userId={}", userId);
			throw new InvalidPermissionsException();
		}
		if(!rateLimiter.tryAcquire()) { 
			logger.error("createUserEntry: userId={} has exceeded current rate limit", userId);
			throw new RateExceededException();
		}
		
		// If the prompt that the user is trying to make an entry for does not exists, throw an exception
		Optional<Prompt> promptFound = promptRepo.findById(promptId);
		if(promptFound.isEmpty()) {
			logger.warn("createUserEntry: promptId={} could not be found for userId={}", promptId, userId);
			throw new ResourceNotFoundException("prompt");
		}
		
		Date currentDate = new Date();
		Timestamp timestamp = new Timestamp(currentDate.getTime());
		
		entry.setCreatedAt(timestamp);
		
		return entryRepo.save(entry);
	}

	public Entry getUserEntry(Long entryId, Long userId) throws ResourceNotFoundException, InvalidPermissionsException, RateExceededException {
				
		if(!compareUserIds(userId)) {
			logger.error("getUserEntry: Invalid permissions to view entries of userId={}", userId);
			throw new InvalidPermissionsException();
		}
		if(!rateLimiter.tryAcquire()) {
			logger.error("getUserEntry: userId={} has exceeded current rate limit", userId);
			throw new RateExceededException();
		}
		
		Optional<Entry> found = entryRepo.findByIdAndUserId(entryId, userId);
		
		// Throw a custom exception here if the entry cannot be found
		if(found.isEmpty()) {
			logger.warn("entryId={} could not be found for userId={}", entryId, userId);
			throw new ResourceNotFoundException("entry");
		}
		
		
		return found.get();
	}

	public List<Entry> getUserEntries(Long userId) throws Exception {
				
		if(!compareUserIds(userId)) { 
			logger.error("getUserEntries: Invalid permissions to view entries of userId={}", userId);
			throw new InvalidPermissionsException();
		}
		if(!rateLimiter.tryAcquire()) { 
			logger.error("getUserEntries: userId={} has exceeded current rate limit", userId);
			throw new RateExceededException();
		}
		
		List<Entry> found = entryRepo.findAllByUserId(userId);
		
		return found;
	}

	public Entry updateUserEntry(EntryRequestDto entry ) throws ResourceNotFoundException, InvalidPermissionsException, RateExceededException {
		
		Long userIdFromRequest = entry.getUserId();
		Long entryIdFromRequest = entry.getEntryId();
		
		// Need to check the userId stored within the token to ensure the proper user is trying to update the entry
		if (!compareUserIds(userIdFromRequest)) {
			logger.error("updateUserEntry: Invalid permissions to view entries of userId={}", userIdFromRequest);
			throw new InvalidPermissionsException();
		}
		if(!rateLimiter.tryAcquire()) {
			logger.error("updateUserEntry: userId={} has exceeded current rate limit", userIdFromRequest);
			throw new RateExceededException();
		}
		
		Optional<Entry> found = entryRepo.findByIdAndUserId(entryIdFromRequest, userIdFromRequest);
		if(found.isEmpty()) {
			logger.warn("updateUserEntry: entryId={} could not be found for userId={}", entryIdFromRequest, userIdFromRequest);
			throw new ResourceNotFoundException("entry");
		}
		
		Entry userEntry = found.get();    
		userEntry.setContent(entry.getContent());;
		
		Entry saved = entryRepo.save(userEntry);
		
		return saved;
	}

	public Entry deleteUserEntry(EntryRequestDto entry) throws InvalidPermissionsException, ResourceNotFoundException, RateExceededException {
		
		Long userIdFromRequest = entry.getUserId();
		Long entryIdFromRequest = entry.getEntryId();
		
		if(!compareUserIds(userIdFromRequest)) {
			logger.error("deleteUserEntry: Invalid permissions to delete entryId={} of userId={}", entryIdFromRequest, userIdFromRequest);
			throw new InvalidPermissionsException();
		}
		if(!rateLimiter.tryAcquire()) {
			logger.error("deleteUserEntry: userId={} has exceeded current rate limit", userIdFromRequest );
			throw new RateExceededException();
		}
		
		Optional<Entry> found = entryRepo.findByIdAndUserId(entryIdFromRequest, userIdFromRequest);
		if(found.isEmpty()) {
			logger.warn("deleteUserEntry: entryId={} could not be found for userId={}", entryIdFromRequest, userIdFromRequest);
			throw new ResourceNotFoundException("entry");
		}
		
		Entry deleted = found.get();
		entryRepo.delete(deleted);
		
		
		return deleted;
	}

}
