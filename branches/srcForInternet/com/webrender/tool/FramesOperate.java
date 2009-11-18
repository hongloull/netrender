package com.webrender.tool;

import java.util.Set;
import java.util.TreeSet;

public class FramesOperate {
	public void framesOperate(String strFramesValue, double defaultByFrame,Set<Double> frames){
		strFramesValue+=",";
		char[] charFrames = strFramesValue.toCharArray();
		StringBuffer buffer = new StringBuffer();
		double startValue = -1 ;
		double endValue = -1;
		double byValue = -1;
		for(char temp : charFrames){
			switch (temp){
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '0':
			case '.':
				buffer.append(temp);
				break;
			case ',':
			case ';':
			case ':':
				if(buffer.length()!=0){
					double tempFrame = -1;
					try{
						tempFrame =  Double.parseDouble(buffer.toString()) ;
					}catch(Exception e){
						throw new NumberFormatException("Frames is not a valid data");
					}
					if(tempFrame==-1){
						//数据格式错误 忽视。
						startValue = endValue = byValue = -1;
						break;
					}
					if(startValue != -1 ){
						if(endValue == -1 ){
							endValue = tempFrame;
							calcFramesOperate(startValue,endValue,defaultByFrame,frames);
						}else{
							byValue = tempFrame;
							calcFramesOperate(startValue,endValue,byValue,frames);
						}
					}else if(endValue != -1){
						// 10*4
						frames.add(endValue);
					}
					else{
						frames.add(tempFrame);
					}
					buffer = new StringBuffer();					
				}else{
					if(startValue!=-1) frames.add(startValue);
				}
				startValue = endValue = byValue = -1;
				break;
				
			case '-':
				if(buffer.length()!=0){
					if(startValue==-1){
						try{
							startValue = Double.parseDouble(buffer.toString()) ;							
						}catch(Exception e){
							throw new NumberFormatException("Frames is not a valid data");
						}
					}else{
						// 1-4-15
					}
					buffer = new StringBuffer();					
				}else{
					// 1--7 -7
				}
				break;
				
			case '*':
			case 'x':
			case 'X':
				if(buffer.length()!=0){
					if(endValue==-1){
						try{
							endValue = Double.parseDouble(buffer.toString()) ;							
						}catch(Exception e){
							throw new NumberFormatException("Frames is not a valid data");
						}
					}else{
//						throw new NumberFormatException("Frames is not a valid data");
					}
					buffer = new StringBuffer();					
				}
				else{
					
				}
				break;
			default:
				throw new NumberFormatException("Frames is not a valid data");
			}
		}
	}

	private void calcFramesOperate(double startValue, double endValue,
			double byValue,Set<Double> frames) {
		if(startValue>endValue) return;
		while (startValue<endValue){
			frames.add(startValue);
			startValue+=byValue;
		}
		frames.add(endValue);
	}
}
