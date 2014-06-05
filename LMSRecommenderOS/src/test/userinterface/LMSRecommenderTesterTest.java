package test.userinterface;

import main.foundation.evaluation.LMSRecommenderEvaluater.EvalType;
import main.userinterface.LMSRecommenderTester;

import org.junit.Test;
/**
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 *
 */

public class LMSRecommenderTesterTest {

	public static final int NUM_RECOMMENDER = 4;
	
	@Test
	public void testRunEvaluater() {
		LMSRecommenderTester main = new LMSRecommenderTester("main/userinterface/examples.config");
		main.setVisible(true);
		try {
			Thread.sleep(1000);
			for (int i = NUM_RECOMMENDER - 1; i >= 0; i--) {
				main.list.setSelectedIndex(i);
				main.setExample(i);
				Thread.sleep(1000);
			}
			Thread.sleep(1000);
			main.runEvaluater(EvalType.LEAVE_ONE_OUT);
			Thread.sleep(10000);
			while(!main.eval.isEnabled());
			main.runEvaluater(EvalType.HOLD_OUT);
			Thread.sleep(10000);
			while(!main.cVal.isEnabled());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
