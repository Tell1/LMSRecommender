package test.domain.CMS;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import main.domain.cases.ProfileDescription;
import main.domain.cases.ProfileDescription.Degree;
import main.domain.CMS.Document;
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
public class LearningObjectTest {

	LearningObject learnObj = new Document();

	@Test
	public void testMakeLearningObject() {
		learnObj = new Document();
	}

	@Test
	public void testEvaluate() throws NoSuchFieldException, SecurityException {
		double eval = 5.0;
		double oldEval = learnObj.getAvgEval();
		int oldEvalCnt = learnObj.getEvalCnt();
		Rating rating = new Rating(new ProfileDescription(IDGenerator.next(),
				23, Degree.Informatica, "09003", 8, 2, 4), eval);
		learnObj.evaluate(rating);
		assertTrue(learnObj.getAvgEval() == (oldEval * oldEvalCnt + rating
				.getNormEval()) / (oldEvalCnt + 1));
	}

	@Test
	public void testEvaluateZero() throws NoSuchFieldException,
			SecurityException {
		double eval = 0.0;
		double oldEval = learnObj.getAvgEval();
		int oldEvalCnt = learnObj.getEvalCnt();
		learnObj.evaluate(new Rating(new ProfileDescription(IDGenerator.next(),
				23, Degree.Informatica, "09003", 8, 2, 4), eval));
		assertTrue(learnObj.getAvgEval() == oldEval);
		assertTrue(learnObj.getEvalCnt() == oldEvalCnt + 1);
	}

	@Test
	public void testEvaluateNull() throws NoSuchFieldException,
			SecurityException {
		try {
			learnObj.evaluate(null); // Should never happen - to test!
		} catch (IllegalArgumentException e) {
			System.err.println("Success: no evaluation done - good job!");
			e.printStackTrace();
			return;
		}
		fail("Error: second singleton object created.");
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
