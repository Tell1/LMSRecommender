package main.domain.cases;

import java.util.Set;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;
import main.domain.CMS.Course;
import main.domain.CMS.Document;
import main.domain.CMS.Exercise;
import main.foundation.dataTypes.CourseSet;
import main.foundation.dataTypes.DocSet;
import main.foundation.dataTypes.ExeSet;

/**
 * Java Bean that stores the description of the case.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * @version 1.0
 */
public class DocumentDescription implements CaseComponent {

	public enum Courses {
		Algoritmia, Aplicaciones_de_Bases_de_Datos, Computacion_Neuronal_y_Evolutiva, Interaccion_Hombre_Maquina, Metodos_Numericos_y_Optimizacion, Mineria_de_Datos, Procesadores_del_Lenguaje, Sistemas_Distribuidos, Inteligencia_Artificial, Redes, Sistemas_Inteligentes, Arquitecturas_Paralelas, Control_por_Computador, Seguridad_Informatica, Organizacion_y_Gestion_de_Empresas, Otro, Nada
	};

	public enum Subjects {
		Complejidad, SQL, Redes_Neuronales, Interfaz_Hombre_Maquina, Metodos_de_Optimizacion, Classificadores, Analisis_Sintactico, P2P, Razonamiento_basado_en_Casos, TCP_IP, Sistemas_basado_en_reglas, Conectividad, Estructuras_de_control, Seguridad_en_Redes, Tipos_de_Organizacion_de_Empresas, Otro, Nada, Divide_y_venceras, Consultas, Perceptron_simple, Operadores, Programacion_lineal, Arboles, Identificacion, JavaRMI, Recuperacion, Client_Server, Busqueda_en_Grafos, Paralelismo, Sistemas_Continuos, Seguridad_en_Sistemas_Operativos, Aspectos_Legales
	};

	Integer id;
	Courses course;
	Subjects subject;
	Integer difficulty;
	String keywords;
	CourseSet doneCourses;
	DocSet doneDocs;
	ExeSet doneExes;

	public DocumentDescription() {

	}

	public DocumentDescription(Integer id, Courses course, Subjects subject,
			Integer difficulty) {
		super();
		this.id = id;
		this.course = course;
		this.subject = subject;
		this.difficulty = difficulty;
	}

	public DocumentDescription(Integer id, Courses course, Subjects subject,
			Integer difficulty, DocSet doneDocs) {
		super();
		this.id = id;
		this.course = course;
		this.subject = subject;
		this.difficulty = difficulty;
		this.doneDocs = doneDocs;
	}

	public DocumentDescription(Integer id, Courses course, Subjects subject,
			Integer difficulty, String keywords, DocSet doneDocs) {
		super();
		this.id = id;
		this.course = course;
		this.subject = subject;
		this.difficulty = difficulty;
		this.keywords = keywords;
		this.doneDocs = doneDocs;
	}

	public DocumentDescription(Integer id, Courses course, Subjects subject,
			Integer difficulty, String keywords, CourseSet doneCourses,
			DocSet doneDocs, ExeSet doneExes) {
		super();
		this.id = id;
		this.course = course;
		this.subject = subject;
		this.difficulty = difficulty;
		this.keywords = keywords;
		this.doneCourses = doneCourses;
		this.doneDocs = doneDocs;
		this.doneExes = doneExes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Subjects getSubject() {
		return subject;
	}

	public void setSubject(Subjects subject) {
		this.subject = subject;
	}

	public Courses getCourse() {
		return course;
	}

	public void setCourse(Courses course) {
		this.course = course;
	}

	public Integer getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Set<Course> getDoneCourses() {
		return doneCourses;
	}

	public void setDoneCourses(Set<Course> doneCourses) {
		this.doneCourses = new CourseSet(doneCourses);
	}

	public Set<Document> getDoneDocs() {
		return doneDocs;
	}

	public void setDoneDocs(Set<Document> doneDocs) {
		this.doneDocs = new DocSet(doneDocs);
	}

	public Set<Exercise> getDoneExes() {
		return doneExes;
	}

	public void setDoneExes(Set<Exercise> doneExes) {
		this.doneExes = new ExeSet(doneExes);
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", this.getClass());
	}

	@Override
	public String toString() {
		return "DocumentDescription [id=" + id + ", course=" + course
				+ ", subject=" + subject + ", difficulty=" + difficulty
				+ ", keywords=" + keywords + ", doneCourses=" + doneCourses
				+ ", doneDocs=" + doneDocs + ", doneExes=" + doneExes + "]";
	}

}
