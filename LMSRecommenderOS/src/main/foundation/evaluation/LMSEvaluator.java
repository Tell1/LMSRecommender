package main.foundation.evaluation;

import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.evaluation.Evaluator;

/**
 * This abstract class defines the common behaviour of an evaluator.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 * 
 */
public abstract class LMSEvaluator extends Evaluator {

	public static final double MAX_CYCLES = 300;

	/**
	 * Initializes the evaluator with the CBR application to evaluate
	 * 
	 * @see jcolibri.cbraplications.StandardCBRApplication
	 * */
	@Override
	public abstract void init(StandardCBRApplication cbrApp);

	/** Object that stores the evaluation results */
	protected static LMSEvaluationReport report;

	/** Returns the evaluation report */
	public static LMSEvaluationReport getEvaluationReport() {
		return report;
	}

	/** Sets the evaluation report */
	public static void setEvaluationReport(LMSEvaluationReport report) {
		LMSEvaluator.report = report;
	}

	public static double getMaxCycles() {
		return MAX_CYCLES;
	}

	/**
	 * Checks if the limit of 300 cycles to evaluate is reached. Furthermore
	 * internally calculates a accurate derivation depending on the numCycles
	 * parameter and returns true if the evalCount hits the derivation otherwise
	 * false. 
	 * <p>
	 * Note: This evaluator can not handle more then 3000 evaluations
	 * (data sets).
	 * 
	 * @return internally calculates a accurate derivation depending on the
	 *         numCycles parameter and returns true if the evalCount hits the
	 *         derivation otherwise false.
	 */
	public static boolean isEvalLimit(int evalCount, double numCycles) {
		return numCycles > MAX_CYCLES ? (evalCount % Math.round(numCycles
				/ MAX_CYCLES)) != 0 : numCycles == MAX_CYCLES;
	}
}
