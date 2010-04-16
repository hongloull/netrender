package com.webrender.dao.view;

/**
 * CommandnodeId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CommandnodeId implements java.io.Serializable {

	// Fields

	private Integer commandId;
	private Integer nodeId;
	private Integer questId;
	private Integer nodeGroupId;

	// Constructors

	/** default constructor */
	public CommandnodeId() {
	}

	/** full constructor */
	public CommandnodeId(Integer commandId, Integer nodeId, Integer questId,
			Integer nodeGroupId) {
		this.commandId = commandId;
		this.nodeId = nodeId;
		this.questId = questId;
		this.nodeGroupId = nodeGroupId;
	}

	// Property accessors

	public Integer getCommandId() {
		return this.commandId;
	}

	public void setCommandId(Integer commandId) {
		this.commandId = commandId;
	}

	public Integer getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getQuestId() {
		return this.questId;
	}

	public void setQuestId(Integer questId) {
		this.questId = questId;
	}

	public Integer getNodeGroupId() {
		return this.nodeGroupId;
	}

	public void setNodeGroupId(Integer nodeGroupId) {
		this.nodeGroupId = nodeGroupId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CommandnodeId))
			return false;
		CommandnodeId castOther = (CommandnodeId) other;

		return ((this.getCommandId() == castOther.getCommandId()) || (this
				.getCommandId() != null
				&& castOther.getCommandId() != null && this.getCommandId()
				.equals(castOther.getCommandId())))
				&& ((this.getNodeId() == castOther.getNodeId()) || (this
						.getNodeId() != null
						&& castOther.getNodeId() != null && this.getNodeId()
						.equals(castOther.getNodeId())))
				&& ((this.getQuestId() == castOther.getQuestId()) || (this
						.getQuestId() != null
						&& castOther.getQuestId() != null && this.getQuestId()
						.equals(castOther.getQuestId())))
				&& ((this.getNodeGroupId() == castOther.getNodeGroupId()) || (this
						.getNodeGroupId() != null
						&& castOther.getNodeGroupId() != null && this
						.getNodeGroupId().equals(castOther.getNodeGroupId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCommandId() == null ? 0 : this.getCommandId().hashCode());
		result = 37 * result
				+ (getNodeId() == null ? 0 : this.getNodeId().hashCode());
		result = 37 * result
				+ (getQuestId() == null ? 0 : this.getQuestId().hashCode());
		result = 37
				* result
				+ (getNodeGroupId() == null ? 0 : this.getNodeGroupId()
						.hashCode());
		return result;
	}
	
	public String toString(){
		return "commandId:"+this.commandId+" nodeGroupId:"+this.nodeGroupId+" nodeId:"+this.nodeId+" questId:"+this.questId;
	}
}