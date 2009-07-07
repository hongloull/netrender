package com.webrender.dao;

/**
 * Rolenodetime entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Rolenodetime implements java.io.Serializable {

	// Fields

	private Integer roleNodeTimeId;
	private Nodegroup nodegroup;
	private Timegroup timegroup;
	private Role role;

	// Constructors

	/** default constructor */
	public Rolenodetime() {
	}

	/** full constructor */
	public Rolenodetime(Nodegroup nodegroup, Timegroup timegroup, Role role) {
		this.nodegroup = nodegroup;
		this.timegroup = timegroup;
		this.role = role;
	}

	// Property accessors

	public Integer getRoleNodeTimeId() {
		return this.roleNodeTimeId;
	}

	public void setRoleNodeTimeId(Integer roleNodeTimeId) {
		this.roleNodeTimeId = roleNodeTimeId;
	}

	public Nodegroup getNodegroup() {
		return this.nodegroup;
	}

	public void setNodegroup(Nodegroup nodegroup) {
		this.nodegroup = nodegroup;
	}

	public Timegroup getTimegroup() {
		return this.timegroup;
	}

	public void setTimegroup(Timegroup timegroup) {
		this.timegroup = timegroup;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}