package main.domain.CMS;

import jcolibri.cbrcore.Attribute;
import main.domain.cases.DocumentDescription;

/**
 * Java Bean that represents a document in an e-learning environment.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * @version 1.0
 */
public class Document extends LearningObject {

	DocumentDescription profile;

	public Document() {
	}

	public Document(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public DocumentDescription getProfile() {
		return profile;
	}

	public void setProfile(DocumentDescription profile) {
		this.profile = profile;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", this.getClass());
	}

	@Override
	public String toString() {
		return "Document [name=" + name + "]";
	}

}
