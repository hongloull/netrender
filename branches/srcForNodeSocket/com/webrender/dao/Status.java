package com.webrender.dao;

import java.util.HashSet;
import java.util.Set;

/**
 * Status entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Status implements java.io.Serializable {

	// Fields

	private Integer statusId;
	private String type;
	private String value;
	private Set nodegroups = new HashSet(0);
	private Set executelogs = new HashSet(0);
	private Set quests = new HashSet(0);
	private Set commandmodelargs = new HashSet(0);
	private Set commandmodels = new HashSet(0);
	private Set commands = new HashSet(0);
	private Set timegroups = new HashSet(0);
	private Set nodes = new HashSet(0);
	private Set regusers = new HashSet(0);

	// Constructors

	/** default constructor */
	public Status() {
	}

	/** minimal constructor */
	public Status(String type, String value) {
		this.type = type;
		this.value = value;
	}

	/** full constructor */
	public Status(String type, String value, Set nodegroups, Set executelogs,
			Set quests, Set commandmodelargs, Set commandmodels, Set commands,
			Set timegroups, Set nodes, Set regusers) {
		this.type = type;
		this.value = value;
		this.nodegroups = nodegroups;
		this.executelogs = executelogs;
		this.quests = quests;
		this.commandmodelargs = commandmodelargs;
		this.commandmodels = commandmodels;
		this.commands = commands;
		this.timegroups = timegroups;
		this.nodes = nodes;
		this.regusers = regusers;
	}

	// Property accessors

	public Integer getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Set getNodegroups() {
		return this.nodegroups;
	}

	public void setNodegroups(Set nodegroups) {
		this.nodegroups = nodegroups;
	}

	public Set getExecutelogs() {
		return this.executelogs;
	}

	public void setExecutelogs(Set executelogs) {
		this.executelogs = executelogs;
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

	public Set getCommandmodels() {
		return this.commandmodels;
	}

	public void setCommandmodels(Set commandmodels) {
		this.commandmodels = commandmodels;
	}

	public Set getCommands() {
		return this.commands;
	}

	public void setCommands(Set commands) {
		this.commands = commands;
	}

	public Set getTimegroups() {
		return this.timegroups;
	}

	public void setTimegroups(Set timegroups) {
		this.timegroups = timegroups;
	}

	public Set getNodes() {
		return this.nodes;
	}

	public void setNodes(Set nodes) {
		this.nodes = nodes;
	}

	public Set getRegusers() {
		return this.regusers;
	}

	public void setRegusers(Set regusers) {
		this.regusers = regusers;
	}

}