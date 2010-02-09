package com.webrender.protocol.processor;

import java.util.ArrayList;
import java.util.List;

import com.webrender.protocol.enumn.EOPCODES.CODE;

public interface IClientProcessor {

	void parseDatas(CODE code,byte[] fmts , List<String> datas);	
}
