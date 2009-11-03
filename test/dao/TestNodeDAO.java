package test.dao;

import java.util.List;

import org.junit.Test;

import com.webrender.dao.NodeDAO;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;

public class TestNodeDAO {
	@Test
	public void testGetNGIDS(){
		QuestDAO questDAO = new QuestDAO();
		Quest quest = questDAO.findById(33);
		List<Integer> list =(new NodeDAO()).getNodeGroupIds(quest);
		for(Integer id : list){
			System.out.println(id);
		}
		
	}
}
