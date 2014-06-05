package main.foundation.evaluation;

import java.util.ArrayList;
import java.util.Date;

import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.exception.ExecutionException;

import org.apache.commons.logging.LogFactory;

/**
 * This methods uses all the cases as queries. It executes so cycles as cases in
 * the case base. In each cycle one case is used as query.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 2.1
 */
public class LMSLeaveOneOutEvaluator extends LMSEvaluator {

	protected StandardCBRApplication app;

	@Override
	public void init(StandardCBRApplication cbrApp) {

		report = new LMSEvaluationReport();
		app = cbrApp;
		try {
			app.configure();
		} catch (ExecutionException e) {
			LogFactory.getLog(this.getClass()).error(e);
		}
	}

	/**
	 * Performs the Leave-One-Out evaluation. For each case in the case base,
	 * remove that case from the case base and use it as query for that cycle.
	 */
	public void LeaveOneOut() {
		try {
			java.util.ArrayList<CBRCase> aux = new java.util.ArrayList<CBRCase>();

			long t = (new Date()).getTime();
			int numberOfCycles = 0;

			// Run the precycle to load the case base
			LogFactory.getLog(this.getClass()).info("Running precycle()");
			CBRCaseBase caseBase = app.preCycle();

			if (!(caseBase instanceof jcolibri.casebase.CachedLinealCaseBase))
				LogFactory
						.getLog(this.getClass())
						.warn("Evaluation should be executed using a cached case base");

			ArrayList<CBRCase> cases = new ArrayList<CBRCase>(
					caseBase.getCases());

			jcolibri.util.ProgressController.init(getClass(),
					"Cross-Validation (LeaveOneOut)", cases.size());

			// For each case in the case base
			for (CBRCase _case : cases) {

				// Delete the case in the case base
				aux.clear();
				aux.add(_case);
				caseBase.forgetCases(aux);

				// Run the cycle
				LogFactory.getLog(this.getClass()).info(
						"Running cycle() " + numberOfCycles);
				app.cycle(_case);

				// Recover case base
				caseBase.learnCases(aux);

				numberOfCycles++;
				jcolibri.util.ProgressController.step(getClass());
			}

			// Run PostCycle
			LogFactory.getLog(this.getClass()).info("Running postcycle()");
			app.postCycle();

			jcolibri.util.ProgressController.finish(getClass());

			t = (new Date()).getTime() - t;

			// complete evaluation report
			report.setTotalTime(t);
			report.setNumberOfCycles(numberOfCycles);

		} catch (ExecutionException e) {
			LogFactory.getLog(this.getClass()).error(e);
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------------------//

}