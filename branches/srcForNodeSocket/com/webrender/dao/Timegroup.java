package com.webrender.dao;

import java.util.HashSet;
import java.util.Set;

/**
 * Timegroup entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Timegroup implements java.io.Serializable {

	// Fields

	private Integer timeGroupId;
	private Status status;
	private String timeGroupName;
	private String timeValue;
	private Set nodegroups = new HashSet(0);

	// Constructors

	/** default constructor */
	public Timegroup() {
	}

	/** minimal constructor */
	public Timegroup(String timeGroupName, String timeValue) {
		this.timeGroupName = timeGroupName;
		this.timeValue = timeValue;
	}

	/** full constructor */
	public Timegroup(Status status, String timeGroupName, String timeValue,
			Set nodegroups) {
		this.status = status;
		this.timeGroupName = timeGroupName;
		this.timeValue = timeValue;
		this.nodegroups = nodegroups;
	}

	// Property accessors

	public Integer getTimeGroupId() {
		return this.timeGroupId;
	}

	public void setTimeGroupId(Integer timeGroupId) {
		this.timeGroupId = timeGroupId;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTimeGroupName() {
		return this.timeGroupName;
	}

	public void setTimeGroupName(String timeGroupName) {
		this.timeGroupName = timeGroupName;
	}

	public String getTimeValue() {
		return this.timeValue;
	}

	public void setTimeValue(String timeValue) {
		this.timeValue = timeValue;
	}

	public Set getNodegroups() {
		return this.nodegroups;
	}

	public void setNodegroups(Set nodegroups) {
		this.nodegroups = nodegroups;
	}

}