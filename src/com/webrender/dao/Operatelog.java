package com.webrender.dao;

/**
 * Operatelog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Operatelog implements java.io.Serializable {

	// Fields

	private Integer operateLogId;
	private Reguser reguser;
	private String operateInformation;
	private Short type;
	private String table;
	private Integer tableId;

	// Constructors

	/** default constructor */
	public Operatelog() {
	}

	/** minimal constructor */
	public Operatelog(Reguser reguser, Short type) {
		this.reguser = reguser;
		this.type = type;
	}

	/** full constructor */
	public Operatelog(Reguser reguser, String operateInformation, Short type,
			String table, Integer tableId) {
		this.reguser = reguser;
		this.operateInformation = operateInformation;
		this.type = type;
		this.table = table;
		this.tableId = tableId;
	}

	// Property accessors

	public Integer getOperateLogId() {
		return this.operateLogId;
	}

	public void setOperateLogId(Integer operateLogId) {
		this.operateLogId = operateLogId;
	}

	public Reguser getReguser() {
		return this.reguser;
	}

	public void setReguser(Reguser reguser) {
		this.reguser = reguser;
	}

	public String getOperateInformation() {
		return this.operateInformation;
	}

	public void setOperateInformation(String operateInformation) {
		this.operateInformation = operateInformation;
	}

	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getTable() {
		return this.table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public Integer getTableId() {
		return this.tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

}