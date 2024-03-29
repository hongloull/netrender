package com.webrender.axis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.axis.operate.BaseOperate;
import com.webrender.axis.operate.QuestOperateImpl;
import com.webrender.dao.QuestDAO;

public class QuestOperate extends BaseAxis {
	
	private static final Log LOG = LogFactory.getLog(QuestOperate.class);
	
	public String deleteQuest(String questId){
		String[] questIds = {questId};
		return this.deleteQuest(questIds);
	}
	public String pauseQuest(String questId){
		String[] questIds = {questId};
		return this.pauseQuest(questIds);
	}
	public String resumeQuest(String questId){
		String[] questIds = {questId};
		return this.resumeQuest(questIds);
	}
	public String reinitQuest(String questId){
		String[] questIds = {questId};
		return this.reinitQuest(questIds);
	}
	public String setFinish(String questId){
		String[] questIds = {questId};
		return this.setFinish(questIds);
	}
	public String changeName(String questId,String name){
		String[] questIds = {questId};
		return this.changeName(questIds, name);
	}
	public String changePriority(String questId,String pri){
		String[] questIds = {questId};
		return this.changePriority(questIds, pri);
	}
	public String changeMaxNodes(String questId,String maxNodes){
		String[] questIds = {questId};
		return this.changeMaxNodes(questIds, maxNodes);
	}
	public String changePool(String questId,String poolName){
		String[] questIds = {questId};
		return this.changePool(questIds, poolName);
	}
	
	public String CommitQuest(String questXML)
	{
		if (  !this.canVisit(0) &&  !this.canVisit(10) ) return BaseAxis.RIGHTERROR;
		
		return (new QuestOperateImpl()).CommitQuest(questXML,this.getLoginUserId(),this.canVisit(0));
				
	}

	public String deleteQuest(String[] questIds)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) ) )
			return BaseAxis.RIGHTERROR;
		QuestOperateImpl questOperateImpl = new QuestOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String questId:questIds){
			if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) ){
				result.append(questId).append(":").append(BaseAxis.RIGHTERROR).append("\n\r");
				continue;
			}
			subResult = questOperateImpl.deleteQuest(questId,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(questId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	public String pauseQuest(String[] questIds)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) ) )
			return BaseAxis.RIGHTERROR;
		QuestOperateImpl questOperateImpl = new QuestOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String questId:questIds){
			if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) ){
				result.append(questId).append(":").append(BaseAxis.RIGHTERROR).append("\n\r");
				continue;
			}
			subResult = questOperateImpl.pauseQuest(questId,null,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(questId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	public String resumeQuest(String[] questIds)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) ) )
			return BaseAxis.RIGHTERROR;
		QuestOperateImpl questOperateImpl = new QuestOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String questId:questIds){
			if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) ){
				result.append(questId).append(":").append(BaseAxis.RIGHTERROR).append("\n\r");
				continue;
			}
			subResult = questOperateImpl.resumeQuest(questId,null,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(questId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	
	public String reinitQuest(String[] questIds)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) ) )
			return BaseAxis.RIGHTERROR;
		QuestOperateImpl questOperateImpl = new QuestOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String questId:questIds){
			if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) ){
				result.append(questId).append(":").append(BaseAxis.RIGHTERROR).append("\n\r");
				continue;
			}
			subResult = questOperateImpl.reinitQuest(questId,null,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(questId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	
	public String setFinish(String[] questIds)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) ) )
			return BaseAxis.RIGHTERROR;
		QuestOperateImpl questOperateImpl = new QuestOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String questId:questIds){
			if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) ){
				result.append(questId).append(":").append(BaseAxis.RIGHTERROR).append("\n\r");
				continue;
			}
			subResult = questOperateImpl.setFinish(questId,null,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(questId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	
	public String changeName(String[] questIds,String name){
		
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) ) )
			return BaseAxis.RIGHTERROR;
		QuestOperateImpl questOperateImpl = new QuestOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String questId:questIds){
			if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) ){
				result.append(questId).append(":").append(BaseAxis.RIGHTERROR).append("\n\r");
				continue;
			}
			subResult = questOperateImpl.changeName(questId,name,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(questId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	
	public String changePriority(String[] questIds,String pri)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) ) )
			return BaseAxis.RIGHTERROR;
		QuestOperateImpl questOperateImpl = new QuestOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String questId:questIds){
			if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) ){
				result.append(questId).append(":").append(BaseAxis.RIGHTERROR).append("\n\r");
				continue;
			}
			subResult = questOperateImpl.changePriority(questId,pri,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(questId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	public String  changeMaxNodes(String[] questIds,String maxNodes)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) ) )
			return BaseAxis.RIGHTERROR;
		QuestOperateImpl questOperateImpl = new QuestOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String questId:questIds){
			if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) ){
				result.append(questId).append(":").append(BaseAxis.RIGHTERROR).append("\n\r");
				continue;
			}
			subResult = questOperateImpl.changeMaxNodes(questId,maxNodes,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(questId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	
	public String changePool(String[] questIds, String poolName)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) ) )
			return BaseAxis.RIGHTERROR;
		QuestOperateImpl questOperateImpl = new QuestOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String questId:questIds){
			if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) ){
				result.append(questId).append(":").append(BaseAxis.RIGHTERROR).append("\n\r");
				continue;
			}
			subResult = questOperateImpl.changePool(questId,poolName,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(questId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	public String patchFrames(String[] questIds, String frames){
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) ) )
			return BaseAxis.RIGHTERROR;
		QuestOperateImpl questOperateImpl = new QuestOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String questId:questIds){
			if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) ){
				result.append(questId).append(":").append(BaseAxis.RIGHTERROR).append("\n\r");
				continue;
			}
			subResult = questOperateImpl.patchFrames(questId,frames,null,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(questId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	public String getDetail(String questId)
	{
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;

		return (new QuestOperateImpl()).getDetail(questId);
	}
	
	public String getChunkDetail(String questId)
	{
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;

		return (new QuestOperateImpl()).getChunkDetail(questId);
	}
	public String getOperateLogs(String questId){
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;

		return (new QuestOperateImpl()).getOperateLogs(questId);
	}
	public String getPreLight(String questId){
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		return (new QuestOperateImpl()).getPreLight(questId); 
	}
	public String getFrame(String questId){
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		return (new QuestOperateImpl()).getFrame(questId);
	}
	
	
	@Override
	protected boolean isSelf(int questId){
		QuestDAO questDAO = new QuestDAO();
		if (questDAO.findById(questId).getReguser().getRegUserId()==this.getLoginUserId() ){
			return true;
		}
		return false;
	}
}
