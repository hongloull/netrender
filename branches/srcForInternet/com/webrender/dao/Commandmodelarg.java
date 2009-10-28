package com.webrender.dao;

import java.util.HashSet;
import java.util.Set;

/**
 * Commandmodelarg entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Commandmodelarg implements java.io.Serializable {

	// Fields

	private Integer commandModelArgId;
	private Status status;
	private Commandmodel commandmodel;
	private String argName;
	private String argInstruction;
	private String value; 
	private Short type;
	private Set commandargs = new HashSet(0);
	private Set questargs = new HashSet(0);

	// Constructors

	/** default constructor */
	public Commandmodelarg() {
	}

	/** minimal constructor */
	public Commandmodelarg(Commandmodel commandmodel, String argName) {
		this.commandmodel = commandmodel;
		this.argName = argName;
	}

	/** full constructor */
	public Commandmodelarg(Status status, Commandmodel commandmodel,
			String argName, String argInstruction, Short type, Set commandargs,
			Set questargs) {
		this.status = status;
		this.commandmodel = commandmodel;
		this.argName = argName;
		this.argInstruction = argInstruction;
		this.type = type;
		this.commandargs = commandargs;
		this.questargs = questargs;
	}

	// Property accessors

	public Integer getCommandModelArgId() {
		return this.commandModelArgId;
	}

	public void setCommandModelArgId(Integer commandModelArgId) {
		this.commandModelArgId = commandModelArgId;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Commandmodel getCommandmodel() {
		return this.commandmodel;
	}

	public void setCommandmodel(Commandmodel commandmodel) {
		this.commandmodel = commandmodel;
	}

	public String getArgName() {
		return this.argName;
	}

	public void setArgName(String argName) {
		this.argName = argName;
	}

	public String getArgInstruction() {
		return this.argInstruction;
	}

	public void setArgInstruction(String argInstruction) {
		this.argInstruction = argInstruction;
	}

	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public Set getCommandargs() {
		return this.commandargs;
	}

	public void setCommandargs(Set commandargs) {
		this.commandargs = commandargs;
	}

	public Set getQuestargs() {
		return this.questargs;
	}

	public void setQuestargs(Set questargs) {
		this.questargs = questargs;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}