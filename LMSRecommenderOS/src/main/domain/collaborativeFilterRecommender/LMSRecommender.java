package main.domain.collaborativeFilterRecommender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.recommendation.ContentBasedProfile.ObtainQueryFromProfile;
import jcolibri.extensions.recommendation.casesDisplay.UserChoice;
import jcolibri.extensions.recommendation.conditionals.BuyOrQuit;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.selection.SelectCases;
import main.domain.StandardLMSRecommender;
import main.domain.cases.ProfileDescription;
import main.domain.cases.RatingResult;
import main.foundation.evaluation.LMSEvaluator;
import main.userinterface.LMSDisplayCasesTableMethod;
import main.userinterface.LMSObtainQueryWithFormMethod;

import org.apache.commons.logging.Log;

/**
 * Single-Shot learning objects recommender in an e-learning environment (like
 * Learning Management Systems) obtaining a description from the profile of the
 * user and scoring cases using collaborative recommendation. <br>
 * This recommender uses a collaborative retrieval algorithm. These
 * collaborative algorithms return items depending on the recommendations of
 * other users. They require an special organization of the case base to be
 * executed.<br>
 * Summary:
 * <ul>
 * <li>Type: Single-Shot
 * <li>Case base: learningObjectCases
 * <li>One off Preference Elicitation: Profile
 * <li>Retrieval: Collaborative + topKselection
 * <li>Display: In table to select the most appropriate by the user
 * </ul>
 * 
 * <p>
 * Note: This implementation filters all items already rated by the user of the
 * retrieved query. The idea behind this filtering is to prevent recommendations
 * of items that the user already has seen (done items). This feature is a
 * general improve of the recommendations but is especially useful in the
 * e-learning environment - normally nobody wants to do the same exercises
 * again.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 * 
 */
public class LMSRecommender extends StandardLMSRecommender {

	/**
	 * Number of users to take in the neighborhood for the collaborative
	 * filtering.
	 */
	private static final int NUM_K_USERS = 10;

	/** Number of recommendations for the final selection */
	private static final int NUM_TOP_K = 6;

	/**
	 * Factor that decreases the similarity between users who have fewer than
	 * this number of co-rated items.
	 */
	private static final int NUM_CORR_ITEMS = 20;

	/** Singleton instance */
	private static LMSRecommender instance = null;

	/** Connector to the data source */
	private Connector connector;

	/** Case base of this recommender */
	private CBRCaseBase caseBase;

	/** Logging instance */
	private Log log;

	/** Attributes to hide in the query form */
	private List<Attribute> hiddenAtts;

	/** Labels to show in the query form */
	private Map<Attribute, String> labels;

	/** Number of evaluations */
	private int evalCount;

	/** Number of cycles */
	private int numCycles;

	/** The current maximal identifier */
	private int maxId;

	/**
	 * Singleton getter.
	 * 
	 * @return instance of this class.
	 */
	public static LMSRecommender getInstance() {
		if (instance == null) {
			return new LMSRecommender();
		}
		return instance;
	}

