package test.domain.CMS;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import main.domain.CMS.Course;
import main.domain.CMS.LearningObject;
import main.foundation.utils.IDGenerator;

import org.junit.Test;
/**
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 *
 */
public class CourseTest {

	Course course = new Course(IDGenerator.next(), "Algorithms");

	@Test
	public void testMakeCourse() {
		course = new Course(IDGenerator.next(), "Algorithms");
	}

	@Test
	public void testGetterSetter() {
		Method[] methods = LearningObject.class.getDeclaredMethods();
		StringBuilder sbMethods = new StringBuilder();
		for (Method method : methods) {
			method.setAccessible(true);
			sbMethods.append(method);
		}
		String strMethods = sbMethods.toString().toUpperCase();
		Field[] fields = LearningObject.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			// Ignore private static fields && private static final fields
			if (field.getModifiers() != 10 && field.getModifiers() != 26) {
				assertTrue(strMethods.contains(("get" + field.getName())
						.toUpperCase()));
				assertTrue(strMethods.contains(("set" + field.getName())
						.toUpperCase()));
			}
		}
	}

}
