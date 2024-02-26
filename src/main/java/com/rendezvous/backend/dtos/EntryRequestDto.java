package com.rendezvous.backend.dtos;

import jakarta.validation.constraints.NotNull;

public class EntryRequestDto {
	
	@NotNull
	private Long entryId;
	@NotNull
	private Long userId;
	private String content;

	public EntryRequestDto(){}

	public EntryRequestDto(Long entryId, Long userId, String content) {
		super();
		this.entryId = entryId;
		this.userId = userId;
		this.content = content;
	}

	public Long getEntryId() {
		return entryId;
	}

	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "EntryRequestDto [entryId=" + entryId + ", userId=" + userId + ", content=" + content + "]";
	}
	
	
	
	
	
	
}
