package com.webrender.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Command entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Command implements java.io.Serializable {

	// Fields

	private Integer commandId;
	private Status status;
	private Quest quest;
	private Node node;
	private String command;
	private Date sendTime;
	private Set commandargs = new HashSet(0);
	private Set executelogs = new HashSet(0);

	// Constructors

	/** default constructor */
	public Command() {
	}

	/** minimal constructor */
	public Command(Quest quest) {
		this.quest = quest;
	}

	/** full constructor */
	public Command(Status status, Quest quest, Node node, String command,
			Date sendTime, Set commandargs, Set executelogs) {
		this.status = status;
		this.quest = quest;
		this.node = node;
		this.command = command;
		this.sendTime = sendTime;
		this.commandargs = commandargs;
		this.executelogs = executelogs;
	}

	// Property accessors

	public Integer getCommandId() {
		return this.commandId;
	}

	public void setCommandId(Integer commandId) {
		this.commandId = commandId;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Quest getQuest() {
		return this.quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
	}

	public Node getNode() {
		return this.node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Set getCommandargs() {
		return this.commandargs;
	}

	public void setCommandargs(Set commandargs) {
		this.commandargs = commandargs;
	}

	public Set getExecutelogs() {
		return this.executelogs;
	}

	public void setExecutelogs(Set executelogs) {
		this.executelogs = executelogs;
	}

}