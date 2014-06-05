package main.domain.contentBasedRecommender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jcolibri.casebase.CachedLinealCaseBase;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.connector.xmlutils.QuerySerializer;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.recommendation.casesDisplay.UserChoice;
import jcolibri.extensions.recommendation.conditionals.BuyOrQuit;
import jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.EqualsStringIgnoreCase;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.method.reuse.CombineQueryAndCasesMethod;
import main.domain.StandardLMSRecommender;
import main.domain.cases.DocumentDescription;
import main.domain.cases.DocumentSolution;
import main.foundation.docDB.HSQLDBserver;
import main.foundation.evaluation.LMSEvaluator;
import main.userinterface.LMSDisplayCasesTableMethod;

import org.apache.commons.logging.Log;

/**
 * Single-Shot learning objects recommender in an e-learning environment (like
 * Learning Management Systems) using form-filling, Nearest Neighbour retrieval
 * and top k selection. <br>
 * This recommender allows the user to define his/her requirements using a form
 * after the first automatic query from a XML-file. Then, it retrieves the k
 * most similar cases using Nearest Neighbour retrieval. These k cases are
 * displayed to the user in a table to select the most appropriate by the user
 * and the system asks for more queries or finishes. <br>
 * Summary:
 * <ul>
 * <li>Type: Single-Shot
 * <li>Case base: LMS_DOC
 * <li>One off Preference Elicitation: Form Filling with initial values after
 * the first query
 * <li>Retrieval: NN + topKselection
 * <li>Display: In table to select the most appropriate by the user
 * </ul>
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.1
 * 
 */
public class LMSRecommender extends StandardLMSRecommender {

	/** Number of recommendations for the final selection */
	private static final int TOP_K = 6;

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

