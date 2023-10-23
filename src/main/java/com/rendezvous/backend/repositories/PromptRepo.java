package com.rendezvous.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rendezvous.backend.models.Prompt;

public interface PromptRepo extends JpaRepository<Prompt, Long>{

}
