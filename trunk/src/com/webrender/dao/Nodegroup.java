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
	private String nodeGroupName;
	private Set nodes = new HashSet(0);
	private Set rolenodetimes = new HashSet(0);

	// Constructors

	/** default constructor */
	public Nodegroup() {
	}

	/** minimal constructor */
	public Nodegroup(String nodeGroupName) {
		this.nodeGroupName = nodeGroupName;
	}

	/** full constructor */
	public Nodegroup(Status status, String nodeGroupName, Set nodes,
			Set rolenodetimes) {
		this.status = status;
		this.nodeGroupName = nodeGroupName;
		this.nodes = nodes;
		this.rolenodetimes = rolenodetimes;
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

	public String getNodeGroupName() {
		return this.nodeGroupName;
	}

	public void setNodeGroupName(String nodeGroupName) {
		this.nodeGroupName = nodeGroupName;
	}

	public Set getRolenodetimes() {
		return this.rolenodetimes;
	}

	public void setRolenodetimes(Set rolenodetimes) {
		this.rolenodetimes = rolenodetimes;
	}

	public Set getNodes() {
		return nodes;
	}

	public void setNodes(Set nodes) {
		this.nodes = nodes;
	}

}