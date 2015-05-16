package swp15.link_discovery.view;

import javafx.scene.control.Button;
import swp15.link_discovery.model.SelfConfigurationModelInterface;

public class SelfConfigurationPanelInterface {
	public SelfConfigurationModelInterface model;
	public SelfConfigurationView view;
	public Button learnButton;
	public Button mapButton;
	double[] UIparams;

	public SelfConfigurationPanelInterface(SelfConfigurationView view) {
		this.view = view;
	};

	public double[] getUIParams() {

		return this.UIparams;
	}
}
