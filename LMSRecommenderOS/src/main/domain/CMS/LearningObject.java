package main.domain.CMS;

import jcolibri.cbrcore.CaseComponent;

/**
 * Abstract class that represents a learning object in an e-learning
 * environment.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * @version 1.0
 */
public abstract class LearningObject implements CaseComponent {

	Integer id;
	String name;
	double difficulty;
	double avgEval;
	int evalCnt;
	String keywords;

	public LearningObject() {

	}

	public LearningObject(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public LearningObject(Integer id, String name, double difficulty,
			double avgEval, int evalCnt, String keywords) {
		super();
		this.id = id;
		this.name = name;
		this.difficulty = difficulty;
		this.avgEval = avgEval;
		this.evalCnt = evalCnt;
		this.keywords = keywords;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(double difficulty) {
		this.difficulty = difficulty;
	}

	public double getAvgEval() {
		return avgEval;
	}

	public void setAvgEval(double avgEval) {
		this.avgEval = avgEval;
	}

	public int getEvalCnt() {
		return evalCnt;
	}

	public void setEvalCnt(int evalCnt) {
		this.evalCnt = evalCnt;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public void evaluate(Rating rating) throws IllegalArgumentException {
		if (rating == null) {
			throw new IllegalArgumentException(
					"Illegal Rating object passed to LearningObject.eval()");
		}
		if (rating.getNormEval() != 0.0) {
			avgEval = (avgEval * evalCnt + rating.getNormEval())
					/ (evalCnt + 1);
		}
		evalCnt++;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LearningObject other = (LearningObject) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LearningObject [id=" + id + ", name=" + name + ", difficulty="
				+ difficulty + ", avgEval=" + avgEval + ", evalCnt=" + evalCnt
				+ ", keywords=" + keywords + "]";
	}

}
