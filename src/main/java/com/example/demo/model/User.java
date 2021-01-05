package com.example.demo.model;

public class User {

	private String id;

	private String userName;

	private String emailId;
	
	private String threadName;

	public User() {
		super();
	}

	public User(String id, String userName, String emailId, String threadName) {
		super();
		this.id = id;
		this.userName = userName;
		this.emailId = emailId;
		this.threadName = threadName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

}
