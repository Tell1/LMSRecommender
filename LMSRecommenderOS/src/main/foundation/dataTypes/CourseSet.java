package main.foundation.dataTypes;

import java.util.HashSet;
import java.util.Set;

import main.domain.CMS.Course;

/**
 * Class that represents a set of courses and extends the TypeSet class to be
 * able to used in the connecters of jcolibri.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 * 
 */
public class CourseSet extends TypeSet<Course> {

	public CourseSet() {
		this.set = new HashSet<Course>();
	}

	public CourseSet(Set<Course> doneCourses) {
		set = doneCourses;
	}

	// [Course [name=Algoritmia], Course [name=Inteligencia Artificial]]
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
				set.add(new Course(id++, sb.toString()));
				sb = new StringBuilder();
			}
		}
	}
}
