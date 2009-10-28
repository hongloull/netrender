package com.webrender.dao;

import java.util.HashSet;
import java.util.Set;

/**
 * Node entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Node implements java.io.Serializable {

	// Fields

	private Integer nodeId;
	private Status status;
	private String nodeName;
	private String nodeIp;
	private Short pri;
	private String os;
	private String fileBase;
	private Short coreNum;
	private boolean realTime = false;
	private Set commands = new HashSet(0);
	private Set nodegroups = new HashSet(0);
	private Set executelogs = new HashSet(0);

	// Constructors

	/** default constructor */
	public Node() {
	}

	/** minimal constructor */
	public Node(String nodeName, String nodeIp) {
		this.nodeName = nodeName;
		this.nodeIp = nodeIp;
	}

	/** full constructor */
	public Node(Status status, String nodeName, String nodeIp, String os,
			String fileBase, Short coreNum, boolean realTime, Set commands,
			 Set nodegroups, Set executelogs) {
		this.status = status;
		this.nodeName = nodeName;
		this.nodeIp = nodeIp;
		this.os = os;
		this.fileBase = fileBase;
		this.coreNum = coreNum;
		this.realTime = realTime;
		this.commands = commands;
		this.nodegroups = nodegroups;
		this.executelogs = executelogs;
	}

	// Property accessors

	public Integer getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeIp() {
		return this.nodeIp;
	}

	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}

	public String getOs() {
		return this.os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getFileBase() {
		return this.fileBase;
	}

	public void setFileBase(String fileBase) {
		this.fileBase = fileBase;
	}

	public Short getCoreNum() {
		return this.coreNum;
	}

	public void setCoreNum(Short coreNum) {
		this.coreNum = coreNum;
	}

	public boolean getRealTime() {
		return this.realTime;
	}

	public void setRealTime(boolean realTime) {
		this.realTime = realTime;
	}

	public Set getCommands() {
		return this.commands;
	}

	public void setCommands(Set commands) {
		this.commands = commands;
	}

	public Set getExecutelogs() {
		return this.executelogs;
	}

	public void setExecutelogs(Set executelogs) {
		this.executelogs = executelogs;
	}

	public Set getNodegroups() {
		return nodegroups;
	}

	public void setNodegroups(Set nodegroups) {
		this.nodegroups = nodegroups;
	}

	/**
	 * @return the pri
	 */
	public Short getPri() {
		return pri;
	}

	/**
	 * @param pri the pri to set
	 */
	public void setPri(Short pri) {
		this.pri = pri;
	}
}
