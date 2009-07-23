package com.webrender.axis;

import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;

import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Nodegroup;
import com.webrender.dao.NodegroupDAO;

public class PoolOperate extends BaseAxis {
	public String addPool(String name){
		return BaseAxis.ActionSuccess;
	}
	public String modPool(String name ,String questXML){
		return BaseAxis.ActionSuccess;
	}
	public String deletePool(String name){
		return BaseAxis.ActionSuccess;
	}
	public String getAllPools(){
		try {
			NodegroupDAO nGDAO = new NodegroupDAO();
			Iterator<Nodegroup> ite_Pools = nGDAO.findAll().iterator();
			Element root = new Element("Pools");
			Document doc = new Document(root);
			while (ite_Pools.hasNext()) {
				Nodegroup pool = ite_Pools.next();
				Element element = new Element("Pool");
				element.addAttribute("name", pool.getNodeGroupName());
				root.addContent(element);
			}
			return XMLOut.outputToString(doc);
		} catch (Exception e) {
			return BaseAxis.ActionFailure;
		}finally{
			this.closeSession();
		}
	}
	public String getPools()
	{
		return getAllPools();
	}
	
}
