package main.domain.cases;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;
import main.foundation.dataTypes.CourseSet;
import main.foundation.dataTypes.DocSet;
import main.foundation.dataTypes.ExeSet;

/**
 * Java Bean that stores the description of the case.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * @version 1.0
 */
public class ProfileDescription implements CaseComponent {

	public enum Degree {
		Informatica, Comunicacion_Audiovisual, Derecho, Quimica, Turismo, Pedagogia, Organizacion_Industrial, Arquitequtura_Tecnica, Mecanica, Electronica_Industrial, Obras_Publicas, Fisica, Enfermeria, Finanzas_y_Contabilidad, Tecnologias_de_Caminos, Otro, Nada /* ... */
	};

	public enum Rating {
		ZERO, ONE, TWO, THREE, FOUR, FIVE
	};

	public enum ModeOfStudy {
		full_time, part_time
	};

	public enum StyleOfStudy {
		Visual, Practice, Interaction
	};

	public enum LevelOfStudy {
		undergraduate, graduate, doctorate, PGCE, other
	};

	Integer id;
	Integer age;
	Degree degree;
	String zipCode;
	Integer avgGrade;
	Integer avgDifficulty;
	Integer avgEvaluation;
	CourseSet doneCourses;
	DocSet doneDocs;
	ExeSet doneExes;

	// ModeOfStudy modeOfStudy;
	// StyleOfStudy styleOfStudy;
	// LevelOfStudy levelOfStudy;

	public ProfileDescription() {
	}

	public ProfileDescription(String userName, String zipCode,
			Integer avgGrade, Integer avgDifficulty, Integer avgEvaluation) {
		super();
		this.zipCode = zipCode;
		this.avgGrade = avgGrade;
		this.avgDifficulty = avgDifficulty;
		this.avgEvaluation = avgEvaluation;
	}

	public ProfileDescription(Integer id, Integer age, Degree degree,
			String zipCode, Integer avgGrade, Integer avgDifficulty,
			Integer avgEvaluation) {
		super();
		this.id = id;
		this.age = age;
		this.degree = degree;
		this.zipCode = zipCode;
		this.avgGrade = avgGrade;
		this.avgDifficulty = avgDifficulty;
		this.avgEvaluation = avgEvaluation;
	}

	public ProfileDescription(Integer id, Integer age, Degree degree,
			String zipCode, Integer avgGrade, Integer avgDifficulty,
			Integer avgEvaluation, CourseSet doneCourses) {
		super();
		this.id = id;
		this.age = age;
		this.degree = degree;
		this.zipCode = zipCode;
		this.avgGrade = avgGrade;
		this.avgDifficulty = avgDifficulty;
		this.avgEvaluation = avgEvaluation;
		this.doneCourses = doneCourses;
	}

	public ProfileDescription(Integer id, Integer age, Degree degree,
			String zipCode, Integer avgGrade, Integer avgDifficulty,
			Integer avgEvaluation, CourseSet doneCourses, DocSet doneDocs) {
		super();
		this.id = id;
		this.age = age;
		this.degree = degree;
		this.zipCode = zipCode;
		this.avgGrade = avgGrade;
		this.avgDifficulty = avgDifficulty;
		this.avgEvaluation = avgEvaluation;
		this.doneCourses = doneCourses;
		this.doneDocs = doneDocs;
	}

	public ProfileDescription(Integer id, Integer age, Degree degree,
			String zipCode, Integer avgGrade, Integer avgDifficulty,
			Integer avgEvaluation, CourseSet doneCourses, DocSet doneDocs,
			ExeSet doneExes) {
		super();
		this.id = id;
		this.age = age;
		this.degree = degree;
		this.zipCode = zipCode;
		this.avgGrade = avgGrade;
		this.avgDifficulty = avgDifficulty;
		this.avgEvaluation = avgEvaluation;
		this.doneCourses = doneCourses;
		this.doneDocs = doneDocs;
		this.doneExes = doneExes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Integer getAvgGrade() {
		return avgGrade;
	}

	public void setAvgGrade(Integer avgGrade) {
		this.avgGrade = avgGrade;
	}

	public Integer getAvgDifficulty() {
		return avgDifficulty;
	}

	public void setAvgDifficulty(Integer avgDifficulty) {
		this.avgDifficulty = avgDifficulty;
	}

	public Integer getAvgEvaluation() {
		return avgEvaluation;
	}

	public void setAvgEvaluation(Integer avgEvaluation) {
		this.avgEvaluation = avgEvaluation;
	}

	public CourseSet getDoneCourses() {
		return doneCourses;
	}

	public void setDoneCourses(CourseSet doneCourses) {
		this.doneCourses = doneCourses;
	}

	public DocSet getDoneDocs() {
		return doneDocs;
	}

	public void setDoneDocs(DocSet doneDocs) {
		this.doneDocs = doneDocs;
	}

	public ExeSet getDoneExes() {
		return doneExes;
	}

	public void setDoneExes(ExeSet doneExes) {
		this.doneExes = doneExes;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", this.getClass());
	}

	@Override
	public String toString() {
		return "ProfileDescription [id=" + id + ", age=" + age + ", degree="
				+ degree + ", zipCode=" + zipCode + ", avgGrade=" + avgGrade
				+ ", avgDifficulty=" + avgDifficulty + ", avgEvaluation="
				+ avgEvaluation + ", doneCourses=" + doneCourses
				+ ", doneDocs=" + doneDocs + ", doneExes=" + doneExes + "]";
	}

}
