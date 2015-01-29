package com.dupont.budget.dto;

import java.io.Serializable;

/**
 * @author bandrade
 *
 */
public class EmailDTO implements Serializable{
	private String from;
	private String to;
	private String subject;
	private String content;
	
	public EmailDTO() {
	}
	
	
	public EmailDTO(String from, String to, String subject, String content) {
		super();
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.content = content;
	}


	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	
}
