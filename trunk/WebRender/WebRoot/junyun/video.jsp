<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>	
	<script type="text/javascript" src="../js/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="../js/interface.js"></script>
	<script type="text/javascript" src="../js/swfobject.js"></script>
	<link href="../css/video.css" rel="stylesheet" type="text/css" />
<!--[if lt IE 7]>
 <style type="text/css">
 .dock img { behavior: url(iepngfix.htc) }
 </style>
<![endif]-->

	<title>NetRender Video Help</title>
	
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>
  
<body>

<script language="javascript">
function getQueryStringRegExp(param) {
    var query = location.search;
    var iLen = param.length;
    var iStart = query.indexOf(param);
    if (iStart == -1) return "";
    iStart += iLen + 1;
    var iEnd = query.indexOf("&", iStart);
    if (iEnd == -1) return query.substring(iStart);
    return query.substring(iStart, iEnd);
}
function loadplayer(id)
{
    var xmlUrl = new String("flvplayer.swf?file="+id);
    //alert(xmlUrl);
	var screenWidth = screen.width;
	var screenHeight = screen.height;
	var vid = new SWFObject(xmlUrl,"vid","800","600","9","#ffffff");
	vid.addParam("allowFullScreen","true");
	vid.addVariable("xmlUrl",xmlUrl);
	vid.addVariable("scrWidth",screenWidth);
	vid.addVariable("scrHeight",screenHeight);
	vid.write("flv_player");
}


var mylist=new Array();

<%
    //String path=application.getRealPath(request.getRequestURI());
    //String dir=new java.io.File(path).getParent();
    String path = application.getRealPath("/").substring(0,application.getRealPath("/").length()-1) + request.getRequestURI().substring(request.getContextPath().length());
	String dir = new java.io.File(path).getParent();
    java.io.File file = new  java.io.File(dir+"/video/");
    if(file.exists() && file.isDirectory()){
    	java.io.File[] files = file.listFiles();
    	for(java.io.File flvFile : files){
    		String name = flvFile.getName();
    		if( name.endsWith(".flv")){
    			out.println("mylist.push(\""+name.substring(0,name.length()-4)+"\")");
    		}
    	}
    }
%>
	
var maxindex=mylist.length-1;

function geturl(type)
{
    if(type<=0)
    {
        type=0;
        
    }else if (type>maxindex)
    {
		type=maxindex;
    }
	$('#TitleVideo').text(mylist[type].substring(2,mylist[type].length));
	loadplayer("video/"+mylist[type]+".flv");
}
</script>
<div align=center id="dock">
	<div class="dock-container">
	<script language=javascript>
		for(i=0;i<=maxindex;i++){
			var name = mylist[i].substring(2,mylist[i].length);
			var s='<a class="dock-item" href="javascript:geturl('+i+');"><img src="video/'+mylist[i]+'.png" alt="'+name+'" /><span>'+name+'</span></a>' ;
			document.write(s);
		}
	</script>
	</div>
</div>
   <div id="Layer1"  align="left"> <a align=left> Network Rendering Management Tool V1.0<BR>
    Technical Support:<BR>
    Mail: support@animationsp.com<BR>
    Tel: 86-21-50805042-404</a><a align=left> <br>
      ShangHai ZhangJiang Animation & Technology Co., Ltd. </a></br>
  </div>
 <div align="center">
  <br/>
  <br/>
  <br/>
  <br/>
  <br/>
  <br/>
  <br/>
  <br/>
  <br>
  <a id="TitleVideo"  align=center></a></br>
</div>
<div id="flv_player" align="center" ></div>

<script type="text/javascript">
		
	$(document).ready(
		function()
		{
			$('#dock').Fisheye(
				{
					maxWidth: 50,
					items: 'a',
					itemsText: 'span',
					container: '.dock-container',
					itemWidth: 40,
					proximity: 90,
					halign : 'center'
				}
			)
			geturl(0);
		}
	);
</script>
</body>

</html>