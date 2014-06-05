package main.domain.collaborativeFilterRecommender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import jcolibri.extensions.recommendation.collaborative.MatrixCaseBase;
import jcolibri.cbrcore.Attribute;

/**
 * Extension of the MatrixCaseBase that computes similarities among neighbors
 * using the Pearson Correlation. <br>
 * It uses a minCorrelateItems Factor to weight similar neighbors that have few
 * common correlate items.
 * <p>
 * Note: Some changes were necessary to make this case base evaluable with the
 * LMSEvaluator.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.1
 */
public class LMSPearsonMatrixCaseBase extends LMSMatrixCaseBase {

	// stores the similarity lists
	private HashMap<Integer, Collection<MatrixCaseBase.SimilarTuple>> similLists;
	// table to store the similarities
	private HashMap<Integer, HashMap<Integer, Double>> similarities;
	// stores the averages
	private HashMap<Integer, Double> averages;

	/**
	 * Factor that decreases the similarity between users who have fewer than
	 * this number of co-rated items. In other words: Decrease evaluation of the
	 * similarity of users that rated less items then this factor.
	 */
	int minCorrelateItemsFactor;

	/**
	 * Constructor
	 * 
	 * @param value
	 *            is the attribute of the result part of the case that contains
	 *            the rating
	 * @param minCorrelateItemsFactor
	 *            factor that decreases the similarity between users who have
	 *            fewer than this number of co-rated items
	 */
	public LMSPearsonMatrixCaseBase(Attribute value, int minCorrelateItemsFactor) {
		super(value);
		this.minCorrelateItemsFactor = minCorrelateItemsFactor;
	}

	@Override
	/**
	 * Returns a list of similar users to a given one in decreasing order 
	 */
	public Collection<MatrixCaseBase.SimilarTuple> getSimilar(Integer id) {
		return similLists.get(id);
	}

	@Override
	/**
	 * Returns the similarity between two users
	 */
	public double getSimil(Integer id1, Integer id2) {
		return similarities.get(id1).get(id2);
	}
	
	/**
	 * returns the ratings average for a given user
	 * 
	 * @param id
	 *            is the user
	 * @return the ratings average
	 */
	public double getAverage(int id) {
		return averages.get(id);
	}

	@Override
	/**
	 * Computes the similarity between users
	 */
	protected void computeSimilarities() {
		this.computeAverages();
		this.computeSimilarityByDescriptionId();
		this.computeSimilarityLists();
	}

	@SuppressWarnings("unchecked")
	private void computeSimilarityLists() {
		similLists = new HashMap<Integer, Collection<MatrixCaseBase.SimilarTuple>>();
		for (Integer key : similarities.keySet()) {
			ArrayList<MatrixCaseBase.SimilarTuple> list = new ArrayList<MatrixCaseBase.SimilarTuple>();
			HashMap<Integer, Double> similMap = similarities.get(key);
			for (Entry<Integer, Double> entry : similMap.entrySet())
				list.add(new MatrixCaseBase.SimilarTuple(entry.getKey(), entry
						.getValue()));
			java.util.Collections.sort(list);
			similLists.put(key, list);
		}
	}

	/**
	 * Computes the Pearson Correlation between users in a smart and efficient
	 * way. This code is an adaptation of the one developed by Jerome Kelleher
	 * and Derek Bridge for the Collaborative Movie Recommender project at
	 * University College Cork (Ireland).
	 */
	private void computeSimilarityByDescriptionId() {
		org.apache.commons.logging.LogFactory.getLog(this.getClass()).info(
				"Computing similarities");
		similarities = new HashMap<Integer, HashMap<Integer, Double>>();
		HashSet<Integer> keyCopy = new HashSet<Integer>(
				byDescriptionId.keySet());
		for (Integer me : byDescriptionId.keySet()) {
			keyCopy.remove(me);
			for (Integer you : keyCopy) {
				/** Checks for similar ratings */
				Iterator<?> ratings = new CommonRatingsIterator(me, you,
						byDescriptionId.get(me), byDescriptionId.get(you));
				double sumX = 0.0;
				double sumXSquared = 0.0;
				double sumY = 0.0;
				double sumYSquared = 0.0;
				double sumXY = 0.0;
				double numDataPoints = 0;
				// X corresponds to active, Y to predictor.
				while (ratings.hasNext()) {
					CommonRatingTuple rt = (CommonRatingTuple) ratings.next();
					double x = rt.getRating1();
					double y = rt.getRating2();
					numDataPoints++;
					sumX += x;
					sumY += y;
					sumXSquared += square(x);
					sumYSquared += square(y);
					sumXY += (x * y);
				}
				// update AbstractMovieRecommender by the correct comparison
				// count.
				// Modified to remove comparisons required for search
				// AbstractMovieRecommender.addToComparisonCount(numDataPoints);

				double correlation = 0.0;
				if (numDataPoints != 0) {
					double numerator = sumXY - ((sumX * sumY) / numDataPoints);
					double sqrt = (sumXSquared - (square(sumX) / numDataPoints))
							* (sumYSquared - (square(sumY) / numDataPoints));
					double denominator = Math.sqrt(sqrt);

					// output 0 here according to Herlocker's recommendations,
					// also watch for negative square roots (extremely rare)
					correlation = denominator == 0.0 || sqrt < 0.0 ? 0.0
							: numerator / denominator;
					correlation = correlation * numDataPoints
							/ minCorrelateItemsFactor;
				}

				HashMap<Integer, Double> mySimilList = similarities.get(me);
				if (mySimilList == null) {
					mySimilList = new HashMap<Integer, Double>();
					similarities.put(me, mySimilList);
				}
				mySimilList.put(you, correlation);

				HashMap<Integer, Double> yourSimilList = similarities.get(you);
				if (yourSimilList == null) {
					yourSimilList = new HashMap<Integer, Double>();
					similarities.put(you, yourSimilList);
				}
				yourSimilList.put(me, correlation);

			}
		}
	}

	// computes the averages
	private void computeAverages() {
		org.apache.commons.logging.LogFactory.getLog(this.getClass()).info(
				"Computing Averages");
		averages = new HashMap<Integer, Double>();
		for (Integer i : byDescriptionId.keySet()) {
			ArrayList<MatrixCaseBase.RatingTuple> list = byDescriptionId.get(i);
			double acum = 0;
			for (MatrixCaseBase.RatingTuple rt : list)
				acum += rt.getRating();
			double size = list.size();
			averages.put(i, acum / size);
		}
	}

	/**
	 * Return the square of the parameter number.
	 * 
	 * @param n
	 *            the number to be squared.
	 * @return n*n.
	 */
	private static double square(double n) {
		return n * n;
	}

}
