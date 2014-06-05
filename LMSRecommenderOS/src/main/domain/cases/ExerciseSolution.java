package main.domain.cases;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.extensions.classification.ClassificationSolution;

/**
 * Java Bean that stores the solution of the case.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * @version 1.0
 */
public class ExerciseSolution implements CaseComponent, ClassificationSolution {

	Integer id;
	String exeName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExeName() {
		return exeName;
	}

	public void setExeName(String exeName) {
		this.exeName = exeName;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", this.getClass());
	}

	@Override
	public Object getClassification() {
		return this;
	}

	@Override
	public String toString() {
		return "ExerciseSolution [id=" + id + ", exeName=" + exeName + "]";
	}

}
