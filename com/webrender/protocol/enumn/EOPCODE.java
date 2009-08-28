package com.webrender.protocol.enumn;

public enum EOPCODE {
	/* 
	 *server to node opcodes
	 */ 
    SYSTEM,COMMAND,STATUS,CONNECTFLAG,WANTCONFIG,SETCONFIG,SERVERSTATUS,
    /* 
     * node to server opcodes
     */
    RUN,READY,FEEDBACK,STATUSINFO,CONFIGINFO,WANTSERVERSTATUS
    /* 
     * command opcodes
     */
}
