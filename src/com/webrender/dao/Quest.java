package com.webrender.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Quest entity. @author MyEclipse Persistence Tools
 */

public class Quest implements java.io.Serializable {

	// Fields

	private Integer questId;
	private Status status;
	private Commandmodel commandmodel;
	private Nodegroup nodegroup;
	private Reguser reguser;
	private String questName;
	private String information;
	private Short pri;
	private Date commitTime;
	private Integer maxNodes;
	private Short packetSize;
	private Set commands = new HashSet(0);
	private Set questargs = new HashSet(0);

	// Constructors

	/** default constructor */
	public Quest() {
	}

	/** minimal constructor */
	public Quest(Commandmodel commandmodel, Reguser reguser, String questName,
			Short pri, Date commitTime) {
		this.commandmodel = commandmodel;
		this.reguser = reguser;
		this.questName = questName;
		this.pri = pri;
		this.commitTime = commitTime;
	}

	/** full constructor */
	public Quest(Status status, Commandmodel commandmodel, Reguser reguser,
			String questName, String information, Short pri,
			Date commitTime, Integer maxNodes, Short packetSize,
			Set commands, Set questargs) {
		this.status = status;
		this.commandmodel = commandmodel;
		this.reguser = reguser;
		this.questName = questName;
		this.information = information;
		this.pri = pri;
		this.commitTime = commitTime;
		this.maxNodes = maxNodes;
		this.packetSize = packetSize;
		this.commands = commands;
		this.questargs = questargs;
	}

	// Property accessors

	public Integer getQuestId() {
		return this.questId;
	}

	public void setQuestId(Integer questId) {
		this.questId = questId;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Commandmodel getCommandmodel() {
		return this.commandmodel;
	}

	public void setCommandmodel(Commandmodel commandmodel) {
		this.commandmodel = commandmodel;
	}

	public Reguser getReguser() {
		return this.reguser;
	}

	public void setReguser(Reguser reguser) {
		this.reguser = reguser;
	}

	public String getQuestName() {
		return this.questName;
	}

	public void setQuestName(String questName) {
		this.questName = questName;
	}

	public String getInformation() {
		return this.information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public Short getPri() {
		return this.pri;
	}

	public void setPri(Short pri) {
		this.pri = pri;
	}

	public Date getCommitTime() {
		return this.commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

	public Integer getMaxNodes() {
		return this.maxNodes;
	}

	public void setMaxNodes(Integer maxNodes) {
		this.maxNodes = maxNodes;
	}

	public Short getPacketSize() {
		return this.packetSize;
	}

	public void setPacketSize(Short packetSize) {
		this.packetSize = packetSize;
	}

	public Set getCommands() {
		return this.commands;
	}

	public void setCommands(Set commands) {
		this.commands = commands;
	}

	public Set getQuestargs() {
		return this.questargs;
	}

	public void setQuestargs(Set questargs) {
		this.questargs = questargs;
	}

	public Nodegroup getNodegroup() {
		return nodegroup;
	}

	public void setNodegroup(Nodegroup nodegroup) {
		this.nodegroup = nodegroup;
	}

}
