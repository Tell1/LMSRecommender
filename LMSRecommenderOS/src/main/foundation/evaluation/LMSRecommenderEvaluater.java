package main.foundation.evaluation;

import main.domain.StandardLMSRecommender;

/**
 * This class evaluates an StandardLMSRecommender application. It uses a CBR
 * application (a StandardCBRApplication implementation) that must store its
 * results in the EvaluationReport.
 * 
 * Note: The evaluations of the collaborative LMSRecommenders are not comparable
 * with the other content and filter-based recommenders cause of the different
 * case base structures.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 * 
 */
public class LMSRecommenderEvaluater {

	public enum EvalType {
		LEAVE_ONE_OUT, HOLD_OUT
	};

	public static void evalRecommender(StandardLMSRecommender lmsRec,
			EvalType evalType) {

		// SwingProgressBar shows the progress
		jcolibri.util.ProgressController.clear();

		switch (evalType) {
		case LEAVE_ONE_OUT:
			jcolibri.util.ProgressController.register(
					new jcolibri.test.main.SwingProgressBar(),
					LMSLeaveOneOutEvaluator.class);
			LMSLeaveOneOutEvaluator oneOutEval = new LMSLeaveOneOutEvaluator();
			oneOutEval.init(lmsRec);
			oneOutEval.LeaveOneOut();
			break;
		case HOLD_OUT:
			jcolibri.util.ProgressController.register(
					new jcolibri.test.main.SwingProgressBar(),
					LMSHoldOneOutEvaluator.class);
			LMSHoldOneOutEvaluator eval = new LMSHoldOneOutEvaluator();
			eval.init(lmsRec);
			eval.HoldOut(25, 1);
			break;
		default:
			throw new IllegalArgumentException();
		}

		if (LMSEvaluator.getEvaluationReport().checkData()) {
			main.userinterface.LMSEvaluationResultGUI.show(
					LMSEvaluator.getEvaluationReport(), lmsRec.getClass()
							.getName() + " - Evaluation", false);

		}
		LMSEvaluator.setEvaluationReport(null);
	}

}
