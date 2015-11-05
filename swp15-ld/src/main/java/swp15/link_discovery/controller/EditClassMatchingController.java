package swp15.link_discovery.controller;

import static swp15.link_discovery.util.SourceOrTarget.SOURCE;
import static swp15.link_discovery.util.SourceOrTarget.TARGET;
import swp15.link_discovery.model.ClassMatchingNode;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.Endpoint;
import swp15.link_discovery.model.GetClassesTask;
import swp15.link_discovery.view.EditClassMatchingView;
import swp15.link_discovery.view.IEditView;
import swp15.link_discovery.view.TaskProgressView;

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
		TaskProgressView taskProgressView = new TaskProgressView("Get classes");
		Endpoint sourceEndpoint = config.getSourceEndpoint();
		GetClassesTask getSourceClassesTask = sourceEndpoint
				.createGetClassesTask(taskProgressView);
		Endpoint targetEndpoint = config.getTargetEndpoint();
		GetClassesTask getTargetClassesTask = targetEndpoint
				.createGetClassesTask(taskProgressView);

		TaskProgressController taskProgressController = new TaskProgressController(
				taskProgressView);
		taskProgressController.addTask(
				getSourceClassesTask,
				items -> {
					view.showTree(SOURCE, items,
							sourceEndpoint.getCurrentClass());
				},
				error -> {
					view.showError("Error while loading source classes",
							error.getMessage());
				});
		taskProgressController.addTask(
				getTargetClassesTask,
				items -> {
					view.showTree(TARGET, items,
							targetEndpoint.getCurrentClass());
				},
				error -> {
					view.showError("Error while loading target classes",
							error.getMessage());
				});
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
