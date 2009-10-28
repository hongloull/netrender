package com.webrender.bean.nodeconfig;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class ModelConfigUtils {
	private static final String MODEL = "model";
	private static final String EXE = "exe";
	private static final String RENDER = "render";
	public ModelConfig xml2bean(Element root){
		
		String name = root.getName();
		String exe = root.getAttributeValue(EXE);
		String render = root.getAttributeValue(RENDER);
		ModelConfig modelConfig = new ModelConfig();
		modelConfig.setName(name);
		modelConfig.setExe(exe);
		modelConfig.setRender(render);
		return modelConfig;
	}
	public Element bean2xml(ModelConfig config) {
		Element root = new Element( config.getName() );
		root.addAttribute(EXE,config.getExe());
		root.addAttribute(RENDER,config.getRender());
		return root;
	}
	
}
