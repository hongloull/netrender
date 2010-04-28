package com.webrender.logic;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Commandarg;
import com.webrender.dao.CommandargDAO;
import com.webrender.dao.Commandmodelarg;
import com.webrender.dao.Quest;
import com.webrender.dao.Questarg;
import com.webrender.dao.Status;
import com.webrender.dao.StatusDAO;
import com.webrender.tool.FramesOperate;
import com.webrender.tool.NameMap;

public class CalcFrames implements CalcCommands {
	private static final Log LOG = LogFactory.getLog(CalcFrames.class);
	private TreeSet<Double> frames = new TreeSet<Double>();
	private CommandDAO commandDAO = new CommandDAO();
	private Command command = null;
	private Commandarg commandArg = null;
	private CommandargDAO commandargDAO = new CommandargDAO();
	private int totalSize = 0;
	/**
	 * @return the totalSize
	 */
	public int getTotalSize() {
		return totalSize;
	}
//	public void add(double frame){
//		frames.add(frame);
//	}
//	public void printFramesValue(){
//		for(double i : frames){
//			System.out.print(i+",");
//		}
//		System.out.println();
//	}
	public int calc(Quest quest){
		return calc(quest, false);
	}
	public int patch(Quest quest){
		return calc(quest,true);
	}
	private int calc(Quest quest,boolean isPatch) {
		
		Short packetSize = quest.getPacketSize();
		String framesValue = null;
		BigDecimal byFrame = null;
		Commandmodelarg frameTag = null ,byTag = null;
		MathContext mc = new MathContext(2,RoundingMode.HALF_UP);
		Iterator questArgs = quest.getQuestargs().iterator();
		try {
			if(packetSize==null) packetSize = 1;
			while( questArgs.hasNext() )
			{
				Questarg questArg = (Questarg)questArgs.next();
				int typeValue = questArg.getCommandmodelarg().getStatus().getStatusId();
				switch (typeValue)
				{
				case 61:	framesValue  = questArg.getValue();
							frameTag = questArg.getCommandmodelarg();
							break;
				case 63:    byFrame   = new BigDecimal(questArg.getValue(),mc);   
							byTag = questArg.getCommandmodelarg();
							break;
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return CalcCommands.NumberFormatException;
		} catch(NullPointerException e){	
		}
		StatusDAO statusDAO = new StatusDAO();
		Status status = statusDAO.findById(70);
		if ( framesValue == null || byFrame==null ){
			if(isPatch) return CalcCommands.NEEDARGS;
			command = new Command(quest);
			command.setType(NameMap.GETFRAME);
			command.setStatus(status);
			commandDAO.save(command);
			return CalcCommands.LACKFRAME;
		}
		LOG.info("CALC  FramesValue:"+framesValue +" ByFrame:"+byFrame.doubleValue() );
		// 	计算Frames
		FramesOperate framesOperate = new FramesOperate();
		framesOperate.framesOperate(framesValue, byFrame.doubleValue(), frames);
		
		/// 计算结束 获得Frames集合
		this.totalSize=frames.size();
		if(totalSize==0){
			LOG.error("Frames's size is 0");
			return CalcCommands.SUCCESS;
		}
		
		double preFrame=-1;
		
		int pktNum = 0;
		String commandFrameValue = null;
		
//		double commandCurrentEndFrame=-1;
		BigDecimal commandByFrame = null;
		BigDecimal oneDelta=new BigDecimal(1,mc);
		BigDecimal delta= null;
//		double lastFrame = frames.last();
//		frames.add(lastFrame+0.00000001);
//		frames.add(lastFrame+0.00000002);
		Iterator ite_frames = frames.iterator();
		Double currentFrame = null;
		while(ite_frames.hasNext()){
			currentFrame = (Double) ite_frames.next();
			if(commandFrameValue == null){// New Command
				if(ite_frames.hasNext()==false){//New Command Finish
					commandFrameValue = currentFrame+"-"+currentFrame;
					commandByFrame = byFrame;
					//循环外保存
					break;
					
				}else{// New Command can continue
					commandFrameValue = currentFrame+"-";
					preFrame = currentFrame;
					pktNum++;
				}
			}
			else{// Command already has Head "XX-"
				delta = new BigDecimal(currentFrame - preFrame,mc);
				if(commandByFrame==null){//Just get second frame for this command 
					if(pktNum!=1){// command's frame number must be 1
						LOG.error("Error: byFrame null pktNum "+pktNum);
					}
					commandByFrame = delta;
				}
				
				if(packetSize==1){// One frame one command
					commandFrameValue=commandFrameValue+preFrame;
					commandByFrame = oneDelta;
					
					command = new Command(quest);
					command.setType(isPatch?NameMap.PATCH:null);
					command.setStatus(status);
					commandDAO.save(command);					
					commandArg = new Commandarg();
					commandArg.setCommand(command);
					commandArg.setCommandmodelarg(frameTag);
					commandArg.setValue(commandFrameValue);
					commandargDAO.save(commandArg);					
					commandArg = new Commandarg();
					commandArg.setCommand(command);
					commandArg.setCommandmodelarg(byTag);
					commandArg.setValue(commandByFrame.doubleValue()+"");
					commandargDAO.save(commandArg);
					
					pktNum=0;
					commandFrameValue=null;
					commandByFrame=null;
					delta=null;
				}else if(delta.doubleValue() == commandByFrame.doubleValue() ){
					if(ite_frames.hasNext()==false){// end frame finish this command
						commandFrameValue = commandFrameValue+currentFrame;
						//循环外保存
						break;
					}else if(pktNum <(packetSize-1) ){
						preFrame = currentFrame;
						pktNum++;
						continue;						
					}
					else{
						commandFrameValue = commandFrameValue+currentFrame;
					}
					// 保存命令，初始化属性
					command = new Command(quest);
					command.setType(isPatch?NameMap.PATCH:null);
					command.setStatus(status);
					commandDAO.save(command);					
					commandArg = new Commandarg();
					commandArg.setCommand(command);
					commandArg.setCommandmodelarg(frameTag);
					commandArg.setValue(commandFrameValue);
					commandargDAO.save(commandArg);					
					commandArg = new Commandarg();
					commandArg.setCommand(command);
					commandArg.setCommandmodelarg(byTag);
					commandArg.setValue(commandByFrame.doubleValue()+"");
					commandargDAO.save(commandArg);
					
					pktNum=0;
					commandFrameValue=null;
					commandByFrame=null;
					delta=null;
					
				}else{
					commandFrameValue = commandFrameValue+preFrame;
					
					command = new Command(quest);
					command.setType(isPatch?NameMap.PATCH:null);
					command.setStatus(status);
					commandDAO.save(command);					
					commandArg = new Commandarg();
					commandArg.setCommand(command);
					commandArg.setCommandmodelarg(frameTag);
					commandArg.setValue(commandFrameValue);
					commandargDAO.save(commandArg);					
					commandArg = new Commandarg();
					commandArg.setCommand(command);
					commandArg.setCommandmodelarg(byTag);
					commandArg.setValue(commandByFrame.doubleValue()+"");
					commandargDAO.save(commandArg);
					commandFrameValue=currentFrame+"-";
					preFrame = currentFrame;
					commandByFrame=null;
					pktNum=1;
					if(ite_frames.hasNext()==false){// end frame finish this command
						commandFrameValue = commandFrameValue+currentFrame;
						commandByFrame = oneDelta;
						//循环外保存
						break;
					}
					
				}
			}
//			if(preDelta.doubleValue()==0){
//				if(preFrame == -1){
//					preFrame=currentFrame;
//					commandFrameValue=preFrame+"-";
//					commandByFrame= new BigDecimal(1,mc);
//					commandCurrentEndFrame=preFrame;
//					continue;
//				}
//				else if(prePreFrame == -1){
//					prePreFrame = preFrame;
//					preFrame = currentFrame;
//					delta = new BigDecimal(preFrame-prePreFrame,mc);
//					commandByFrame = delta;
////					commandCurrentEndFrame=preFrame;
//					continue;
//				}
//				else{
//					preDelta = new BigDecimal(preFrame-prePreFrame,mc);
//					delta = new BigDecimal(currentFrame - preFrame,mc);
////					commandFrameValue = prePreFrame+"-";
//					commandCurrentEndFrame = prePreFrame;
//					commandByFrame =  preDelta;
//					pktNum=1;
//				}
//			}
//			else{ // 非初始化
//				delta = new BigDecimal(currentFrame - preFrame,mc);
//				// 考虑packetSize的分包计算生产command
//				if(pktNum<packetSize){
//					commandCurrentEndFrame = prePreFrame;
//					if(commandByFrame.doubleValue() == preDelta.doubleValue()){
//						pktNum++;
//					}
//					else{
//						commandFrameValue = commandFrameValue+commandCurrentEndFrame;
////						LOG.info("CommandFrames:"+commandFrameValue+" byFrame:"+commandByFrame);
//						command = new Command(quest);
//						command.setType(isPatch?NameMap.PATCH:null);
//						command.setStatus(status);
//						commandDAO.save(command);
//						
//						commandArg = new Commandarg();
//						commandArg.setCommand(command);
//						commandArg.setCommandmodelarg(frameTag);
//						commandArg.setValue(commandFrameValue);
//						commandargDAO.save(commandArg);
//						
//						commandArg = new Commandarg();
//						commandArg.setCommand(command);
//						commandArg.setCommandmodelarg(byTag);
//						commandArg.setValue(commandByFrame.doubleValue()+"");
//						commandargDAO.save(commandArg);
//						
//						commandFrameValue = preFrame+"-";
//						commandCurrentEndFrame = preFrame;
//						commandByFrame = preDelta;
//						pktNum=1;
//					}
//				}else{
//					commandFrameValue = commandFrameValue+commandCurrentEndFrame;
//					
////					LOG.info("CommandFrames:"+commandFrameValue+" byFrame:"+commandByFrame);
//					command = new Command(quest);
//					command.setType(isPatch?NameMap.PATCH:null);
//					command.setStatus(status);
//					commandDAO.save(command);
//					
//					commandArg = new Commandarg();
//					commandArg.setCommand(command);
//					commandArg.setCommandmodelarg(frameTag);
//					commandArg.setValue(commandFrameValue);
//					commandargDAO.save(commandArg);
//					
//					commandArg = new Commandarg();
//					commandArg.setCommand(command);
//					commandArg.setCommandmodelarg(byTag);
//					commandArg.setValue(commandByFrame.toString());
//					commandargDAO.save(commandArg);
//					
//					commandFrameValue = prePreFrame+"-";
//					commandCurrentEndFrame = prePreFrame;
//					commandByFrame = preDelta;
//					pktNum=1;
//				}
//				
//				
//			}
//			//数据前进
//			prePreFrame = preFrame;
//			preFrame = currentFrame;
//			preDelta = delta;
//		}
//		if(commandCurrentEndFrame>lastFrame){
//			return CalcCommands.SUCCESS;
//		}
//		commandFrameValue = commandFrameValue+commandCurrentEndFrame;
//		
//		if( commandByFrame.doubleValue()==0.00000001){
//			commandByFrame= new BigDecimal(1,mc);
//		}
//			LOG.info("CommandFrames:"+commandFrameValue+" byFrame:"+commandByFrame);
//			command = new Command(quest);
//			command.setType(isPatch?NameMap.PATCH:null);
//			command.setStatus(status);
//			commandDAO.save(command);
//			
//			commandArg = new Commandarg();
//			commandArg.setCommand(command);
//			commandArg.setCommandmodelarg(frameTag);
//			commandArg.setValue(commandFrameValue);
//			commandargDAO.save(commandArg);
//			
//			commandArg = new Commandarg();
//			commandArg.setCommand(command);
//			commandArg.setCommandmodelarg(byTag);
//			commandArg.setValue(commandByFrame.toString());
//			commandargDAO.save(commandArg);
		}
		if(commandFrameValue!=null && commandByFrame!=null ){
			command = new Command(quest);
			command.setType(isPatch?NameMap.PATCH:null);
			command.setStatus(status);
			commandDAO.save(command);
			
			commandArg = new Commandarg();
			commandArg.setCommand(command);
			commandArg.setCommandmodelarg(frameTag);
			commandArg.setValue(commandFrameValue);
			commandargDAO.save(commandArg);
			
			commandArg = new Commandarg();
			commandArg.setCommand(command);
			commandArg.setCommandmodelarg(byTag);
			commandArg.setValue(commandByFrame.doubleValue()+"");
			commandargDAO.save(commandArg);
		}
		return CalcCommands.SUCCESS;
	}

}
