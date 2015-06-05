package swp15.link_discovery.model;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;

import com.hp.hpl.jena.rdf.model.Model;

import de.konrad.commons.sparql.PrefixHelper;
import de.konrad.commons.sparql.SPARQLHelper;
import de.uni_leipzig.simba.io.KBInfo;

public class GetPropertiesTask extends Task<List<String>> {
	private KBInfo info;
	private Model model;
	private ClassMatchingNode class_;

	public GetPropertiesTask(KBInfo info, Model model, ClassMatchingNode class_) {
		this.info = info;
		this.model = model;
		this.class_ = class_;
	}

	@Override
	protected List<String> call() throws Exception {
		List<String> result = new ArrayList<String>();
		for (String property : SPARQLHelper.properties(info.endpoint,
				info.graph, class_.getUri().toString(), model)) {
			result.add(PrefixHelper.abbreviate(property));
		}
		return result;
	}
}
