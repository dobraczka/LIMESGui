package swp15.link_discovery.controller;

import static swp15.link_discovery.util.SourceOrTarget.SOURCE;
import static swp15.link_discovery.util.SourceOrTarget.TARGET;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.Endpoint;
import swp15.link_discovery.util.SourceOrTarget;
import swp15.link_discovery.view.EditEndpointsView;
import swp15.link_discovery.view.IEditView;
import de.uni_leipzig.simba.io.KBInfo;

/**
 * Controls EditEndpointsView
 * 
 * @author Manuel Jacob, Felix Brei
 *
 */
public class EditEndpointsController implements IEditController {
	/**
	 * Config of the LIMES Query
	 */
	private Config config;

	private EditEndpointsView view;

	/**
	 * Constructor
	 * 
	 * @param config
	 *            Config of Limes Query
	 * @param view
	 *            corresponding EditEndpointsView
	 */
	EditEndpointsController(Config config, EditEndpointsView view) {
		this.config = config;
		this.view = view;
		view.setController(this);
	}

	@Override
	public void load() {
		KBInfo sourceEndpoint = config.getSourceInfo();
		view.setFields(SOURCE, sourceEndpoint.endpoint, sourceEndpoint.id,
				sourceEndpoint.graph, Integer.toString(sourceEndpoint.pageSize));
		KBInfo targetEndpoint = config.getTargetInfo();
		view.setFields(TARGET, targetEndpoint.endpoint, targetEndpoint.id,
				targetEndpoint.graph, Integer.toString(targetEndpoint.pageSize));
	}

	/**
	 * Saves edited Endpoint
	 * 
	 * @param source
	 *            if True Source else Target
	 * @param endpointURL
	 *            URL of the Endpoint
	 * @param idNamespace
	 *            Namespace of Endpoint
	 * @param graph
	 *            TODO
	 * @param pageSize
	 *            length of Query
	 */
	public void save(SourceOrTarget sourceOrTarget, String endpointURL,
			String idNamespace, String graph, String pageSize) {
		Endpoint endpoint = sourceOrTarget == SOURCE ? config
				.getSourceEndpoint() : config.getTargetEndpoint();
		KBInfo info = endpoint.getInfo();
		info.endpoint = endpointURL;
		info.id = idNamespace;
		info.graph = graph;
		// TODO: Validierung
		info.pageSize = Integer.parseInt(pageSize);
		endpoint.update();
	}

	@Override
	public IEditView getView() {
		return view;
	}
}
