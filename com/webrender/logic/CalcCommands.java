package com.webrender.logic;

import com.webrender.dao.Quest;

public interface CalcCommands {
	public static final int NumberFormatException = 0;
	public static final int SUCCESS = 1; 
	public static final int NEEDARGS = 2; // 需要必备参数或正确参数
	public static final int LACKFRAME = 3; // 缺少可计算查看帧数
	
	public int calc(Quest quest);
}
