package test.logic;

import java.util.Iterator;
import org.junit.Test;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;
import com.webrender.logic.CalcFrame;

public class testCalcFrame {
	@Test
	public void test()
	{
		QuestDAO questDAO = new QuestDAO();
		Quest quest = questDAO.findById(53);
		CalcFrame c = new CalcFrame();
		
	}
}
