package swp15.link_discovery.controller;

import swp15.link_discovery.view.IEditView;

/**
 * Interface for controllers for step in wizard
 * 
 * @author Manuel Jacob
 */
public interface IEditController {
	/**
	 * Load data entered in this wizard step
	 */
	public void load();

	/**
	 * Save data entered in this wizard step
	 */
	public default void save() {
		getView().save();
	}

	public IEditView getView();
}
