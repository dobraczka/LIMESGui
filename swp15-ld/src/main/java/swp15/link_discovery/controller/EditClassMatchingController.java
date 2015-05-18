package swp15.link_discovery.controller;

import static swp15.link_discovery.util.SourceOrTarget.SOURCE;
import static swp15.link_discovery.util.SourceOrTarget.TARGET;
import swp15.link_discovery.model.ClassMatchingNode;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.Endpoint;
import swp15.link_discovery.view.EditClassMatchingView;
import swp15.link_discovery.view.IEditView;

/**
 * Controller class for class matching step in create wizard
 * 
 * @author Manuel Jacob
 */
public class EditClassMatchingController implements IEditController {
	private Config config;
	private EditClassMatchingView view;

	EditClassMatchingController(Config config, EditClassMatchingView view) {
		this.config = config;
		this.view = view;
		view.setController(this);
	}

	@Override
	public void load() {
		Endpoint sourceEndpoint = config.getSourceEndpoint();
		view.showTree(SOURCE, sourceEndpoint.getClassesForMatching(),
				sourceEndpoint.getCurrentClass());
		Endpoint targetEndpoint = config.getTargetEndpoint();
		view.showTree(TARGET, targetEndpoint.getClassesForMatching(),
				targetEndpoint.getCurrentClass());
	}

	public void save(ClassMatchingNode sourceClass,
			ClassMatchingNode targetClass) {
		config.getSourceEndpoint().setCurrentClass(sourceClass);
		config.getTargetEndpoint().setCurrentClass(targetClass);
	}

	@Override
	public IEditView getView() {
		return view;
	}
}
