package com.webrender.dao.view;

/**
 * Commandnode entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Commandnode implements java.io.Serializable {

	// Fields

	private CommandnodeId id;

	// Constructors

	/** default constructor */
	public Commandnode() {
	}

	/** full constructor */
	public Commandnode(CommandnodeId id) {
		this.id = id;
	}

	// Property accessors

	public CommandnodeId getId() {
		return this.id;
	}

	public void setId(CommandnodeId id) {
		this.id = id;
	}
	
}