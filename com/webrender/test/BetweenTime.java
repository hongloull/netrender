package com.webrender.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.mina.common.ByteBuffer;

import com.webrender.protocol.messages.ServerMessages;

public class BetweenTime {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
//		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		   java.util.Date begin=dfs.parse("2008-01-02 11:30:24");
//		   java.util.Date end = dfs.parse("2008-01-03 12:32:41");
//		   long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
//
//		   long day1=between/(24*3600);
//		   long hour1=between%(24*3600)/3600;
//		   long minute1=between%3600/60;
//		   long second1=between%60;
//		   System.out.println(""+day1+"天"+hour1+"小时"+minute1+"分"+second1+"秒");
		
		
		try {
			ByteBuffer buffer = (new ServerMessages()).createSetConfigPkt("==================");
			System.out.println(buffer);
			buffer.setAutoExpand(true);
			buffer.put((byte)16);
			System.out.println(buffer);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}

}
