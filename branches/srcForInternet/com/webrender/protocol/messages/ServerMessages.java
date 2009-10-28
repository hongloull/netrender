package com.webrender.protocol.messages;

import org.apache.mina.common.ByteBuffer;
import com.webrender.protocol.enumn.EOPCODES;
import com.webrender.protocol.enumn.EOPCODES.CODE;
import com.webrender.server.Conversion;

public class ServerMessages {
	public ByteBuffer createCommandPkt(CODE commandType,Integer commandId, String cmdString) throws Exception{
		char[] fmt = {'b','i','s'};
		String[] datas = {(char)commandType.getId()+"",commandId.toString(),cmdString};
		return createPkt(EOPCODES.getInstance().get("S_COMMAND"),fmt,datas);
	}
	public ByteBuffer createSystemPkt(CODE code) throws Exception{
		char[] fmt = {'b'};
		String[] datas = {(char)code.getId()+""};
		return createPkt(EOPCODES.getInstance().get("S_SYSTEM"),fmt,datas);
	}
	public ByteBuffer createPathConfigPkt(String pathConfig) throws Exception{
		char[] fmt = {'s'};
		String[] datas = {pathConfig};
		return createPkt(EOPCODES.getInstance().get("S_PATHCONFIG"),fmt,datas);
		
	}
	public ByteBuffer createStatusPkt() throws Exception{
		return createPkt(EOPCODES.getInstance().get("S_GETSTATUS"), null, null);
	}
	public ByteBuffer createConnectFlagPkt(Integer nodeId) throws Exception{
		char[] fmt = {'i'};
		String[] datas = {nodeId.toString()};
		return createPkt(EOPCODES.getInstance().get("S_SETNODEID"),fmt,datas);
	}
	public ByteBuffer createWantConfigPkt() throws Exception{
		return createPkt(EOPCODES.getInstance().get("S_GETCONFIG"), null, null);
	}
	public ByteBuffer createSetConfigPkt(String config) throws Exception {
		char[] fmt = {'s'};
		String[] datas = {config};
		return createPkt(EOPCODES.getInstance().get("S_SETCONFIG"),fmt,datas);
	}
	public ByteBuffer createServerStatusPkt() throws Exception {
		char[] fmt = {'b'};
		String[] datas = {(char)Conversion.getInstance().getStatus().getId()+""};
		return createPkt(EOPCODES.getInstance().get("S_SERVERSTATUS"),fmt,datas);
	}
	public ByteBuffer createErrorPkt(String message) throws Exception{
		char[] fmt = {'s'};
		String[] datas = {message};
		return createPkt(EOPCODES.getInstance().get("S_ERROR"),fmt,datas);
	}
	
	/**
	 * 编码函数 协议如下：
	 * ID顺序      说明                                       长度
	 * 1：      code 函数操作码                                1B
	 * 2：      包头长度 字节数                                 1B
	 * -----------------包头开始------------------------------------------
	 * 3：      参数类型 i/I表示整型 s/S表示字符串             若参数个数为3 类型为 iiss时 长度为 1+1+(1+4)+(1+4)
	 * -----------------包头结束---------------------------------------
	 * 4:       参数值                                     由ID 3决定 如iisS时 长度为 4+4+s.length+S.length    
	 * @param code 
	 * @param fmt
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public ByteBuffer createPkt(CODE code,char[] fmts,String[] data ) throws Exception{
		//只传CODE时，无参。
		if(fmts == null || fmts.length==0){
			byte[] bytes = new byte[1];
			ByteBuffer buffer = ByteBuffer.wrap(bytes);
			buffer.put((byte)code.getId() );
			buffer.flip();
			return buffer;
		}
		
		// 有参数时
		int bytesLength = fmts.length;
		int totalLength = 2;
		int headLength = 0;
		
		if( bytesLength != data.length){
			throw new Exception("fmt length must equal with data length");
		}
//		
		ByteBuffer headBuffer = ByteBuffer.allocate(127); 
		for(int i=0 ; i<bytesLength ;i++){
			switch (fmts[i]){
			case 'i': 
			case 'I':
				headBuffer.put((byte)'i');
				totalLength+=(1+4);
				headLength++;
				break;
			case 's':
			case 'S':
				headBuffer.put((byte)'s');
				int sLength = data[i].getBytes("utf-8").length;
				headBuffer.putInt(sLength);
				totalLength+=(5+sLength);
				headLength+=5;
				break;
			case 'b':
			case 'B':
				headBuffer.put((byte)'b');
				totalLength+=(1+1);
				headLength++;
			default: break;
			}
		}
		headBuffer.flip();
		ByteBuffer buffer = ByteBuffer.allocate(totalLength);
		buffer.put((byte)code.getId());
		buffer.put((byte)headLength);
		buffer.put(headBuffer);
		for(int i=0;i<bytesLength;i++){
			switch(fmts[i]){
			case 'i':
			case 'I':
				buffer.putInt(Integer.parseInt(data[i]) );
				break;
			case 's':
			case 'S':
				byte[] bytesData = data[i].getBytes("utf-8");
				buffer.put(bytesData);
				break;
			case 'b':
			case 'B':
				if(data[i].getBytes().length==1){
					byte subCode = (byte) data[i].charAt(0); 
					buffer.put(subCode);				
				}
				else{
					throw new Exception("SubCode error");
				}
			}
		}
		buffer.flip();
		return buffer;
	}
}
