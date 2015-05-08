package swp15.link_discovery.controller;

import swp15.link_discovery.model.Config;
import swp15.link_discovery.view.EditEndpointsView;
import de.uni_leipzig.simba.io.KBInfo;

public class EditEndpointsController {
	private Config config;

	EditEndpointsController(Config config, EditEndpointsView view) {
		this.config = config;
		view.setController(this);
		KBInfo sourceEndpoint = config.getSourceInfo();
		view.setFields(true, sourceEndpoint.endpoint, sourceEndpoint.id,
				sourceEndpoint.graph, Integer.toString(sourceEndpoint.pageSize));
		KBInfo targetEndpoint = config.getTargetInfo();
		view.setFields(false, targetEndpoint.endpoint, targetEndpoint.id,
				targetEndpoint.graph, Integer.toString(targetEndpoint.pageSize));
	}

	public void save(boolean source, String endpointURL, String idNamespace,
			String graph, String pageSize) {
		KBInfo endpoint = source ? config.getSourceInfo() : config
				.getTargetInfo();
		endpoint.endpoint = endpointURL;
		endpoint.id = idNamespace;
		endpoint.graph = graph;
		// TODO: Validierung
		endpoint.pageSize = Integer.parseInt(pageSize);
	}
}
