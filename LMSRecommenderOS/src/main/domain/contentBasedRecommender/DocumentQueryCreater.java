package main.domain.contentBasedRecommender;

import java.util.HashSet;

import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.recommendation.ContentBasedProfile.CreateProfile;
import main.domain.cases.DocumentDescription;
import main.domain.cases.DocumentDescription.Courses;
import main.domain.cases.DocumentDescription.Subjects;
import main.domain.CMS.Course;
import main.domain.CMS.Document;
import main.domain.CMS.Exercise;
import main.foundation.dataTypes.CourseSet;
import main.foundation.dataTypes.DocSet;
import main.foundation.dataTypes.ExeSet;

/**
 * Creates the user profile.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 * 
 */
public class DocumentQueryCreater {

	public static void main(String[] args) {

		CBRQuery query = new CBRQuery();
		CourseSet doneCourses = new CourseSet(new HashSet<Course>());
		doneCourses.add(new Course(1, "Algoritmia"));
		DocSet doneDocs = new DocSet(new HashSet<Document>());
		doneDocs.add(new Document(1,
				"Successfull Case-based Reasoning Applications"));
		ExeSet doneExes = new ExeSet(new HashSet<Exercise>());
		doneExes.add(new Exercise(1, "Ejercicios de complejidades"));
		query.setDescription(new DocumentDescription(1, Courses.Algoritmia,
				Subjects.Complejidad, 3,"O-Notacion", doneDocs));
		CreateProfile.createProfile(query,
				"src/main/domain/filterBasedRecommender/document1.xml");
		System.out.println("Document created: document1.xml");
	}
}
