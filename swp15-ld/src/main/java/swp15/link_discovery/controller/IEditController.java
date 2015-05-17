package swp15.link_discovery.controller;

import swp15.link_discovery.view.IEditView;

public interface IEditController {
	public void load();

	public default void save() {
		getView().save();
	}

	public IEditView getView();
}
