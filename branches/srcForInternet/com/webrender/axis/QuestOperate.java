package com.webrender.axis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.axis.operate.QuestOperateImpl;
import com.webrender.dao.QuestDAO;

public class QuestOperate extends BaseAxis {
	
	private static final Log LOG = LogFactory.getLog(QuestOperate.class);
	//String NotLogin = "<ResultSet>NotLogin</ResultSet>";
	public String CommitQuest(String questXML)
	{
		if (  !this.canVisit(0) &&  !this.canVisit(10) ) return BaseAxis.RIGHTERROR;
		
		return (new QuestOperateImpl()).CommitQuest(questXML,this.getLoginUserId());
				
	}

	public String deleteQuest(String questId)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
					return BaseAxis.RIGHTERROR;		
		return (new QuestOperateImpl()).deleteQuest(questId, this.getLoginUserId());
	}
	public String pauseQuest(String questId)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;
		
		return (new QuestOperateImpl()).pauseQuest(questId, this.getLoginUserId());
	}
	public String resumeQuest(String questId)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;
		
		return (new QuestOperateImpl()).resumeQuest(questId,this.getLoginUserId());
	}
	
	public String reinitQuest(String questId)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;
		return (new QuestOperateImpl()).reinitQuest(questId, this.getLoginUserId());
	}
	
	public String setFinish(String questId)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;
		return (new QuestOperateImpl()).setFinish(questId, this.getLoginUserId());
	}
	
	public String changeName(String questId,String name){
		
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;
		
		return (new QuestOperateImpl()).changeName(questId, name, this.getLoginUserId()); 
	}
	
	public String changePriority(String questId,String pri)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;

		return (new QuestOperateImpl()).changePriority(questId, pri,this.getLoginUserId());
	}
	public String  changeMaxNodes(String questId,String maxNodes)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;

		return (new QuestOperateImpl()).changeMaxNodes(questId, maxNodes, this.getLoginUserId());
	}
	public String changePool(String questId , String poolName)
	{
		
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;
		return (new QuestOperateImpl()).changePool(questId, poolName, this.getLoginUserId());
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
	
	public String getPreLight(String questId){
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		return (new QuestOperateImpl()).getPreLight(questId); 
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
