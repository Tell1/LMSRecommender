package main.foundation.dataTypes;

import java.util.HashSet;
import java.util.Set;

import main.domain.CMS.Exercise;

/**
 * Class that represents a set of exercises and extends the TypeSet class to be
 * able to used in the connecters of jcolibri.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 * 
 */
public class ExeSet extends TypeSet<Exercise> {

	public ExeSet() {
		this.set = new HashSet<Exercise>();
	}

	public ExeSet(Set<Exercise> doneExes) {
		set = doneExes;
	}

	@Override
	public void fromString(String data) throws Exception {

		StringBuilder sb = new StringBuilder();
		char thisChar = ' ';
		Integer id = 1;
		int j = 0;
		for (int c = j; c < data.length(); c++) {
			thisChar = data.charAt(c);
			if (thisChar == '=') {
				for (j = c + 1; thisChar != ']'; j++) {
					thisChar = data.charAt(j);
					if (thisChar != '[' && thisChar != ']') {
						sb.append(thisChar);
					}
				}
				set.add(new Exercise(id++, sb.toString()));
				sb = new StringBuilder();
			}
		}
	}

}
