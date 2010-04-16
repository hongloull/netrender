package com.webrender.protocol.handler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.mina.common.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;
import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.StatusDAO;
import com.webrender.protocol.enumn.EOPCODES;
import com.webrender.protocol.enumn.EOPCODES.CODE;
import com.webrender.protocol.processor.IClientProcessor;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;

public class MessageHandlerImpl implements MessageHandler {
	private static MessageHandler instance;
    protected MessageHandlerImpl() {Runtime.getRuntime().traceMethodCalls(true);}
    
    public static synchronized MessageHandler getInstance() {
        if(instance == null) {
            MessageHandlerImpl.instance = new MessageHandlerImpl();
        }
        return instance;
    }
    private final Log LOG = LogFactory.getLog(MessageHandlerImpl.class);
	public ByteBuffer parseClientPacket(CODE code, ByteBuffer packet,
			IClientProcessor processor) {
//		LOG.info("CODEID: "+ code.getId());
//		LOG.info("ProcessorID:"+ ((NodeMachine)processor).getId());
		int initialPositon = packet.position();
		try {
			
			ArrayList<String> datas = new ArrayList<String>();
			
			int headLength = (int)packet.get();
			if(headLength==0 ){  // 无参数
				processor.parseDatas(code, new byte[0], datas);
				return null;
			}
		// 有参数时
		

			byte[] head = new byte[headLength];
			packet.get(head);
			ByteBuffer headBuffer = ByteBuffer.wrap(head);
			ByteBuffer fmts = ByteBuffer.allocate(headLength);
			while(headBuffer.hasRemaining()){
				char fmt = (char)headBuffer.get();
				switch(fmt){
				case 'i':
				case 'I':
					fmts.put((byte)'i');
					datas.add( (new Integer(packet.getInt())).toString() ) ;
					break;
				case 's':
				case 'S':
					fmts.put((byte)'s');
					int length = headBuffer.getInt(); 
//					LOG.info("length:"+ length);
					byte[] bytes = new byte[length];
					packet.get(bytes);
//					LOG.info(new String(bytes));
//					LOG.info(new String(bytes,"GBK"));
//					LOG.info(new String(bytes,"UTF-16"));
					String data = new String(bytes);
					datas.add(data);
					break;
				}	
			}
			fmts.flip();
			byte[] byteFmts = new byte[fmts.limit()];
			fmts.get(byteFmts);
			processor.parseDatas(code, byteFmts, datas);
			
			return null;
		} catch (java.nio.BufferUnderflowException e) {
			//解析到不完整包 数据未完
			packet.position( (initialPositon-1) );
			int remaining = packet.remaining();
			byte[] bytes = new byte[remaining];
			packet.get(bytes);
			ByteBuffer remainBuffer = ByteBuffer.allocate(remaining);
			remainBuffer.put(bytes);
			return remainBuffer;
		}
//		finally{
//			LOG.info("ProcessorID:"+ ((NodeMachine)processor).getId()+" finish");
//		}

	}
	
	/**
	 * N_RUN  HeadLength   i    s    i        s     i     s       i       s         i     s       i     N_STATE     i
                int       ID   IP  length   Name  len   Prority  len   ThreadNum   len   Version  len   Subcode   CommandId
	 */
	public int initialClient(ByteBuffer packet) {
		// i:nodeId s:name s:ip s:priority s:threads
		int headLength = (int)packet.get();
		byte[] head = new byte[headLength];
		// head
		packet.get(head);
		ByteBuffer headBuffer = ByteBuffer.wrap(head);
		if(headBuffer.get()!= 'i'){
			LOG.error("initialNodePkt fmt error : [HEAD]1!=i ID");
			return 0;
		}
		if(headBuffer.get()!= 's'){
			LOG.error("initialNodePkt fmt error : [HEAD]2!=s NAME" );
			return 0;
		}
		int nameLength = headBuffer.getInt();
		if ( headBuffer.get()!='s'){
			LOG.error("initialNodePkt fmt error : [HEAD]3!=s IP");
			return 0;
		}
		int ipLength = headBuffer.getInt();
		if ( headBuffer.get()!='s'){
			LOG.error("initialNodePkt fmt error : [HEAD]4!=s Priority");
			return 0;
		}
		int priLength = headBuffer.getInt();
		if ( headBuffer.get()!='s'){
			LOG.error("initialNodePkt fmt error : [HEAD]5!=s ThreadNum");
			return 0;
		}
		int threadsLength = headBuffer.getInt();
		String version = null;
		int versionLength = 0;
		int state = EOPCODES.getInstance().get("N_RUN").getSubCode("N_PAUSE").getId();
		int commandId = 0;
		if(headBuffer.remaining() > 0){
			if( headBuffer.get()!='s' ){
				LOG.error("initialNodePkt fmt error : [HEAD]6!=s Version");
				return 0;
			}
			versionLength = headBuffer.getInt();
			state = headBuffer.get();
			commandId = headBuffer.getInt();
		}
		// body
		int nodeId = packet.getInt();
		byte[] name = new byte[ nameLength ];
		packet.get(name);
		byte[] ip = new byte[ipLength];
		packet.get(ip);
		byte[] pri = new byte[priLength];
		packet.get(pri);
		byte[] threads = new byte[threadsLength];
		packet.get(threads);
		if(versionLength>0){
			byte[] versions = new byte[versionLength];
			packet.get(versions);
			version = new String(versions);
		}else{
			version = "1.1";
		}
		// pkt parse finish.
		
		
		String strName=null;
		String strIp = new String(ip);
		String strPri = new String(pri);
		String strThreads = new String(threads);
		try {
			strName = new String(name,"UTF8");
		} catch (UnsupportedEncodingException e){
			strName = new String(name);
			e.printStackTrace();
		}
//				判断是否有nodeId节点连接到服务器上，如果有重新分配
		NodeMachine nodeMachine = NodeMachineManager.getInstance().getNodeMachine(nodeId);
		if( nodeMachine != null && nodeMachine.isConnect()==true){
			LOG.info("nodeId: "+nodeId+" has run.");
			nodeId = 0;
		}
		NodeDAO nodeDAO = new NodeDAO();
		Transaction tx = null;
		try{
			tx = HibernateSessionFactory.getSession().beginTransaction();
//			LOG.info(nodeId+"---"+strIp+"---"+strName+"---"+strPri+"---"+strThreads);
			Node node = nodeDAO.runNode(nodeId,strIp, strName,strPri,strThreads);
			
			StatusDAO statusDAO = new StatusDAO();
			ExecutelogDAO exeDAO = new ExecutelogDAO();
			Executelog log = new Executelog(null,statusDAO.findById(90),node,strIp+"---"+strName+": is connecting..",new Date());
			exeDAO.save(log);
			
			tx.commit();
			int saveNodeId = node.getNodeId();
			nodeMachine = NodeMachineManager.getInstance().getNodeMachine(saveNodeId);
			nodeMachine.initial(version,state,commandId);
			return saveNodeId;
		}catch(Exception e){
			LOG.error("RunSaveNode fail nodeId:"+nodeId, e);
			return 0;
		}
		
		
		
		
		
	}

}
