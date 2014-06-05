package main.foundation.dataGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import main.domain.cases.DocumentSolution;
import main.domain.cases.ProfileDescription;
import main.domain.cases.RatingResult;
import main.domain.cases.ProfileDescription.Degree;

import jcolibri.util.FileIO;

/**
 * Class that generates the learning object database in a proper format.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 */
public class LearningObjectProfileDataBaseCreater {
	private static HashMap<Integer, ProfileDescription> profiles = new HashMap<Integer, ProfileDescription>();
	private static HashMap<Integer, DocumentSolution> documents = new HashMap<Integer, DocumentSolution>();

	private static void parseProfiles(String filename) throws Exception {
		BufferedReader br = null;
		br = new BufferedReader(
				new InputStreamReader(FileIO.openFile(filename)));
		String line = "";
		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "|");
			ProfileDescription profile = new ProfileDescription();
			profile.setId(Integer.parseInt(st.nextToken()));
			profile.setAge(Integer.parseInt(st.nextToken()));
			profile.setDegree(Degree.valueOf(st.nextToken()));
			profile.setZipCode(st.nextToken());
			profile.setAvgGrade(Integer.parseInt(st.nextToken()));
			profile.setAvgDifficulty(Integer.parseInt(st.nextToken()));
			profile.setAvgEvaluation(Integer.parseInt(st.nextToken()));

			profiles.put(profile.getId(), profile);
		}
		br.close();

	}

	private static void parseDocuments(String filename) throws Exception {
		BufferedReader br = null;
		br = new BufferedReader(
				new InputStreamReader(FileIO.openFile(filename)));

		String line = "";
		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "|");
			DocumentSolution document = new DocumentSolution();
			document.setId(Integer.parseInt(st.nextToken()));
			document.setDocName(st.nextToken());
			document.setDocAuthor(st.nextToken());
			document.setDocDate(st.nextToken());
			document.setDifficulty(Integer.parseInt(st.nextToken()));

			documents.put(document.getId(), document);
		}
		br.close();
	}

	private static String getProfile(ProfileDescription profile, String sep) {
		return profile.getId() + sep + profile.getAge() + sep
				+ profile.getDegree() + sep + profile.getZipCode() + sep
				+ profile.getAvgGrade() + sep + profile.getAvgDifficulty()
				+ sep + profile.getAvgEvaluation();
	}

	private static String getDocument(DocumentSolution document, String sep) {
		return document.getId() + sep + document.getDocName() + sep
				+ document.getDocAuthor() + sep + document.getDocDate() + sep
				+ document.getDifficulty();
	}

	private static void generateNewFile(String ratingsFile, String filename,
			String separator) throws Exception {
		BufferedWriter bw = null;
		bw = new BufferedWriter(new FileWriter(new File(filename), false));
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(
				FileIO.openFile(ratingsFile)));
		int ratingId = 1;
		String line = "";
		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "\t");
			Integer profileId = Integer.parseInt(st.nextToken());
			Integer documentId = Integer.parseInt(st.nextToken());
			Integer rat = Integer.parseInt(st.nextToken());

			ProfileDescription profile = profiles.get(profileId);
			DocumentSolution document = documents.get(documentId);
			RatingResult rating = new RatingResult();
			rating.setId(ratingId++);
			rating.setRating(rat);

			bw.write(getProfile(profile, separator) + separator
					+ getDocument(document, separator) + separator
					+ rating.getId() + separator + rating.getRating());
			bw.newLine();
		}

		br.close();
		bw.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			parseProfiles("main/foundation/data/u.profileDescription");
			parseDocuments("main/foundation/data/u.documentSolution");

			generateNewFile("main/foundation/data/u.ratingResult",
					"src/main/foundation/data/learningObjectCases.txt", "|");
		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(
					LearningObjectProfileDataBaseCreater.class).error(e, e);

		}

		System.out.println("File created: learningObjectCases.txt");

	}

}
