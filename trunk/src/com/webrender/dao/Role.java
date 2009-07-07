package com.webrender.dao;

import java.util.HashSet;
import java.util.Set;

/**
 * Role entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Role implements java.io.Serializable {

	// Fields

	private Integer roleId;
	private Status status;
	private String roleName;
	private Short highestPri;
	private Set rolenodetimes = new HashSet(0);
	private Set regusers = new HashSet(0);
	private Set rights = new HashSet(0);

	// Constructors

	/** default constructor */
	public Role() {
	}

	/** minimal constructor */
	public Role(String roleName) {
		this.roleName = roleName;
	}

	/** full constructor */
	public Role(Status status, String roleName, Short highestPri,
			Set rolenodetimes, Set regusers, Set rights) {
		this.status = status;
		this.roleName = roleName;
		this.highestPri = highestPri;
		this.rolenodetimes = rolenodetimes;
		this.regusers = regusers;
		this.rights = rights;
	}

	// Property accessors

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Short getHighestPri() {
		return this.highestPri;
	}

	public void setHighestPri(Short highestPri) {
		this.highestPri = highestPri;
	}

	public Set getRolenodetimes() {
		return this.rolenodetimes;
	}

	public void setRolenodetimes(Set rolenodetimes) {
		this.rolenodetimes = rolenodetimes;
	}

	public Set getRegusers() {
		return this.regusers;
	}

	public void setRegusers(Set regusers) {
		this.regusers = regusers;
	}

	public Set getRights() {
		return this.rights;
	}

	public void setRights(Set rights) {
		this.rights = rights;
	}

}