	public Map<Attribute, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<Attribute, String> labels) {
		this.labels = labels;
	}

	public List<Attribute> getHiddenAtts() {
		return hiddenAtts;
	}

	public void setHiddenAtts(List<Attribute> hiddenAtts) {
		this.hiddenAtts = hiddenAtts;
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

	public int getMaxId() {
		return maxId;
	}

	public void setMaxId(int maxId) {
		this.maxId = maxId;
	}

	public static int getTopK() {
		return TOP_K;
	}

	/**
	 * Configures the CBR recommender to start with the cycle.
	 * 
	 * @see jcolibri.cbraplications.StandardCBRApplication#configure()
	 */
	@Override
	public void configure() throws ExecutionException {
		HSQLDBserver.init();
		connector = new DataBaseConnector();
		// Init the ddbb connector with the config file
		connector.initFromXMLfile(jcolibri.util.FileIO
				.findFile("main/foundation/docDB/databaseconfig.xml"));
		// Create a Lineal case base for in???memory organization
		caseBase = new CachedLinealCaseBase();

		// Obtain configuration for kNN
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		simConfig
				.addMapping(new Attribute("course", DocumentDescription.class),
						new Equal());
		simConfig.addMapping(
				new Attribute("subject", DocumentDescription.class),
				new Equal());
		simConfig.addMapping(new Attribute("difficulty",
				DocumentDescription.class), new Interval(5));
		simConfig.addMapping(new Attribute("keywords",
				DocumentDescription.class), new EqualsStringIgnoreCase());

		// Configure Form Filling
		hiddenAtts = new ArrayList<Attribute>();
		hiddenAtts.add(new Attribute("id", DocumentDescription.class));
		hiddenAtts.add(new Attribute("doneCourses", DocumentDescription.class));
		hiddenAtts.add(new Attribute("doneDocs", DocumentDescription.class));
		hiddenAtts.add(new Attribute("doneExes", DocumentDescription.class));
		labels = new HashMap<Attribute, String>();
		labels.put(new Attribute("course", DocumentDescription.class),
				"Select the courses");
		labels.put(new Attribute("subject", DocumentDescription.class),
				"Select the subject");
		labels.put(new Attribute("difficulty", DocumentDescription.class),
				"Min difficulty (0-5)");
		labels.put(new Attribute("keywords", DocumentDescription.class),
				"Select some keywords");

		log = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	}

	/**
	 * Fills the case base with cases from the connector.
	 * 
	 * @see jcolibri.cbraplications.StandardCBRApplication#preCycle()
	 */
	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		java.util.Collection<CBRCase> cases = caseBase.getCases();
		for (CBRCase c : cases) {
			// DEBUG: Prints all loaded cases from the DB
			// System.out.println(c);
			maxId = Math.max((Integer) c.getID(), maxId);
		}
		numCycles = caseBase.getCases().size();
		return caseBase;
	}

	/**
	 * Creates a table of recommendations by using content-based filtering.
	 * 
	 * @see jcolibri.cbraplications.StandardCBRApplication#cycle(jcolibri.cbrcore.CBRQuery)
	 */
	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		log.info("Query: " + query.getDescription());

		/********* Execute NN ************/
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(
				caseBase.getCases(), query, simConfig);

		/********* Select cases **********/
		Collection<CBRCase> selectedCases = SelectCases.selectTopK(eval, TOP_K);

		/********* Evaluation ************/
		if (LMSEvaluator.getEvaluationReport() != null) {
			if (!LMSEvaluator.isEvalLimit(evalCount, numCycles)) {
				// DEBUG: Prints the evaluated cases
				// System.out.println("TEST: " + eval);
				// System.out.println("EVAL: " +
				// eval.iterator().next().getEval());
				LMSEvaluator.getEvaluationReport().addDataToSeries(
						"Average Similarity", eval);
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

		if (!eval.isEmpty()
				&& eval.iterator().next().get_case().getSolution() != null) {

			/********* Reuse **********/
			// DEBUG: Prints the retrieval
			// System.out.println("Retrieved cases (classified by similarity):");
			// for (RetrievalResult nse : eval)
			// System.out.println(nse);

			System.out.println("Query:");
			System.out.println(query);

			if (selectedCases != null && selectedCases.size() != 0
					&& selectedCases.iterator().next() != null) {

				Collection<CBRCase> newcases = CombineQueryAndCasesMethod
						.combine(query, selectedCases);
				// DEBUG: Prints the combined cases
				// System.out.println("Combined cases");
				// for (jcolibri.cbrcore.CBRCase c : newcases)
				// System.out.println(c);

				/********* Revise **********/
				CBRCase bestCase = newcases.iterator().next();
				maxId++;
				HashMap<Attribute, Object> componentsKeys = new HashMap<Attribute, Object>();
				componentsKeys.put(new Attribute("id",
						DocumentDescription.class), maxId);
				componentsKeys.put(new Attribute("id", DocumentSolution.class),
						maxId);
				jcolibri.method.revise.DefineNewIdsMethod.defineNewIdsMethod(
						bestCase, componentsKeys);

				System.out.println("Case with new Id");
				System.out.println(bestCase);

				/********* Retain **********/
				// Comment next line to do not store cases into persistence
				jcolibri.method.retain.StoreCasesMethod.storeCase(caseBase,
						bestCase);
			}
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
		HSQLDBserver.shutDown();
	}

	/**
	 * String representation of this class.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LMSRecommender [connector=" + connector + ", caseBase="
				+ caseBase + ", log=" + log + ", labels=" + labels
				+ ", hiddenAtts=" + hiddenAtts + ", evalCount=" + evalCount
				+ ", numCycles=" + numCycles + ", maxId=" + maxId + "]";
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		LMSRecommender recommender = getInstance();
		try {
			recommender.configure();

			recommender.preCycle();

			// Main CBR cycle
			boolean cont = true;
			while (cont) {
				CBRQuery query = new CBRQuery();
				query = QuerySerializer
						.deserializeQuery("main/domain/contentBasedRecommender/document.xml");
				ObtainQueryWithFormMethod.obtainQueryWithInitialValues(query,
						recommender.hiddenAtts, recommender.labels);
				recommender.cycle(query);

				int ans = javax.swing.JOptionPane.showConfirmDialog(null,
						"CBR cycle finished , query again?", "Cycle finished",
						javax.swing.JOptionPane.YES_NO_OPTION);
				cont = (ans == javax.swing.JOptionPane.YES_OPTION);
			}

			recommender.postCycle();

		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(LMSRecommender.class)
					.error(e, e);
			javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

}
