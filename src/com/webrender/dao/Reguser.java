package com.webrender.dao;

import java.util.HashSet;
import java.util.Set;

/**
 * Reguser entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Reguser implements java.io.Serializable {

	// Fields

	private Integer regUserId;
	private Status status;
	private Role role;
	private String regName;
	private String passWord;
	private Set quests = new HashSet(0);
	private Set operatelogs = new HashSet(0);

	// Constructors

	/** default constructor */
	public Reguser() {
	}

	/** minimal constructor */
	public Reguser(String regName, String passWord) {
		this.regName = regName;
		this.passWord = passWord;
	}

	/** full constructor */
	public Reguser(Status status, Role role, String regName, String passWord,
			Set quests, Set operatelogs) {
		this.status = status;
		this.role = role;
		this.regName = regName;
		this.passWord = passWord;
		this.quests = quests;
		this.operatelogs = operatelogs;
	}

	// Property accessors

	public Integer getRegUserId() {
		return this.regUserId;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getRegName() {
		return this.regName;
	}

	public void setRegName(String regName) {
		this.regName = regName;
	}

	public String getPassWord() {
		return this.passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public Set getQuests() {
		return this.quests;
	}

	public void setQuests(Set quests) {
		this.quests = quests;
	}

	public Set getOperatelogs() {
		return this.operatelogs;
	}

	public void setOperatelogs(Set operatelogs) {
		this.operatelogs = operatelogs;
	}

}