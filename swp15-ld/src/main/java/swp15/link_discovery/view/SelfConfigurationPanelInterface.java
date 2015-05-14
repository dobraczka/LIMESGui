package swp15.link_discovery.view;

import javafx.scene.control.Button;
import swp15.link_discovery.model.SelfConfigurationModelInterface;

public class SelfConfigurationPanelInterface {
	protected SelfConfigurationModelInterface model;
	protected SelfConfigurationView view;
	protected Button learnButton;
	protected Button mapButton;

	public SelfConfigurationPanelInterface(SelfConfigurationView view) {
		this.view = view;
	}

}
