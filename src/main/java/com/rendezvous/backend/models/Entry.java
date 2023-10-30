package com.rendezvous.backend.models;

import java.sql.Timestamp;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Entry {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String content;
	
	@NotNull
	private Long userId;
	
	@ManyToOne
	@JoinColumn(name = "prompt_id")
	@NotNull
	private Prompt prompt;
	
	
	private Timestamp createdAt;

	public Entry() {}

	public Entry(Long id, @NotBlank String content, @NotNull Long userId, @NotNull Prompt prompt, Timestamp createdAt) {
		super();
		this.id = id;
		this.content = content;
		this.userId = userId;
		this.prompt = prompt;
		this.createdAt = createdAt;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Prompt getPrompt() {
		return prompt;
	}


	public void setPrompt(Prompt prompt) {
		this.prompt = prompt;
	}


	public Timestamp getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}


	@Override
	public String toString() {
		return "Entry [id=" + id + ", content=" + content + ", userId=" + userId + ", prompt=" + prompt + ", createdAt="
				+ createdAt + "]";
	}


	@Override
	public int hashCode() {
		return Objects.hash(content, createdAt, id, prompt, userId);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entry other = (Entry) obj;
		return Objects.equals(content, other.content) && Objects.equals(createdAt, other.createdAt)
				&& Objects.equals(id, other.id) && Objects.equals(prompt, other.prompt)
				&& Objects.equals(userId, other.userId);
	}
}
	
	