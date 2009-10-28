package com.webrender.bean.nodeconfig;

import java.util.HashSet;
import java.util.Set;

public class NodeConfig {
	int nodeId;
	GeneralConfig generalConfig;
	NetworkConfig networkConfig;
	Set modelconfigs = new HashSet(0);
	/**
	 * @return the nodeId
	 */
	public int getNodeId() {
		return nodeId;
	}
	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	/**
	 * @return the generalConfig
	 */
	public GeneralConfig getGeneralConfig() {
		return generalConfig;
	}
	/**
	 * @param generalConfig the generalConfig to set
	 */
	public void setGeneralConfig(GeneralConfig generalConfig) {
		this.generalConfig = generalConfig;
	}
	/**
	 * @return the networkConfig
	 */
	public NetworkConfig getNetworkConfig() {
		return networkConfig;
	}
	/**
	 * @param networkConfig the networkConfig to set
	 */
	public void setNetworkConfig(NetworkConfig networkConfig) {
		this.networkConfig = networkConfig;
	}
	/**
	 * @return the modelconfigs
	 */
	public Set getModelconfigs() {
		return modelconfigs;
	}
	/**
	 * @param modelconfigs the modelconfigs to set
	 */
	public void setModelconfigs(Set modelconfigs) {
		this.modelconfigs = modelconfigs;
	}
}
