package com.webrender.dao;

import java.util.HashSet;
import java.util.Set;

/**
 * Nodegroup entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Nodegroup implements java.io.Serializable {

	// Fields

	private Integer nodeGroupId;
	private Status status;
	private Timegroup timegroup;
	private String nodeGroupName;
	private Set nodes = new HashSet(0);
	private Set regusers = new HashSet(0);

	// Constructors

	/** default constructor */
	public Nodegroup() {
	}

	/** minimal constructor */
	public Nodegroup(String nodeGroupName) {
		this.nodeGroupName = nodeGroupName;
	}

	/** full constructor */
	public Nodegroup(Status status, Timegroup timegroup, String nodeGroupName,
			Set regusers, Set nodes) {
		this.status = status;
		this.timegroup = timegroup;
		this.nodeGroupName = nodeGroupName;
		this.regusers = regusers;
		this.nodes = nodes;
	}

	// Property accessors

	public Integer getNodeGroupId() {
		return this.nodeGroupId;
	}

	public void setNodeGroupId(Integer nodeGroupId) {
		this.nodeGroupId = nodeGroupId;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Timegroup getTimegroup() {
		return this.timegroup;
	}

	public void setTimegroup(Timegroup timegroup) {
		this.timegroup = timegroup;
	}

	public String getNodeGroupName() {
		return this.nodeGroupName;
	}

	public void setNodeGroupName(String nodeGroupName) {
		this.nodeGroupName = nodeGroupName;
	}

	public Set getRegusers() {
		return this.regusers;
	}

	public void setRegusers(Set regusers) {
		this.regusers = regusers;
	}

	public Set getNodes() {
		return this.nodes;
	}

	public void setNodes(Set nodes) {
		this.nodes = nodes;
	}

}