package com.rendezvous.backend.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rendezvous.backend.models.Entry;
import com.rendezvous.backend.repositories.EntryRepo;

@Service
public class EntryService {
	
	@Autowired
	EntryRepo entryRepo;

	public Entry createUserEntry(Entry entry) {
		
		Date currentDate = new Date();
		
		Timestamp timestamp = new Timestamp(currentDate.getTime());
		
		entry.setCreatedAt(timestamp);
		
		return entryRepo.save(entry);
	}

	public Entry getUserEntry(Long entryId, Long userId) {
		
		Optional<Entry> found = entryRepo.findByIdAndUserId(entryId, userId);
		
		// Throw a custom exception here
		if(found.isEmpty()) {
			return null;
		}
		
		
		return found.get();
	}

	public List<Entry> getUserEntries(Long userId) {
		
		List<Entry> found = entryRepo.findAllByUserId(userId);
		
		return found;
	}

}
