package main.foundation.dataGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import main.domain.cases.DocumentDescription;
import main.domain.cases.DocumentSolution;
import main.domain.cases.RatingResult;
import main.domain.cases.DocumentDescription.Courses;
import main.domain.cases.DocumentDescription.Subjects;

import jcolibri.util.FileIO;

/**
 * Class that generates the document cases database in a proper format.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 */
public class LearningObjectDocDataBaseCreater {
	private static HashMap<Integer, DocumentDescription> docDescriptions = new HashMap<Integer, DocumentDescription>();
	private static HashMap<Integer, DocumentSolution> docSolutions = new HashMap<Integer, DocumentSolution>();

	private static void parseDescriptions(String filename) throws Exception {
		BufferedReader br = null;
		br = new BufferedReader(
				new InputStreamReader(FileIO.openFile(filename)));
		String line = "";
		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "|");
			DocumentDescription docDesc = new DocumentDescription();
			docDesc.setId(Integer.parseInt(st.nextToken()));
			docDesc.setCourse(Courses.valueOf(st.nextToken()));
			docDesc.setSubject(Subjects.valueOf(st.nextToken()));
			docDesc.setDifficulty(Integer.parseInt(st.nextToken()));
			docDesc.setKeywords(st.nextToken());

			docDescriptions.put(docDesc.getId(), docDesc);
		}
		br.close();

	}

	private static void parseSolutions(String filename) throws Exception {
		BufferedReader br = null;
		br = new BufferedReader(
				new InputStreamReader(FileIO.openFile(filename)));

		String line = "";
		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "|");
			DocumentSolution docSol = new DocumentSolution();
			docSol.setId(Integer.parseInt(st.nextToken()));
			docSol.setDocName(st.nextToken());
			docSol.setDocAuthor(st.nextToken());
			docSol.setDocDate(st.nextToken());
			docSol.setDifficulty(Integer.parseInt(st.nextToken()));

			docSolutions.put(docSol.getId(), docSol);
		}
		br.close();
	}

	private static String getDescription(DocumentDescription docDesc,
			String sep, String quote) {
		return docDesc.getId() + sep + quote + docDesc.getCourse() + quote
				+ sep + quote + docDesc.getSubject() + quote + sep
				+ docDesc.getDifficulty() + sep + quote + docDesc.getKeywords()
				+ quote;
	}

	private static String getSolution(DocumentSolution docSol, String sep,
			String quote) {
		return docSol.getId() + sep + quote + docSol.getDocName() + quote + sep
				+ quote + docSol.getDocAuthor() + quote + sep + quote
				+ docSol.getDocDate() + quote + sep + docSol.getDifficulty();
	}

	private static void generateNewFile(String ratingsFile, String filename,
			String separator, String quote) throws Exception {
		BufferedWriter bw = null;
		bw = new BufferedWriter(new FileWriter(new File(filename), false));
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(
				FileIO.openFile(ratingsFile)));
		int ratingId = 1;
		String line = "";
		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "\t");
			Integer docId = Integer.parseInt(st.nextToken());
			Integer documentId = Integer.parseInt(st.nextToken());
			Integer rat = Integer.parseInt(st.nextToken());

			DocumentDescription docDesc = docDescriptions.get(docId);
			DocumentSolution docSol = docSolutions.get(documentId);
			RatingResult rating = new RatingResult();
			rating.setId(ratingId++);
			rating.setRating(rat);

			bw.write(getDescription(docDesc, separator, quote) + separator
					+ getSolution(docSol, separator, quote) + separator
					+ rating.getId() + separator + rating.getRating());
			bw.newLine();
		}

		br.close();
		bw.close();
	}

	private static void generateQuotedFile(String docfile, String filename,
			String separator, String quote) throws IOException {
		BufferedWriter bw = null;
		bw = new BufferedWriter(new FileWriter(new File(filename), false));
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(FileIO.openFile(docfile)));
		// int ratingId = 1;
		String line = "";
		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "\t");
			Integer docId = Integer.parseInt(st.nextToken());
			Integer documentId = Integer.parseInt(st.nextToken());
			// Integer rat = Integer.parseInt(st.nextToken());

			DocumentDescription docDesc = docDescriptions.get(docId);
			DocumentSolution docSol = docSolutions.get(documentId);
			// RatingResult rating = new RatingResult();
			// rating.setId(ratingId++);
			// rating.setRating(rat);

			bw.write(getDescription(docDesc, separator, quote) + separator
					+ getSolution(docSol, separator, quote));
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
			parseDescriptions("main/foundation/data/u.documentDescription");
			parseSolutions("main/foundation/data/u.documentSolution");

			generateNewFile("main/foundation/data/u.ratingResult",
					"src/main/foundation/data/documentCasesQuotedRated.txt",
					"|", "'");
			generateQuotedFile("main/foundation/data/u.docDescDocSol",
					"src/main/foundation/data/documentCasesQuoted.txt", "|",
					"'");
		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(
					LearningObjectDocDataBaseCreater.class).error(e, e);

		}

		System.out
				.println("File created: documentCasesQuotes.txt and documentCasesQuotedRated.txt");
	}

}
