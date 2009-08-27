package com.webrender.dao;

/**
 * Commandarg entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Commandarg implements java.io.Serializable {

	// Fields

	private Integer commandArgId;
	private Command command;
	private Commandmodelarg commandmodelarg;
	private String value;

	// Constructors

	/** default constructor */
	public Commandarg() {
	}

	/** full constructor */
	public Commandarg(Command command, Commandmodelarg commandmodelarg,
			String value) {
		this.command = command;
		this.commandmodelarg = commandmodelarg;
		this.value = value;
	}

	// Property accessors

	public Integer getCommandArgId() {
		return this.commandArgId;
	}

	public void setCommandArgId(Integer commandArgId) {
		this.commandArgId = commandArgId;
	}

	public Command getCommand() {
		return this.command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public Commandmodelarg getCommandmodelarg() {
		return this.commandmodelarg;
	}

	public void setCommandmodelarg(Commandmodelarg commandmodelarg) {
		this.commandmodelarg = commandmodelarg;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}