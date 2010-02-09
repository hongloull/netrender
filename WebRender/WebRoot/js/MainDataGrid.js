function DataGridinit() {
//NodeDataGrid init
	nodeDataGrid = new Bs_DataGrid('nodeDataGrid');
	nodeDataGrid.buttonsDefaultPath = './image/';
	nodeDataGrid.bHeaderFix = true;
//	nodeDataGrid.rowClick = new Object( {key:2, baseUrl:'./NodeId='} );
	nodeDataGrid.rightClick = new Object( {key:2,MenuType:'NodeMenu'} );
	nodeDataGrid.setHeaderProps(
		new Array( 
			{text:'HostName'}, 
			{text:'IpAddress'},
			{text:'HostId'},
			{text:'Status'},
			{text:'Priority'},
			{text:'Platfom'},
			{text:'ProcNum'},
			{text:'CpuUsage'},
			{text:'RAMUsage'},
			{text:'JobName'},
			{text:'Frames'},
			{text:'RealLog'}
		)
	);
	var nodedata = new Array();
	nodeDataGrid.setData(nodedata);
	nodeDataGrid.drawInto('nodeDivDataGrid');

//JobDataGrid init
	jobDataGrid = new Bs_DataGrid('jobDataGrid');
	jobDataGrid.buttonsDefaultPath = './image/';
	jobDataGrid.bHeaderFix = true;
	jobDataGrid.rowClick = new Object( {key:1, baseUrl:'./jobDetail.htm?JobId='} );
	jobDataGrid.rightClick = new Object( {key:1,MenuType:'JobMenu'} );
	jobDataGrid.setHeaderProps(
		new Array(
			{text:'JobName'}, 
			{text:'JobId'},
			{text:'Status'},
			{text:'Priority'},
			{text:'Progress'},
			{text:'Filename'},
			{text:'Frames'},
			{text:'TotalFrame'},
			{text:'Pool'},
			{text:'UserInstances'},
			{text:'Submitted by'},
			{text:'CommitTime'}
		)
	);
	var jobdata = new Array()
	jobDataGrid.setData(jobdata);
	jobDataGrid.drawInto('jobDivDataGrid');
	
	refreshAllNodes();
	refreshAllJobs();
}

function cleanNodes(){
	var tagElm = document.getElementById('nodeDivDataGrid');
    if (!tagElm) return false;
    tagElm.innerHTML = '<img src="./image/ajax-loader.gif"></img>';
	nodeDataGrid.setData(new Array());
	nodeDataGrid.drawInto('nodeDivDataGrid');
}

function addNodeElement(HostName, IpAddress,HostId,Status,Priority,Platfom,ProcNum,CpuUsage,RAMUsage,JobName,Frames,RealLog) {
	nodeDataGrid.addRow(new Array(HostName, IpAddress,HostId,Status,Priority,Platfom,ProcNum,CpuUsage,RAMUsage,JobName,Frames,RealLog));
}

function updateNodeElement(HostName, IpAddress,HostId,Status,Priority,Platfom,ProcNum,CpuUsage,RAMUsage,JobName,Frames,RealLog){
	var unitData = new Array(HostName, IpAddress,HostId,Status,Priority,Platfom,ProcNum,CpuUsage,RAMUsage,JobName,Frames,RealLog)
	var flag = false;
	for (i = 0; i<nodeDataGrid._data.length; i++ )
	{
		if ( nodeDataGrid._data[i][2] == unitData[2] ){
			nodeDataGrid._data[i] = unitData;
			flag = true;
		}
	}
	if(flag==true) nodeDataGrid.drawInto('nodeDivDataGrid');
	else nodeDataGrid.addRow(unitData);
}

function refreshAllNodes(){
	var tagElm = document.getElementById('nodeDivDataGrid');
    if (!tagElm) return false;
    tagElm.innerHTML = '<img src="./image/ajax-loader.gif" align="middle"></img>';
	nodeDataGrid.setData(new Array());
	node_refreshall();
}

function cleanJobs(){
	var tagElm = document.getElementById('jobDivDataGrid');
    if (!tagElm) return false;
    tagElm.innerHTML = '<img src="./image/ajax-loader.gif" align="middle"></img>';
	jobDataGrid.setData(new Array());
	jobDataGrid.drawInto('jobDivDataGrid');
}
function addJobElement(JobName,JobId,Status,Priority,Progress,Filename,StartFrame,EndFrame,TotalFrame,Pool,UserInstances,Submitted,CommitTime) {
	jobDataGrid.addRow(new Array(JobName,JobId,Status,Priority,Progress,Filename,StartFrame,EndFrame,TotalFrame,Pool,UserInstances,Submitted,CommitTime));
}

function updateJobElement(JobName,JobId,Status,Priority,Progress,Filename,StartFrame,EndFrame,TotalFrame,Pool,UserInstances,Submitted,CommitTime){
	var unitData = new Array(JobName,JobId,Status,Priority,Progress,Filename,StartFrame,EndFrame,TotalFrame,Pool,UserInstances,Submitted,CommitTime)
	var flag = false;
	for (i = 0; i<jobDataGrid._data.length; i++ )
	{
		if ( jobDataGrid._data[i][1]== unitData[1] ){
			jobDataGrid._data[i] = unitData;
			flag = true;
		}
	}
	if(flag==true) jobDataGrid.drawInto('jobDivDataGrid');
	else jobDataGrid.addRow(unitData);
}

function deleteJobElement(JobId){
	for (i = 0; i<jobDataGrid._data.length; i++ )
	{
		if ( jobDataGrid._data[i][1]== JobId ){
			jobDataGrid.removeRow(i+1) ;
			return;
		}
	}
}

function refreshAllJobs(){
	var tagElm = document.getElementById('jobDivDataGrid');
    if (!tagElm) return false;
    tagElm.innerHTML = '<img src="./image/ajax-loader.gif"></img>';
	jobDataGrid.setData(new Array());
	job_refreshall();
}


