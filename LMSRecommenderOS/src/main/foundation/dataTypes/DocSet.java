package main.foundation.dataTypes;

import java.util.HashSet;
import java.util.Set;

import main.domain.CMS.Document;

/**
 * Class that represents a set of documents and extends the TypeSet class to be
 * able to used in the connecters of jcolibri.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 * 
 */
public class DocSet extends TypeSet<Document> {

	public DocSet() {
		this.set = new HashSet<Document>();
	}

	public DocSet(Set<Document> doneDocs) {
		set = doneDocs;
	}

	// [Document [name=Algoritmia], Document [name=Inteligencia Artificial]]
	@Override
	public void fromString(String data) throws Exception {

		StringBuilder sb = new StringBuilder();
		char thisChar = ' ';
		Integer id = 1;
		int j = 0;
		for (int c = j; c < data.length(); c++) {
			thisChar = data.charAt(c);
			if (thisChar == '=') {
				for (j = c + 1; thisChar != ']'; j++) {
					thisChar = data.charAt(j);
					if (thisChar != '[' && thisChar != ']') {
						sb.append(thisChar);
					}
				}
				set.add(new Document(id++, sb.toString()));
				sb = new StringBuilder();
			}
		}
	}

}
