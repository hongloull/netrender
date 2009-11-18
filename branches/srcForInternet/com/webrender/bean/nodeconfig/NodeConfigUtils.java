package com.webrender.bean.nodeconfig;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.XMLOut;

public class NodeConfigUtils {
	
//	private static final String NODE = "node";
//	private static final String MODEL = "model";
	private static final String GENERAL = "general";
//	private static final String NODEID = "nodeId";
//	private static final String NETWORK = "network";
	private static final String PRIORITY = "priority";
//	private ModelConfigUtils modelConfigUtils = new ModelConfigUtils();
//	private GeneralConfigUtils generalConfigUtils = new GeneralConfigUtils();
//	private NetworkConfigUtils networkConfigUtils = new NetworkConfigUtils();
//	
//	public NodeConfig xmlString2bean(String configXML) throws JDOMException{
//		SAXBuilder builder = new SAXBuilder();
//		InputStream inputStream = new ByteArrayInputStream(configXML.getBytes());
//		Document doc = builder.build(inputStream);
//		Element root = doc.getRootElement();
//		return xml2bean(root);
//	}
//	public NodeConfig xml2bean(Element root){
//		
//		String nodeId = root.getAttributeValue(NODEID);
//		
//		NodeConfig nodeConfig = new NodeConfig();
//		try{
//			nodeConfig.setNodeId(Integer.parseInt(nodeId));
//		}catch(Exception e){
//		}
//
//		Element models = root.getChild(MODEL);
//		Iterator ite_models = models.getChildren().iterator();
//		while(ite_models.hasNext()){
//			Element eleModel = (Element)ite_models.next();
//			nodeConfig.getModelconfigs().add(modelConfigUtils.xml2bean(eleModel));
//		}
//		Element general = root.getChild(GENERAL);
//		nodeConfig.setGeneralConfig( generalConfigUtils.xml2bean(general) );
//		
//		Element network = root.getChild(NETWORK);
//		nodeConfig.setNetworkConfig(networkConfigUtils.xml2bean(network));
//		
//		return nodeConfig;
//	}
//	
//	public Element bean2xml(NodeConfig config){
//		Element root = new Element(NODE);
//		
//		root.addContent( generalConfigUtils.bean2xml( config.getGeneralConfig() ) );
//		
//		root.addContent( networkConfigUtils.bean2xml(config.getNetworkConfig()));
//		
//		Iterator ite_ModelConfigs = config.getModelconfigs().iterator();
//		Element modelElement = new Element(MODEL);
//		while(ite_ModelConfigs.hasNext()){
//			 modelElement.addContent( modelConfigUtils.bean2xml((ModelConfig) ite_ModelConfigs.next()) );
//		}
//		root.addContent(modelElement);
//		return root;
//	}
//	
//	public String bean2xmlString(NodeConfig config){
//		Document doc = new Document(bean2xml(config));
//		return (new XMLOut()).outputToString(doc);
//	}
//	
	public String setPriorityToConfig (String configXML ,Short priority) throws JDOMException{
		SAXBuilder builder = new SAXBuilder();
		InputStream inputStream = new ByteArrayInputStream(configXML.getBytes());
		Document doc = builder.build(inputStream);
		Element root = doc.getRootElement();
		Element general = root.getChild(GENERAL);
		if(general.getAttribute(PRIORITY) == null ){
			general.addAttribute(PRIORITY,priority+"");
		}else{
			general.getAttribute(PRIORITY).setValue(priority+"");
		}
		return (new XMLOut()).outputToString(doc);
	}
	public Short getPrioritFromConfig(String configXML) throws JDOMException{
		SAXBuilder builder = new SAXBuilder();
		InputStream inputStream = new ByteArrayInputStream(configXML.getBytes());
		Document doc = builder.build(inputStream);
		Element root = doc.getRootElement();
		Element general = root.getChild(GENERAL);
		String priority = general.getAttributeValue(PRIORITY);
		return Short.parseShort(priority);
		
	}
}
