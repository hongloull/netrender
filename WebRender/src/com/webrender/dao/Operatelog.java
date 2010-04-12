package com.webrender.dao;

import java.util.Date;

/**
 * Operatelog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Operatelog implements java.io.Serializable {

	// Fields
	public static final Short LOGIN = 1;
	public static final Short MOD = 3;
	public static final Short ADD = 4;
	public static final Short DEL = 5;
	public static final Short ERROR = 7;
	
	public static final String QUEST = "Quest";
	public static final String COMMAND="Command";
	public static final String NODE = "Node";
	public static final String NODEGROUP = "Nodegroup";
	private Integer operateLogId;
	private Reguser reguser;
	private String operateInformation;
	private Short type;
	private String tableName;
	private Integer tableId;
	private Date logTime;
	private String comment;

	// Constructors

	/** default constructor */
	public Operatelog() {
	}

	/**
	 * 
	 * @param reguser
	 * @param type 1:login. 2:query. 3:modify. 4:add. 5:delete. 6:OM. 7:Error.
	 */
	public Operatelog(Reguser reguser, Short type, Date logTime) {
		this.reguser = reguser;
		this.type = type;
		this.logTime = logTime;
	}

	/** full constructor */
	public Operatelog(Reguser reguser, String operateInformation, Short type,
			 Date logTime) {
		this.reguser = reguser;
		this.operateInformation = operateInformation;
		this.type = type;
		this.logTime = logTime;
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
	

	/**
	 * @return the tableId
	 */
	public Integer getTableId() {
		return tableId;
	}

	/**
	 * @param tableId the tableId to set
	 */
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getLogTime() {
		return this.logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}