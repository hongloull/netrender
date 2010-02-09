function job_detail(jobId){
	$.WSVisit({
		type:"QuestOperate",
		methodName: "getDetail",
		data: {questId :jobId},
		success:function(data){
   			var doc = $.createXMLDocument(data);
   			$(doc).find('Quest').each(function(){
   				fillJobAttrs(   $(this).attr("questName")+'',
   								$(this).attr("pri")+'',
   								$(this).attr("regName")+'',
   								$(this).attr("maxNodes")+'',
   								$(this).attr("commitTime")+'',
   								$(this).attr("packetSize")+'',
   								$(this).attr("Nodes")+''
   				);
   			});
   			
   			$(doc).find('Commandmodel').each(function(){
   				commandModelName = $(this).attr("commandModelName")+'';
   				initModel(commandModelName);
   			});
   			$(doc).find('Questarg').each(function(){
   				addJobArg( $(this).attr("argInstruction")+'',
   						$(this).attr("value")+''
   				);
   			});
   		}
	});
}
