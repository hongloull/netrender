/***
DreamCore - JsLib/Menu
Date     : Dec 03, 2006
Copyright: DreamSoft Co.,Ltd.
Mail     : [email]Dream@Dreamsoft.Ca[/email]
Author   : Egmax
Browser : IE5.0&+,Firefox1.5&+,Netscape7.0&+
Update:
***/
 
if(!document.all) document.captureEvents(Event.MOUSEDOWN);
var _Tmenu = 0;
var _Amenu = 0;
var _Type = 'A';
document.onclick = _Hidden;
function _Hidden()
{
    if(_Tmenu==0) return;
    document.getElementById(_Tmenu).style.visibility='hidden';
    _Tmenu=0;
}
 
document.oncontextmenu = function (e)
{
    _Hidden();
    var _Obj = document.all ? event.srcElement : e.target;
    if(_Type.indexOf(_Obj.tagName) == -1) return;
    _Amenu = _Obj.getAttribute('menu');
    if(_Amenu == null || _Amenu == 'null') return;
    if(document.all) e = event;
    _ShowMenu(_Amenu, e);
    return false;
}
 
function _ShowMenu(Eid, event)
{
	typeAndId = Eid.split('_');
	if(typeAndId.length != 2) return;
	type = typeAndId[0];
	id   = typeAndId[1];
	var _Menu = document.getElementById(type);
//    var _Menu = document.getElementById(Eid);
	if(type=='NodeMenu'){
		document.getElementById('node_menu_pause').href ="javascript:node_pause(" + id + ")";
		document.getElementById('node_menu_resume').href ="javascript:node_resume(" + id + ")";
		document.getElementById('node_menu_killpause').href ="javascript:node_killpause(" + id + ")";
		document.getElementById('node_menu_killgoon').href ="javascript:node_killgoon(" + id + ")";
		document.getElementById('node_menu_logoff').href ="javascript:node_logoff(" + id + ")";
		document.getElementById('node_menu_logon').href ="javascript:node_logon(" + id + ")";
		document.getElementById('node_menu_openlog').href ="javascript:node_openlog(" + id + ")";
		document.getElementById('node_menu_logonandopen').href ="javascript:node_logonandopen(" + id + ")";
		document.getElementById('node_menu_softrestart').href ="javascript:node_softrestart(" + id + ")";
		document.getElementById('node_menu_restart').href ="javascript:node_restart(" + id + ")";
		document.getElementById('node_menu_poweroff').href ="javascript:node_poweroff(" + id + ")";
		document.getElementById('node_menu_remotecontrol').href ="javascript:node_remotecontrol(" + id + ")";
		document.getElementById('node_menu_refresh').href ="javascript:node_refresh(" + id + ")";
		
	}
    else if(type=='JobMenu'){
    	document.getElementById('job_menu_pause').href ="javascript:job_pause(" + id + ")";
		document.getElementById('job_menu_resume').href ="javascript:job_resume(" + id + ")";
		document.getElementById('job_menu_reinit').href ="javascript:job_reinit(" + id + ")";
		document.getElementById('job_menu_chunkdetails').href ="javascript:job_chunkdetails(" + id + ")";
		document.getElementById('job_menu_delete').href ="javascript:job_delete(" + id + ")";
		document.getElementById('job_menu_refresh').href ="javascript:job_refresh(" + id + ")";
		
		
    }
    var _Left = event.clientX + document.body.scrollLeft;
    var _Top = event.clientY + document.body.scrollTop;
    _Menu.style.left = _Left.toString() + 'px';
    _Menu.style.top = _Top.toString() + 'px';
    _Menu.style.visibility = 'visible';
	_Tmenu = type;
//    _Tmenu = Eid;
}
 
/***

***/

_Type='INPUT,A,DIV,BODY,IMG,TR,TD';