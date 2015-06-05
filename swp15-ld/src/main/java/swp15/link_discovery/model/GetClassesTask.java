package swp15.link_discovery.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.concurrent.Task;

import com.hp.hpl.jena.rdf.model.Model;

import de.konrad.commons.sparql.SPARQLHelper;
import de.uni_leipzig.simba.io.KBInfo;

public class GetClassesTask extends Task<List<ClassMatchingNode>> {
	private KBInfo info;
	private Model model;

	GetClassesTask(KBInfo info, Model model) {
		this.info = info;
		this.model = model;
	}

	/**
	 * Get classes for matching.
	 */
	@Override
	protected List<ClassMatchingNode> call() throws Exception {
		Set<String> rootClasses = SPARQLHelper.rootClasses(info.endpoint,
				info.graph, model);
		List<ClassMatchingNode> result = getClassMatchingNodes(rootClasses);
		return result;
	}

	private List<ClassMatchingNode> getClassMatchingNodes(Set<String> classes) {
		if (isCancelled()) {
			return null;
		}
		List<ClassMatchingNode> result = new ArrayList<ClassMatchingNode>();
		for (String class_ : classes) {
			try {
				List<ClassMatchingNode> children = getClassMatchingNodes(SPARQLHelper
						.subclassesOf(info.endpoint, info.graph, class_, model));
				result.add(new ClassMatchingNode(new URI(class_), children));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
