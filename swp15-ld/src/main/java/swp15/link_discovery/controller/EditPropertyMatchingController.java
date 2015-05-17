package swp15.link_discovery.controller;

import java.util.List;

import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.PropertyPair;
import swp15.link_discovery.view.EditPropertyMatchingView;
import swp15.link_discovery.view.IEditView;

public class EditPropertyMatchingController implements IEditController {
	private Config config;
	private EditPropertyMatchingView view;

	public EditPropertyMatchingController(Config config,
			EditPropertyMatchingView view) {
		this.config = config;
		this.view = view;
		view.setController(this);
	}

	@Override
	public void load() {
		view.showAvailableProperties(config.getSourceEndpoint()
				.getPossibleProperties(), config.getTargetEndpoint()
				.getPossibleProperties());
	}

	@Override
	public IEditView getView() {
		return view;
	}

	public void save(List<PropertyPair> propertyPairs) {
		config.setPropertiesMatching(propertyPairs);
	}
}
