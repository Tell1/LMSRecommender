package main.domain.cases;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/**
 * Java Bean that stores the result of the case.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * @version 1.0
 */
public class RatingResult implements CaseComponent {

	Integer id;
	Integer rating;

	public RatingResult() {
	}
	

	public RatingResult(Integer id, Integer rating) {
		this.id = id;
		this.rating = rating;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "Rating [id=" + id + ", rating=" + rating + "]";
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", RatingResult.class);
	}

}
