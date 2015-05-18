package swp15.link_discovery.controller;

import swp15.link_discovery.view.ActiveLearningResultView;

/**
 * Controller that corresponds to the view
 * 
 * @author Felix Brei
 *
 */
public class ActiveLearningResultController extends ResultController {

	/**
	 * The corresponding view
	 */
	private ActiveLearningResultView view;

	/**
	 * Default constructor
	 * 
	 * @param v
	 *            View that is to be observed
	 */
	public ActiveLearningResultController(ActiveLearningResultView v) {
		super(v);
		view = v;
	}
}
