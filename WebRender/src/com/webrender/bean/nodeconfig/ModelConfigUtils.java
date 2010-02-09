package com.webrender.bean.nodeconfig;
import org.jdom.Element;

public class ModelConfigUtils {
	private static final String MODEL = "model";
	private static final String EXE = "exe";
	private static final String RENDER = "render";
	public ModelConfig xml2bean(Element root){
		
//		String name = root.getName();
//		String exe = root.getAttributeValue(EXE);
//		String render = root.getAttributeValue(RENDER);
		ModelConfig modelConfig = new ModelConfig();
//		modelConfig.setName(name);
//		modelConfig.setExe(exe);
//		modelConfig.setRender(render);
		modelConfig.setElement(root);
		return modelConfig;
	}
	public Element bean2xml(ModelConfig config) {
//		Element root = new Element( config.getName()+"" );
//		root.addAttribute(EXE,config.getExe()+"");
//		root.addAttribute(RENDER,config.getRender()+"");
//		return root;
		return config.getElement();
	}
	
}
