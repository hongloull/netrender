package com.webrender.protocol.enumn;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.config.GenericConfig;



public final class EOPCODES{
	private static EOPCODES instance;
	private Map<String,CODE> codes = new Hashtable<String,CODE>();;
	private EOPCODES(){
		
	}
	public static EOPCODES getInstance(){
		if(instance == null){
			EOPCODES.instance = new EOPCODES();
			try {
				instance.loadFromXML();
			} catch (JDOMException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public void loadFromXML() throws JDOMException {
		String fileStr = GenericConfig.getInstance().getFile("protocol/header.xml");
		File file = new File(fileStr);
		if(file.isFile()){
			SAXBuilder sb =  new SAXBuilder();
			Document doc = sb.build(file);
			Element root = doc.getRootElement();
			List<Element> lis_Codes = root.getChildren("code");
			int codeLength = lis_Codes.size();
			for(int i =0;i<codeLength;i++){
				Element element = lis_Codes.get(i);
				String id = element.getAttributeValue("id");
				String name = element.getAttributeValue("name");
				CODE code = add(Integer.parseInt(id),name);
				if(code != null){
					List<Element> lis_SubCodes = element.getChildren("subcode");
					int subCodeLength = lis_SubCodes.size();
					for(int j =0;j<subCodeLength;j++){
						Element subElement = lis_SubCodes.get(j);
						String subId = subElement.getAttributeValue("id");
						String subName = subElement.getAttributeValue("name");
						code.addSubCode(Integer.parseInt(subId),subName);
					}					
				}
			}
			
		}
		else{
			throw new JDOMException("Cannot read protocol file!!");
		}
	}
	
	public CODE add(int id ,String name){
		if(codes.get(name)==null){
			CODE code = new CODE(id);
			codes.put(name, code);
			return code;
		}
		return null;
	}
	
	public CODE get(String code){
		return codes.get(code);
	}
	public CODE get(int id){
		Iterator<CODE> ite = codes.values().iterator();
		while(ite.hasNext()){
			CODE code = (CODE)ite.next();
			if (code.getId()==id){
				return code;
			}
		}
		return null;
	}
	
	public int size(){
		return codes.size();
	}
	
	public class CODE{
		private Map<String,CODE> subcodes=null;
		private int id ;
		public CODE(int id){
			this.id = id;
		}
		
		public CODE addSubCode(int id ,String name){
			if(subcodes == null){
				subcodes = new Hashtable<String,CODE>();
			}
			if(subcodes.get(name)==null){
				CODE code = new CODE(id);
				subcodes.put(name, code);
				return code;
			}
			return null;
		}
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}		
		public CODE getSubCode(String code){
			return subcodes.get(code);
		}
		public CODE getSubCode(int id){
			Iterator<CODE> ite = subcodes.values().iterator();
			while(ite.hasNext()){
				CODE code = (CODE)ite.next();
				if (code.getId()==id){
					return code;
				}
			}
			return null;
		}
		
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			result = prime * result
					+ ((subcodes == null) ? 0 : subcodes.hashCode());
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof CODE))
				return false;
			final CODE other = (CODE) obj;
			if (id != other.id)
				return false;
			if (subcodes == null) {
				if (other.subcodes != null)
					return false;
			} else if (!subcodes.equals(other.subcodes))
				return false;
			return true;
		}
		
		public int size(){
			return subcodes.size();
		}
	}
}


