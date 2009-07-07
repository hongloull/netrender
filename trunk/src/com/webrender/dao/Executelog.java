package com.webrender.dao;


import java.util.Date;

/**
 * Executelog entity. @author MyEclipse Persistence Tools
 */

public class Executelog implements java.io.Serializable {

	// Fields

	private Integer executeLogId;
	private Command command;
	private Status status;
	private Node node;
	private String note;
	private Date logTime;

	// Constructors

	/** default constructor */
	public Executelog() {
	}

	/** minimal constructor */
	public Executelog(Status status, Node node, Date logTime) {
		this.status = status;
		this.node = node;
		this.logTime = logTime;
	}

	/** full constructor */
	public Executelog(Command command, Status status, Node node, String note,
			Date logTime) {
		this.command = command;
		this.status = status;
		this.node = node;
		this.note = note;
		this.logTime = logTime;
	}

	// Property accessors

	public Integer getExecuteLogId() {
		return this.executeLogId;
	}

	public void setExecuteLogId(Integer executeLogId) {
		this.executeLogId = executeLogId;
	}

	public Command getCommand() {
		return this.command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Node getNode() {
		return this.node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getLogTime() {
		return this.logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

}
