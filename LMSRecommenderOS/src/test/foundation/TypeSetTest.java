package test.foundation;

import static org.junit.Assert.assertTrue;
import main.domain.CMS.Course;
import main.foundation.dataTypes.CourseSet;

import org.junit.Test;
/**
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 *
 */
public class TypeSetTest {

	@Test
	public void testCourseSet() throws Exception {
		CourseSet ts = new CourseSet();
		String[] strArr = { "Algoritmia", "Inteligencia Artificial" };
		ts.fromString("[Course [name=Algoritmia], Course [name=Inteligencia Artificial]]");
		for (int i = 0; i < strArr.length; i++) {
			System.out.println(ts.getSet() + ": " + new Course(i+1, strArr[i]));
			assertTrue(ts.getSet().contains(new Course(i+1, strArr[i])));
		}
	}

}
