package com.webrender.axis.beanxml;

import org.jdom.Element;
import com.webrender.dao.Right;

public final class RightUtils {
	public static Element bean2xml(Right right){
		Element root = new Element("Right");
		root.addAttribute("Id",right.getRightId().toString());
		root.addAttribute("detail",right.getInstruction()+"");
		return root;
	}
}
