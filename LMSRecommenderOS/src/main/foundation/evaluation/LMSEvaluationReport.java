package main.foundation.evaluation;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import jcolibri.evaluation.EvaluationReport;
import jcolibri.method.retrieve.RetrievalResult;

/**
 * This class stores the result of an evaluation. It is configured and filled by
 * an Evaluator. This info is also used to represent graphically the result of
 * an evaluation. The stored information can be:
 * <ul>
 * <li>Several series of data. The lengh of the series is the number of executed
 * cycles. And several series can be stored using diferent labels.
 * <li>Any other information. The put/getOtherData() methods allow to store any
 * other kind of data.
 * <li>Number of cycles.
 * <li>Total evaluation time.
 * <li>Time per cycle.
 * </ul>
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 2.1
 */
public class LMSEvaluationReport extends EvaluationReport {
	private long totalTime;
	private int numberOfCycles;

	/** Stores the series info */
	protected Hashtable<String, Vector<Double>> data;

	/** Stores other info */
	protected Hashtable<String, String> other;

	/** Default constructor */
	public LMSEvaluationReport() {
		data = new Hashtable<String, Vector<Double>>();
		other = new Hashtable<String, String>();
	}

	/**
	 * Gets the evaluation info identified by the label
	 * 
	 * @param label
	 *            Identifies the evaluation serie.
	 */
	@Override
	public Vector<Double> getSeries(String label) {
		return data.get(label);
	}

	/**
	 * Sets the evaluation info
	 * 
	 * @param label
	 *            Identifier of the info
	 * @param evaluation
	 *            Evaluation result
	 */
	@Override
	public void setSeries(String label, Vector<Double> evaluation) {

		data.put(label, evaluation);
	}

	@Override
	public void addDataToSeries(String label, Double value) {
		Vector<Double> v = data.get(label);
		if (v == null) {
			v = new Vector<Double>();
			data.put(label, v);
		}
		v.add(value);
	}

	public void addDataToSeries(String label, Collection<RetrievalResult> values) {
		Vector<Double> v = data.get(label);
		if (v == null) {
			v = new Vector<Double>();
			data.put(label, v);
		}
		Double avgEval = calcAverageEval(values);
		avgEval = Math.abs(avgEval);
		v.add(avgEval);
	}

	public Double calcAverageEval(Collection<RetrievalResult> evals) {
		Double sumEval = 0.0;
		for (RetrievalResult eval : evals) {
			sumEval += eval.getEval();
		}
		if (sumEval.equals(Double.NaN)) {
			sumEval = 0.0;
		}
		return evals.size() != 0 ? sumEval / evals.size() : 0.0;
	}

	/**
	 * Returns the name of the contained evaluation series
	 */
	@Override
	public String[] getSeriesLabels() {
		Set<String> set = data.keySet();
		String[] res = new String[set.size()];
		int i = 0;
		for (String e : set)
			res[i++] = e;
		return res;
	}

	@Override
	public void putOtherData(String label, String data) {
		this.other.put(label, data);
	}

	@Override
	public String getOtherData(String label) {
		return this.other.get(label);
	}

	@Override
	public String[] getOtherLabels() {
		Set<String> set = other.keySet();
		String[] res = new String[set.size()];
		int i = 0;
		for (String e : set)
			res[i++] = e;
		return res;
	}

	@Override
	public int getNumberOfCycles() {
		return numberOfCycles;
	}

	@Override
	public void setNumberOfCycles(int numberOfCycles) {
		this.numberOfCycles = numberOfCycles;
	}

	@Override
	public long getTotalTime() {
		return totalTime;
	}

	@Override
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	@Override
	public double getTimePerCycle() {
		return (double) this.totalTime / (double) numberOfCycles;
	}

	/**
	 * Checks if the evaluation series are correct. This is: all them must have
	 * the same length
	 */
	@Override
	public boolean checkData() {
		boolean ok = true;
		int l = -1;
		for (Enumeration<Vector<Double>> iter = data.elements(); iter
				.hasMoreElements() && ok;) {
			Vector<Double> v = iter.nextElement();
			if (l == -1)
				l = v.size();
			else
				ok = (l == v.size());
		}
		return ok;
	}

	private double calcAvgSimilarity(Vector<Double> v) {
		Double totalSimilarity = 0.0;
		for (Double d : v) {
			totalSimilarity += d;
		}
		return totalSimilarity /= v.size();
	}

	@Override
	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("Series:\n");
		String[] series = this.getSeriesLabels();
		for (int i = 0; i < series.length; i++) {
			Vector<Double> v = this.getSeries(series[i]);
			s.append("\n  Total " + series[i] + ":\n\t" + calcAvgSimilarity(v));
			s.append("\n  " + series[i] + ": \n    ");
			for (Double d : v) {
				s.append("\t" + d + "\n");
			}
		}

		s.append("\nOther data:\n");
		String[] other = this.getOtherLabels();
		for (int i = 0; i < other.length; i++) {
			s.append("  " + other[i] + ": " + this.getOtherData(other[i])
					+ "\n");
		}

		s.append("\nNumber of Cycles: \n\t" + this.getNumberOfCycles());
		s.append("\nTime per Cycle:   \n\t" + this.getTimePerCycle() + " ms");
		s.append("\nTotal time:       \n\t" + this.getTotalTime() + " ms");

		return s.toString();
	}

}
