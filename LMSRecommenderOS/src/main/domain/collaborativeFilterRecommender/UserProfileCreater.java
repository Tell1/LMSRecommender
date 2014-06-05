package main.domain.collaborativeFilterRecommender;

import java.util.HashSet;

import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.recommendation.ContentBasedProfile.CreateProfile;
import main.domain.cases.ProfileDescription;
import main.domain.cases.ProfileDescription.Degree;
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
public class UserProfileCreater {

	public static void main(String[] args) {
		CBRQuery query = new CBRQuery();
		CourseSet doneCourses = new CourseSet(new HashSet<Course>());
		doneCourses.add(new Course(1, "Algoritmia"));
		DocSet doneDocs = new DocSet(new HashSet<Document>());
		doneDocs.add(new Document(1, "The Art of Computer Programming"));
		ExeSet doneExes = new ExeSet(new HashSet<Exercise>());
		doneExes.add(new Exercise(1, "Ejercicios de Problemas-NP"));
		query.setDescription(new ProfileDescription(305, 23,
				Degree.Informatica, "94086", 7, 2, 2, doneCourses, doneDocs,
				doneExes));
		CreateProfile.createProfile(query,
				"src/main/domain/collaborativeRecommender/profile1.xml");
		System.out.println("Profile created: profile1.xml");
	}
}
