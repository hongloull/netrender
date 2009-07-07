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
	private String timeGroupName;
	private String timeValue;
	private Integer statusId;
	private Set rolenodetimes = new HashSet(0);

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
	public Timegroup(String timeGroupName, String timeValue, Integer statusId,
			Set rolenodetimes) {
		this.timeGroupName = timeGroupName;
		this.timeValue = timeValue;
		this.statusId = statusId;
		this.rolenodetimes = rolenodetimes;
	}

	// Property accessors

	public Integer getTimeGroupId() {
		return this.timeGroupId;
	}

	public void setTimeGroupId(Integer timeGroupId) {
		this.timeGroupId = timeGroupId;
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

	public Integer getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Set getRolenodetimes() {
		return this.rolenodetimes;
	}

	public void setRolenodetimes(Set rolenodetimes) {
		this.rolenodetimes = rolenodetimes;
	}

}