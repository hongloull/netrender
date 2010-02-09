package com.webrender.dao;

/**
 * Questarg entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Questarg implements java.io.Serializable {

	// Fields

	private Integer questArgId;
	private Quest quest;
	private Commandmodelarg commandmodelarg;
	private String value;

	// Constructors

	/** default constructor */
	public Questarg() {
	}

	/** minimal constructor */
	public Questarg(Quest quest, Commandmodelarg commandmodelarg) {
		this.quest = quest;
		this.commandmodelarg = commandmodelarg;
	}

	/** full constructor */
	public Questarg(Quest quest, Commandmodelarg commandmodelarg, String value) {
		this.quest = quest;
		this.commandmodelarg = commandmodelarg;
		this.value = value;
	}

	// Property accessors

	public Integer getQuestArgId() {
		return this.questArgId;
	}

	public void setQuestArgId(Integer questArgId) {
		this.questArgId = questArgId;
	}

	public Quest getQuest() {
		return this.quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
	}

	public Commandmodelarg getCommandmodelarg() {
		return this.commandmodelarg;
	}

	public void setCommandmodelarg(Commandmodelarg commandmodelarg) {
		this.commandmodelarg = commandmodelarg;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}