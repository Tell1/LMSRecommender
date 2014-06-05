package test.domain;

import java.lang.reflect.Field;

import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.recommendation.ContentBasedProfile.ObtainQueryFromProfile;
import junit.framework.TestCase;
import main.domain.StandardLMSRecommender;
import main.foundation.docDB.HSQLDBserver;

import org.junit.Test;
/**
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 *
 */
public class LMSRecommenderTest extends TestCase {

	StandardLMSRecommender lmsRecom = StandardLMSRecommender.getInstance();

	@Test
	public void testAllLMSRecommender() {
		StandardLMSRecommender[] recArr = {
				main.domain.filterBasedRecommender.LMSRecommender.getInstance(),
				main.domain.contentBasedRecommender.LMSRecommender
						.getInstance(),
				main.domain.collaborativeRecommender.LMSRecommender
						.getInstance() };
		for (StandardLMSRecommender standardLMSRecommender : recArr) {
			testLMSRecommender(standardLMSRecommender);
		}
	}

	private void testLMSRecommender(
			StandardLMSRecommender standardLMSRecommender) {
		testMakeLMSRecommenderTestItem(standardLMSRecommender);
		testConfigure(standardLMSRecommender);
		testPreCycle(standardLMSRecommender);
		testCycle(standardLMSRecommender);
		testPostCycle(standardLMSRecommender);
		testToString(standardLMSRecommender);
	}

	@Test
	public void testMakeLMSRecommenderTestItem(
			StandardLMSRecommender standardLMSRecommender) {
		lmsRecom = standardLMSRecommender;
		try {
			lmsRecom.getClass().newInstance();
		} catch (Exception e) {
			System.err
					.println("Success: No second singleton object created - good job!");
			e.printStackTrace();
			return;
		}
		fail("Error: second singleton object created.");
	}

	@Test
	public void testConfigure(StandardLMSRecommender standardLMSRecommender) {
		try {
			lmsRecom = standardLMSRecommender;
			lmsRecom.configure();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		Field[] fields = lmsRecom.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				// Ignore private static fields
				if (field.getModifiers() != 10 && field.getModifiers() != 26) {
					System.out.println(field + "," + field.get(lmsRecom));
					assertNotNull(field.get(lmsRecom));
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testPreCycle(StandardLMSRecommender standardLMSRecommender) {
		try {
			lmsRecom = standardLMSRecommender;
			lmsRecom.configure();
			Object testObj = lmsRecom.getClass().getDeclaredField("caseBase");
			lmsRecom.preCycle();
			Object otherTestObj;
			otherTestObj = lmsRecom.getClass().getDeclaredField("caseBase");
			assertNotSame(testObj, otherTestObj);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCycle(StandardLMSRecommender standardLMSRecommender) {
		try {
			lmsRecom = standardLMSRecommender;
			lmsRecom.configure();
			lmsRecom.preCycle();
			Field field = null;
			CBRQuery qf = new CBRQuery();
			qf = ObtainQueryFromProfile
					.obtainQueryFromProfile("src/main/domain/collaborativeRecommender/profile1.xml");
			System.out.println(qf);
			lmsRecom.cycle(qf);
			field = lmsRecom.getClass().getDeclaredField("connector");
			field.setAccessible(true);
			Connector con = (Connector) field.get(lmsRecom);
			assertTrue(!con.retrieveAllCases().isEmpty());
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPostCycle(StandardLMSRecommender standardLMSRecommender) {
		try {
			lmsRecom = standardLMSRecommender;
			lmsRecom.configure();
			lmsRecom.preCycle();
			lmsRecom.postCycle();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		try {
			// Check if connector is closed
			Field field = lmsRecom.getClass().getDeclaredField("connector");
			field.setAccessible(true);
			// Connector con = (Connector) field.get(lmsRecom);
			// System.out.println("CASES: " + con.retrieveAllCases());
			// assertTrue(con.retrieveAllCases().isEmpty());
			// Check shutdown of HSQLDBServer
			field = HSQLDBserver.class.getDeclaredField("initialized");
			field.setAccessible(true);
			assertFalse(field.getBoolean(lmsRecom));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testToString(StandardLMSRecommender standardLMSRecommender) {
		lmsRecom = standardLMSRecommender;
		Field[] fields = lmsRecom.getClass().getDeclaredFields();
		String strFields = lmsRecom.toString();
		for (Field field : fields) {
			field.setAccessible(true);
			// Ignore private static fields && private static final fields
			if (field.getModifiers() != 10 && field.getModifiers() != 26) {
				assertTrue(strFields.contains(field.getName()));
			}
		}
	}

}
