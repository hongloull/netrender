package com.webrender.axis.beanxml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import com.webrender.dao.Right;

public final class RightUtils {
	private static final Log LOG = LogFactory.getLog(RightUtils.class);
	public static Element bean2xml(Right right){
		LOG.debug("bean2xml right:"+right.getInstruction());
		Element root = new Element("Right");
		root.addAttribute("Id",right.getRightId().toString());
		root.addAttribute("detail",right.getInstruction()+"");
		LOG.debug("bean2xml success right:"+right.getInstruction());
		return root;
	}
}
