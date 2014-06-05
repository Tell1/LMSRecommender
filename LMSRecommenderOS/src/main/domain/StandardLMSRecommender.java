package main.domain;

import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.Connector;
import jcolibri.method.retrieve.NNretrieval.NNConfig;

/**
 * This abstract class provides the base structure of a CBR application for
 * Learning Management Systems.
 * <p>
 * Note: it´s necessary to implement the static method getInstance if you
 * pretend to evaluate the inheriting recommender. Furthermore there should be
 * filled the LMSEvaluationReport to collect the necessary data for the
 * evaluation.
 * 
 * Defines the method of an standard CBR application. It is composed by:
 * <ul>
 * <li>A configuration method to set up the application.
 * <li>A preCycle that loads cases and prepares the application to run.
 * <li>The cycle method that runs a CBR step using the given query.
 * <li>A postCycle in charge of finishing the application.
 * </ul>
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 */
public abstract class StandardLMSRecommender implements StandardCBRApplication {

	/** */
	private static StandardLMSRecommender instance = null;
	/** */
	protected Connector connector;
	/** */
	protected CBRCaseBase caseBase;
	/** */
	protected NNConfig simConfig;

	public static StandardLMSRecommender getInstance() {
		return instance;
	}

	protected StandardLMSRecommender() {
	}

	public Connector getConnector() {
		return connector;
	}

	public void setConnector(Connector connector) {
		this.connector = connector;
	}

	public CBRCaseBase getCaseBase() {
		return caseBase;
	}

	public void setCaseBase(CBRCaseBase caseBase) {
		this.caseBase = caseBase;
	}

	public NNConfig getSimConfig() {
		return simConfig;
	}

	public void setSimConfig(NNConfig simConfig) {
		this.simConfig = simConfig;
	}

}
