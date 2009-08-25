package com.webrender.protocol.enumn;

public enum EOPCODE {
	/*
	 *server to node opcodes
	 */ 
    SYSTEM,COMMAND,STATUS,CONNECTFLAG,
    /* 
     * node to server opcodes
     */
    RUN,READY,FEEDBACK,STATUSINFO
    /* 
     * command opcodes
     */
}
