package main.domain.CMS;

import main.domain.cases.ProfileDescription;

/**
 * Class that represents a rating in an e-learning environment.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * @version 1.0
 */
public class Rating {

	private static final double MAX_RATING = 5.0;
	private static final double AVG_RATING = 2.5;

	private double normEval;
	private double userEvalTendency;
	private ProfileDescription profile;

	public Rating() {
	}

	public Rating(ProfileDescription profile, double eval) {
		this.profile = profile;
		this.userEvalTendency = profile.getAvgEvaluation();
		this.normEval = (AVG_RATING / userEvalTendency) * eval;
	}

	public double getNormEval() {
		return normEval;
	}

	public void setNormEval(double normEval) {
		this.normEval = normEval;
	}

	public double getUserEvalTendency() {
		return userEvalTendency;
	}

	public void setUserEvalTendency(double userEvalTendency) {
		this.userEvalTendency = userEvalTendency;
	}

	public ProfileDescription getProfile() {
		return profile;
	}

	public void setProfile(ProfileDescription profile) {
		this.profile = profile;
	}

	public static double getMaxRating() {
		return MAX_RATING;
	}

	@Override
	public String toString() {
		return "Rating [normEval=" + normEval + ", userEvalTendency="
				+ userEvalTendency + ", profile=" + profile + "]";
	}

}
