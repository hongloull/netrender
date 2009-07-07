package com.webrender.dao;

import java.util.HashSet;
import java.util.Set;

/**
 * Commandmodel entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Commandmodel implements java.io.Serializable {

	// Fields

	private Integer commandModelId;
	private Status status;
	private String commandModelName;
	private String description;
	private Set quests = new HashSet(0);
	private Set commandmodelargs = new HashSet(0);

	// Constructors

	/** default constructor */
	public Commandmodel() {
	}

	/** minimal constructor */
	public Commandmodel( String commandModelName) {
		this.commandModelName = commandModelName;
	}

	/** full constructor */
	public Commandmodel(Status status,
			String commandModelName, String description, Set quests,
			Set commandmodelargs) {
		this.status = status;
		this.commandModelName = commandModelName;
		this.description = description;
		this.quests = quests;
		this.commandmodelargs = commandmodelargs;
	}

	// Property accessors

	public Integer getCommandModelId() {
		return this.commandModelId;
	}

	public void setCommandModelId(Integer commandModelId) {
		this.commandModelId = commandModelId;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	public String getCommandModelName() {
		return this.commandModelName;
	}

	public void setCommandModelName(String commandModelName) {
		this.commandModelName = commandModelName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set getQuests() {
		return this.quests;
	}

	public void setQuests(Set quests) {
		this.quests = quests;
	}

	public Set getCommandmodelargs() {
		return this.commandmodelargs;
	}

	public void setCommandmodelargs(Set commandmodelargs) {
		this.commandmodelargs = commandmodelargs;
	}

}