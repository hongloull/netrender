package com.webrender.axis;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.axis.operate.BaseOperate;
import com.webrender.axis.operate.CommandOperateImpl;
import com.webrender.dao.CommandDAO;
/**
 * Axis Class
 * control commands information.
 * command: Minimum  Unit send to a node
 * @author WAEN
 *
 */
public class CommandOperate extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(CommandOperate.class);
	
	/**
	 * Get command's real log (console)
	 * @param commandId
	 * @return LogFile's content
	 */
	public String getRealLogs(String commandId){
		
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		return  (new CommandOperateImpl()).getRealLogFile(commandId);
	}
	
//	public String getRealLogFile(String commandId){
//		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
//		
//		return BaseAxis.ACTIONFAILURE;
//	}

	/**
	 * redo commands
	 * @param commandIds 
	 * @return BaseOperate result : FAILURE or SUCCESS
	 */
	public String reinitCommand(String[] commandIds){
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) ) )
			return BaseAxis.RIGHTERROR;
		CommandOperateImpl commandOperateImpl = new CommandOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String commandId:commandIds){
			if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(commandId)) ) ){
				result.append(commandId).append(":").append(BaseAxis.RIGHTERROR).append("\n\r");
				continue;
			}
			subResult = commandOperateImpl.reinitCommand(commandId,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(commandId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();			
	}
	/**
	 * SetFinish commands
	 * @param commandIds
	 * @return BaseOperate result : FAILURE or SUCCESS
	 */
	public String setFinish(String[] commandIds){
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) ) )
			return BaseAxis.RIGHTERROR;
		CommandOperateImpl commandOperateImpl = new CommandOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String commandId:commandIds){
			if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(commandId)) ) ){
				result.append(commandId).append(":").append(BaseAxis.RIGHTERROR).append("\n\r");
				continue;
			}
			subResult = commandOperateImpl.setFinish(commandId,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(commandId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();	
		
	}
	
	
	@Override
	/**
	 * judge commandId is or not submitted by log user;
	 * 
	 */
	protected boolean isSelf(int commandId) {
		CommandDAO commandDAO = new CommandDAO();
		if ( this.getLoginUserId() == commandDAO.findById(commandId).getQuest().getReguser().getRegUserId() ){
			return true;
		}
		return false;
	}
	
}