	/**
	 * Constructor
	 */
	private LMSRecommender() {
		super();
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public int getEvalCount() {
		return evalCount;
	}

	public void setEvalCount(int evalCount) {
		this.evalCount = evalCount;
	}

	public int getNumCycles() {
		return numCycles;
	}

	public void setNumCycles(int numCycles) {
		this.numCycles = numCycles;
	}

	public static int getkUsers() {
		return NUM_K_USERS;
	}

	public static int getTopK() {
		return NUM_TOP_K;
	}

	public static int getCorrItems() {
		return NUM_CORR_ITEMS;
	}

	/**
	 * Configures the CBR recommender to start with the cycle.
	 * 
	 * @see jcolibri.cbraplications.StandardCBRApplication#configure()
	 */
	@Override
	public void configure() throws ExecutionException {

		connector = new PlainTextConnector();
		connector.initFromXMLfile(jcolibri.util.FileIO
				.findFile("main/foundation/profileDB/plaintextconfig.xml"));
		// Create a Lineal case base for in-memory organization
		caseBase = new LMSPearsonMatrixCaseBase(new Attribute("rating",
				RatingResult.class), NUM_CORR_ITEMS);

		// Configure Form Filling
		hiddenAtts = new ArrayList<Attribute>();
		hiddenAtts.add(new Attribute("doneCourses", ProfileDescription.class));
		hiddenAtts.add(new Attribute("doneDocs", ProfileDescription.class));
		hiddenAtts.add(new Attribute("doneExes", ProfileDescription.class));
		hiddenAtts.add(new Attribute("age", ProfileDescription.class));
		hiddenAtts.add(new Attribute("degree", ProfileDescription.class));
		hiddenAtts.add(new Attribute("zipCode", ProfileDescription.class));
		hiddenAtts.add(new Attribute("avgGrade", ProfileDescription.class));
		hiddenAtts
				.add(new Attribute("avgDifficulty", ProfileDescription.class));
		hiddenAtts
				.add(new Attribute("avgEvaluation", ProfileDescription.class));

		labels = new HashMap<Attribute, String>();

		log = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	}

	/**
	 * Fills the case base with cases from the connector.
	 * 
	 * @see jcolibri.cbraplications.StandardCBRApplication#preCycle()
	 */
	@Override
	public CBRCaseBase preCycle() throws ExecutionException {

		// Load cases from connector into the case base
		caseBase.init(connector);

		// Print the cases
		java.util.Collection<CBRCase> cases = caseBase.getCases();
		for (CBRCase c : cases) {
			maxId = Math.max((Integer) c.getID(), maxId);
			// DEBUG: Prints all loaded cases from the DB
			// System.out.println(c);
		}
		numCycles = caseBase.getCases().size();

		labels.put(new Attribute("id", ProfileDescription.class),
				"Select the profile by Id (Range: 1 - " + maxId + ")");
		org.apache.commons.logging.LogFactory.getLog(this.getClass()).info(
				cases.size() + " cases loaded");
		return caseBase;
	}

	/**
	 * Creates a table of recommendations by using collaborative filtering.
	 * 
	 */
	@Override
	public void cycle(CBRQuery query) throws ExecutionException {

		/********* Execute Collaborative */
		Collection<RetrievalResult> res = LMSCollaborativeFilterRetrievalMethod
				.getRecommendation((LMSPearsonMatrixCaseBase) caseBase, query,
						NUM_K_USERS);
		log.info("Query: " + query.getDescription());

		Collection<CBRCase> selectedCases = SelectCases.selectTopK(res, NUM_TOP_K);

		/********* Evaluation ************/
		if (LMSEvaluator.getEvaluationReport() != null) {
			if (!LMSEvaluator.isEvalLimit(evalCount, numCycles)) {
				// DEBUG: Prints the evaluated cases
				// System.out.println("TEST: " + eval);
				// System.out.println("EVAL: " +
				// eval.iterator().next().getEval());
				LMSEvaluator.getEvaluationReport().addDataToSeries(
						"Average Similarity", res);
			}
			evalCount++;
		} else {
			UserChoice choice = LMSDisplayCasesTableMethod
					.displayCasesInTableBasic(selectedCases);
			if (BuyOrQuit.buyOrQuit(choice)) {
				System.out.println("Finish - User Selects: "
						+ choice.getSelectedCase());
				selectedCases.clear();
				selectedCases.add(choice.getSelectedCase());
			} else
				System.out.println("Finish - User Quits");
		}

	}

	/**
	 * Closes the cycle of the CBR recommender.
	 * 
	 * @see jcolibri.cbraplications.StandardCBRApplication#postCycle()
	 */
	@Override
	public void postCycle() throws ExecutionException {
		connector.close();
	}

	/**
	 * String representation of this class.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LMSRecommender [connector=" + connector + ", caseBase="
				+ caseBase + ", log=" + log + ", hiddenAtts=" + hiddenAtts
				+ ", labels=" + labels + ", evalCount=" + evalCount
				+ ", numCycles=" + numCycles + ", maxId=" + maxId + "]";
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// SwingProgressBar shows the progress
		jcolibri.util.ProgressController.clear();
		jcolibri.util.ProgressController
				.register(new jcolibri.test.main.SwingProgressBar(),
						LMSMatrixCaseBase.class);

		LMSRecommender recommender = LMSRecommender.getInstance();
		try {
			recommender.configure();

			recommender.preCycle();

			boolean cont = true;
			boolean firstIter = true;
			while (cont) {
				CBRQuery query = new CBRQuery();
				query = ObtainQueryFromProfile
						.obtainQueryFromProfile("main/domain/collaborativeRecommender/profile1.xml");
				if (!firstIter) {
					LMSObtainQueryWithFormMethod.obtainQueryWithInitialValues(
							query, recommender.hiddenAtts, recommender.labels);
				}
				recommender.cycle(query);

				int ans = javax.swing.JOptionPane.showConfirmDialog(null,
						"CBR cycle finished , query again?", "Cycle finished",
						javax.swing.JOptionPane.YES_NO_OPTION);
				cont = (ans == javax.swing.JOptionPane.YES_OPTION);
				firstIter = false;
			}

			recommender.postCycle();

		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(LMSRecommender.class)
					.error(e, e);

		}
	}

}
