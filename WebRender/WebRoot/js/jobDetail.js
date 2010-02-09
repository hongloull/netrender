function JobDetailInit(){
	 var jobId = findParam("JobId");
	 if( jobId == "0")
	 {
	 	alert("JobId error");
	 	return;
	 }
	 job_detail(jobId);
}

function fillJobAttrs(questName,pri,regName,maxNodes,commitTime,packetSize,Nodes){
	$("#questName").text(questName);
	$("#pri").text(pri);
	$("#regName").text(regName);  
	$("#maxNodes").text(maxNodes);
	$("#commitTime").text(commitTime);
	$("#packetSize").text(packetSize);
	$("#Nodes").text(Nodes);
}

function initModel(commandModelName){
	$("#modleName").text(commandModelName+"");
}
function addJobArg(name,value){
	$("#modelInfo").append( "<tr><td class='JobArg'>"+name+"</td><td>"+value+"</td></tr>");
	
}

