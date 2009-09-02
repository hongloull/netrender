package com.webrender.protocol.processor;

import java.util.ArrayList;
import java.util.List;

import com.webrender.protocol.enumn.EOPCODES.CODE;

public interface IClientProcessor {

	void execute(CODE code,byte[] fmts , List<String> datas);	
}
