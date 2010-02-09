function UserLogin(){
	$.WSVisit({
		type:"UserLogin",
		methodName: "LoginValidate",
		data: {regName:$("#user_id").val(),passWord:$("#pw_id").val()},
		success:function(data){
   			refreshAllNodes();
   			refreshAllJobs();
		}
	});
} 



//-------------------------Node----------------------------------------//
function node_refreshall(){
	$.WSVisit({
		type:"NodesState",
		methodName: "getNodesStatus",
		data: {groupName:"All"},
		success:function(data){
			 	var doc = $.createXMLDocument(data);
			 	cleanNodes();
	   			$(doc).find('Node').each(function(){
	   				addNodeElement($(this).attr("nodeName")+'',
	   						$(this).attr("nodeIp")+'',
	   						$(this).attr("nodeId")+'',
	   						$(this).attr("status")+'',
	   						$(this).attr("priority")+'',
	   						$(this).attr("platform")+'',
	   						$(this).attr("procNum")+'',
	   						$(this).attr("cpu")+'',
	   						$(this).attr("ramUsage")+'',
	   						$(this).attr("jobName")+'',
	   						$(this).attr("frames")+'',
	   						$(this).attr("realTime"))
   			 	});
		}
	});
}

function node_refresh(id){
	$.WSVisit({
		type:"NodesState",
		methodName: "getNodeStatus",
		data: {nodeId:id},
		success:function(data){
			 	var doc = $.createXMLDocument(data);
	   			$(doc).find('Node').each(function(){
	   				updateNodeElement($(this).attr("nodeName")+'',
	   						$(this).attr("nodeIp")+'',
	   						$(this).attr("nodeId")+'',
	   						$(this).attr("status")+'',
	   						$(this).attr("priority")+'',
	   						$(this).attr("platform")+'',
	   						$(this).attr("procNum")+'',
	   						$(this).attr("cpu")+'',
	   						$(this).attr("ramUsage")+'',
	   						$(this).attr("jobName")+'',
	   						$(this).attr("frames")+'',
	   						$(this).attr("realTime"))
   			 	});
		}
	});
} 

function node_pause(id){
	$.WSVisit({
		type:"NodeOperate",
		methodName: "pauseNode",
		data: {nodeId:id},
		success:function(data){
			 node_refresh(id);
		}
	});
}

function node_resume(id){
	$.WSVisit({
		type:"NodeOperate",
		methodName: "resumeNode",
		data: {nodeId:id},
		success:function(data){
			 node_refresh(id);
		}
	});
}
function node_logoff(id){
	$.WSVisit({
		type:"NodeOperate",
		methodName: "setRealLog",
		data: {nodeId:id,isOpen:"0"},
		success:function(data){
			 node_refresh(id);
		}
	});
}
function node_logon(id){
	$.WSVisit({
		type:"NodeOperate",
		methodName: "setRealLog",
		data: {nodeId:id,isOpen:"1"},
		success:function(data){
			 node_refresh(id);
		}
	});
}
function node_openlog(id){
	alert('openlog');
}
function node_softrestart(id){
	$.WSVisit({
		type:"NodeOperate",
		methodName: "softRestart",
		data: {nodeId:id},
		success:function(data){
			 node_refresh(id);
		}
	});
}
function node_restart(id){
	$.WSVisit({
		type:"NodeOperate",
		methodName: "shutdownNode",
		data: {nodeId:id,isReboot:"1"},
		success:function(data){
			 node_refresh(id);
		}
	});
}
function node_poweroff(id){
	$.WSVisit({
		type:"NodeOperate",
		methodName: "shutdownNode",
		data: {nodeId:id,isReboot:"0"},
		success:function(data){
			 node_refresh(id);
		}
	});
}
//-------------------------JOB---------------------------------//
function job_refreshall(){
	$.WSVisit({
		type:"QuestsState",
		methodName: "getQuestsStatus",
		data: {},
		success:function(data){
			 	var doc = $.createXMLDocument(data);
			 	cleanJobs();
	   			$(doc).find('Quest').each(function(){
	   				addJobElement($(this).attr("questName")+'',
	   						$(this).attr("questId")+'',
	   						$(this).attr("status")+'',
	   						$(this).attr("pri")+'',
	   						$(this).attr("progress")+'',
	   						$(this).attr("fileName")+'',
	   						$(this).attr("frames")+'',
	   						$(this).attr("totalFrames")+'',
	   						$(this).attr("Nodes")+'',
	   						$(this).attr("userInstances")+'',
	   						$(this).attr("regName")+'',
	   						$(this).attr("commitTime"))
	   						
   			 	});
		}
	});
}
function job_refresh(id){
	$.WSVisit({
		type:"QuestsState",
		methodName: "getQuestStatus",
		data: {questId:id},
		success:function(data){
			 	var doc = $.createXMLDocument(data);
	   			$(doc).find('Quest').each(function(){
	   				updateJobElement($(this).attr("questName")+'',
	   						$(this).attr("questId")+'',
	   						$(this).attr("status")+'',
	   						$(this).attr("pri")+'',
	   						$(this).attr("progress")+'',
	   						$(this).attr("fileName")+'',
	   						$(this).attr("frames")+'',
	   						$(this).attr("totalFrames")+'',
	   						$(this).attr("Nodes")+'',
	   						$(this).attr("userInstances")+'',
	   						$(this).attr("regName")+'',
	   						$(this).attr("commitTime"))
	   						
   			 	});
		}
	});
}
   
function job_pause(id){
	$.WSVisit({
		type:"QuestOperate",
		methodName: "pauseQuest",
		data: {questId:id},
		success:function(data){
			 job_refresh(id);
		}
	});
}

function job_resume(id){
	$.WSVisit({
		type:"QuestOperate",
		methodName: "resumeQuest",
		data: {questId:id},
		success:function(data){
			 job_refresh(id);
		}
	});
}
function job_reinit(id){
	$.WSVisit({
		type:"QuestOperate",
		methodName: "reinitQuest",
		data: {questId:id},
		success:function(data){
			 job_refresh(id);
		}
	});
}
function job_chunkdetails(id){
	openWindow('chunkdetails.htm?id='+id,500,350,'');
}
function job_delete(id){
	$.WSVisit({
		type:"QuestOperate",
		methodName: "deleteQuest",
		data: {questId:id},
		success:function(data){
			refreshAllJobs();
		}
	});
}