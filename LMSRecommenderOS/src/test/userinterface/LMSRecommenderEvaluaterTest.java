package test.userinterface;

import main.domain.StandardLMSRecommender;
import main.foundation.evaluation.LMSRecommenderEvaluater;
import main.foundation.evaluation.LMSRecommenderEvaluater.EvalType;

import org.junit.Test;
/**
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 *
 */
public class LMSRecommenderEvaluaterTest {

	@Test
	public void testEvalRecommender() throws InterruptedException {
		StandardLMSRecommender[] recArr = {
				main.domain.filterBasedRecommender.LMSRecommender.getInstance(),
				main.domain.contentBasedRecommender.LMSRecommender
						.getInstance(),
				main.domain.collaborativeRecommender.LMSRecommender
						.getInstance() };
		for (StandardLMSRecommender standardLMSRecommender : recArr) {
			LMSRecommenderEvaluater.evalRecommender(standardLMSRecommender,EvalType.LEAVE_ONE_OUT);
			Thread.sleep(1000);
		}
		for (StandardLMSRecommender standardLMSRecommender : recArr) {
			LMSRecommenderEvaluater.evalRecommender(standardLMSRecommender,EvalType.HOLD_OUT);
			Thread.sleep(1000);
		}
	}

}
