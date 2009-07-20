package com.webrender.dao;

import java.util.HashSet;
import java.util.Set;

/**
 * Right entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Right implements java.io.Serializable {

	// Fields

	private Integer rightId;
	private String instruction;
	private Set regusers = new HashSet(0);

	// Constructors

	/** default constructor */
	public Right() {
	}

	/** minimal constructor */
	public Right(String instruction) {
		this.instruction = instruction;
	}

	/** full constructor */
	public Right(String instruction, Set reguser) {
		this.instruction = instruction;
		this.regusers = regusers;
	}

	// Property accessors

	public Integer getRightId() {
		return this.rightId;
	}

	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}

	public String getInstruction() {
		return this.instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public Set getRegusers() {
		return this.regusers;
	}

	public void setRegusers(Set regusers) {
		this.regusers = regusers;
	}

}