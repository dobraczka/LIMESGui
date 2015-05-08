package swp15.link_discovery.controller;

import swp15.link_discovery.model.Config;
import swp15.link_discovery.view.EditEndpointsView;
import de.uni_leipzig.simba.io.KBInfo;

/**
 * Controls EditEndpointsView
 * @author Manuel Jacob, Felix Brei
 *
 */
public class EditEndpointsController {
	/**
	 * Config of the LIMES Query
	 */
	private Config config;

	/**
	 * Constructor
	 * @param config Config of Limes Query
	 * @param view corresponding EditEndpointsView
	 */
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

	/**
	 * Saves edited Endpoint
	 * @param source if True Source else Target
	 * @param endpointURL URL of the Endpoint
	 * @param idNamespace Namespace of Endpoint
	 * @param graph TODO
	 * @param pageSize length of Query
	 */
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
