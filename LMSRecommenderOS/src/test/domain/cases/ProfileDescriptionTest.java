package test.domain.cases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import jcolibri.cbrcore.Attribute;
import main.domain.CMS.LearningObject;
import main.domain.cases.ProfileDescription;
import main.domain.cases.ProfileDescription.Degree;
import main.foundation.utils.IDGenerator;

import org.junit.Test;
/**
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 *
 */
public class ProfileDescriptionTest {

	ProfileDescription student = new ProfileDescription(IDGenerator.next(),
			23, Degree.Informatica, "09003", 8, 2, 4);

	@Test
	public void testMakeProfileDescription() {
		student = new ProfileDescription(IDGenerator.next(),
				23, Degree.Informatica, "09003", 8, 2, 4);
	}

	@Test
	public void testgetIdAttribute() throws NoSuchFieldException,
			SecurityException {
		Field field = student.getClass().getDeclaredField("id");
		field.setAccessible(true);
		assertEquals(student.getIdAttribute(), new Attribute(field.getName(),
				field.getDeclaringClass()));
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
		Field[] fields = student.getClass().getDeclaredFields();
		String strFields = student.toString();
		for (Field field : fields) {
			field.setAccessible(true);
			// Ignore private static fields && private static final fields
			if (field.getModifiers() != 10 && field.getModifiers() != 26) {
				assertTrue(strFields.contains(field.getName()));
			}
		}
	}

}
