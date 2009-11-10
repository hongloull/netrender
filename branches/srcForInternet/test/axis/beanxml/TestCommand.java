package test.axis.beanxml;

import java.util.List;

import org.junit.Test;

import com.webrender.axis.beanxml.CommmandUtils;
import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;

public class TestCommand {

	@Test
	public void testCommand(){
		CommmandUtils commmandUtils = new CommmandUtils();
		CommandDAO commandDAO = new CommandDAO();
		List list = commandDAO.findAll();
		Command command = (Command) list.get(list.size()-1);
		System.out.println( commmandUtils.commandToXMLForExe(command) );
	}
}
