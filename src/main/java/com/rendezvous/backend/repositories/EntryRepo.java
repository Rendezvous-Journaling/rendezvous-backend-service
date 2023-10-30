package com.rendezvous.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rendezvous.backend.models.Entry;

@Repository
public interface EntryRepo extends JpaRepository<Entry, Long> {
	
	
	public Optional<Entry> findByIdAndUserId(Long entryId, Long userId);

	public List<Entry> findAllByUserId(Long userId);
}
