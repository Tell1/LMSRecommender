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
public class DocumentSolution implements CaseComponent, ClassificationSolution {

	Integer id;
	String docName;
	String docAuthor;
	String docDate;
	Integer difficulty;

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", this.getClass());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocAuthor() {
		return docAuthor;
	}

	public void setDocAuthor(String docAuthor) {
		this.docAuthor = docAuthor;
	}

	public Integer getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public Object getClassification() {
		return this;
	}

	@Override
	public String toString() {
		return "DocumentSolution [id=" + id + ", docName=" + docName
				+ ", docAuthor=" + docAuthor + ", docDate=" + docDate
				+ ", difficulty=" + difficulty + "]";
	}

}
