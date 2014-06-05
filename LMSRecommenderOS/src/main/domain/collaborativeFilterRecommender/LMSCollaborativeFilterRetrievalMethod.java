package main.domain.collaborativeFilterRecommender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.recommendation.collaborative.CollaborativeRetrievalMethod;
import jcolibri.extensions.recommendation.collaborative.MatrixCaseBase.RatingTuple;
import jcolibri.extensions.recommendation.collaborative.MatrixCaseBase.SimilarTuple;
import jcolibri.method.retrieve.RetrievalResult;

/**
 * This method returns cases depending on the recommendations of other users. <br>
 * It uses a PearsonMatrix Case base to compute the similarity among neighbors.
 * Then, cases are scored according to a rating that is estimated using the
 * following formula:<br>
 * <img src="doc-files/collaborativerating.jpg"/>
 * <p>
 * Note: This implementation filters all items already rated by the user of the
 * query. The idea behind this filtering is to prevent recommendations of items
 * that the user already has seen (done items). This feature is a general
 * improve of the recommendations but is especially useful in the e-learning
 * environment - normally nobody wants to do the same exercises again.
 * Furthermore some changes were necessary to make this case base evaluable with
 * the LMSEvaluator.
 * <p>
 * See:
 * J. Kelleher and D. Bridge. An accurate and scalable collaborative
 * recommender. Articial Intelligence Review, 21(3-4):193-213, 2004.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.1
 * 
 */
public class LMSCollaborativeFilterRetrievalMethod extends
		CollaborativeRetrievalMethod {

	public static final int NORMALIZE_FACTOR = 2;

	/**
	 * Returns a list of cases scored following the collaborative recommendation
	 * formulae and filtered all items already rated by the user of the query.
	 * 
	 * @param cb
	 *            is the case base that contains the cases
	 * @param query
	 *            is the query to get the recommendation for.
	 * @param kUsers
	 *            defines the number of users taken into account to score the
	 *            cases.
	 */
	@SuppressWarnings("unchecked")
	public static Collection<RetrievalResult> getRecommendation(
			LMSPearsonMatrixCaseBase cb, CBRQuery query, int kUsers) {
		ArrayList<RetrievalResult> result = new ArrayList<RetrievalResult>();

		int id = (Integer) query.getID();
		Collection<SimilarTuple> simil = cb.getSimilar(id);
		if (simil == null) {
			org.apache.commons.logging.LogFactory.getLog(
					LMSCollaborativeFilterRetrievalMethod.class).error(
					"Id " + id + " does not exists");
			return result;
		}

		ArrayList<SimilarTuple> select = new ArrayList<SimilarTuple>();
		int i = 0;
		for (Iterator<SimilarTuple> iter = simil.iterator(); (iter.hasNext() && i < kUsers); i++)
			select.add(iter.next());

		// ///// DEBUG
		// System.out.println("\nQuery: " + cb.getDescription(id));
		// System.out.println(cb.getRatingTuples(id).size() + " Ratings: "
		// + cb.getRatingTuples(id));
		// System.out.println("\nSimilar ratings:");
		// for (MatrixCaseBase.SimilarTuple st : select) {
		// System.out.print((st.getSimilarity() / NORMALIZE_FACTOR) + " <--- ");
		// System.out.println(cb.getDescription(st.getSimilarId()));
		// System.out.println(cb.getRatingTuples(st.getSimilarId()).size()
		// + " Ratings: " + cb.getRatingTuples(st.getSimilarId()));
		// }
		// ///////////

		for (Integer solId : cb.getSolutions()) {
			double mean = cb.getAverage(id);
			double acum = 0;
			double simacum = 0;
			for (SimilarTuple st : select) {
				int other = st.getSimilarId();
				double rating = findRating(cb, other, solId);
				double otherMean = cb.getAverage(other);
				acum += ((rating - otherMean) * st.getSimilarity());
				simacum += st.getSimilarity();
			}
			double res = ((mean + (acum / simacum)));

			CBRCase c = new CBRCase();
			c.setDescription(cb.getDescription(id));
			c.setSolution(cb.getSolution(solId));

			if (!isDoneDocument(cb, query, solId)) {
				result.add(new RetrievalResult(c, res));
			}
		}

		java.util.Collections.sort(result);
		return result;
	}

	private static boolean isDoneDocument(LMSPearsonMatrixCaseBase cb,
			CBRQuery query, Integer solId) {
		return findRating(cb, (Integer) query.getID(), solId) != -1;
	}

	private static double findRating(LMSPearsonMatrixCaseBase cb, int descId,
			int solId) {
		for (RatingTuple rt : cb.getRatingTuples(descId)) {
			if (rt.getSolutionId() == solId)
				return rt.getRating();
		}
		return -1;
	}

}
