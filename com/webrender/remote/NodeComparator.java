package com.webrender.remote;

import java.util.Comparator;

public class NodeComparator implements Comparator<NodeMachine> {


	public int compare(NodeMachine o1, NodeMachine o2) {
		// TODO Auto-generated method stub
	
		int comp = (Integer)(o2.getPri()-o1.getPri());
		if(comp != 0 ) return comp;
		else{
			return o1.getId() - o2.getId();
		}
		
	}

}
