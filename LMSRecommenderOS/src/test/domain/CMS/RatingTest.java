package test.domain.CMS;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import main.domain.cases.ProfileDescription;
import main.domain.cases.ProfileDescription.Degree;
import main.domain.CMS.LearningObject;
import main.domain.CMS.Rating;
import main.foundation.utils.IDGenerator;

import org.junit.Test;
/**
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 *
 */
public class RatingTest {

	Rating rating = new Rating();

	@Test
	public void testMakeRating() {
		rating = new Rating();
		rating = new Rating(new ProfileDescription(IDGenerator.next(),
				23, Degree.Informatica, "09003", 8, 2, 4), 3.0);
	}

	@Test
	public void testCalcRating() throws NoSuchFieldException, SecurityException {
		double eval = 3.0;
		int avgEval = 4;
		ProfileDescription profile = new ProfileDescription(IDGenerator.next(),
				23, Degree.Informatica, "09003", 8, 2, avgEval);
		rating = new Rating(profile, eval);
		assertTrue(rating.getNormEval() == (2.5 / avgEval * eval));
	}

	@Test
	public void testEvaluateNull() throws NoSuchFieldException,
			SecurityException {
		double eval = 0.0;
		int avgEval = 4;
		ProfileDescription profile = new ProfileDescription(IDGenerator.next(),
				23, Degree.Informatica, "09003", 8, 2, avgEval);
		rating = new Rating(profile, eval);
		assertTrue(rating.getNormEval() == (2.5 / avgEval * eval));
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

	@Test
	public void testToString() {
		Field[] fields = rating.getClass().getDeclaredFields();
		String strFields = rating.toString();
		for (Field field : fields) {
			field.setAccessible(true);
			// Ignore private static fields && private static final fields
			if (field.getModifiers() != 10 && field.getModifiers() != 26) {
				assertTrue(strFields.contains(field.getName()));
			}
		}
	}
}
