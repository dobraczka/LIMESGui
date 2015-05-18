package swp15.link_discovery.view;

import javafx.scene.control.TableView;
import swp15.link_discovery.controller.ActiveLearningResultController;
import swp15.link_discovery.model.ActiveLearningResult;

/**
 * Extends the original result view by adding the possibility to review matches
 * and continue the learning process
 * 
 * @author Felix Brei
 *
 */
public class ActiveLearningResultView extends ResultView {

	/**
	 * Table that contains the results of the learning process, extended by an
	 * 'is match'-flag
	 */
	private TableView<ActiveLearningResult> table;

	/**
	 * Corresponding controller
	 */
	private ActiveLearningResultController controller;

	/**
	 * Default constructor
	 */
	public ActiveLearningResultView() {
		this.controller = new ActiveLearningResultController(this);
	}

}