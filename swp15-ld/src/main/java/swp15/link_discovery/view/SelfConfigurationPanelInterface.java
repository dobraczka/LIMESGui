package swp15.link_discovery.view;

import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import swp15.link_discovery.model.SelfConfigurationModelInterface;

public class SelfConfigurationPanelInterface {
	public SelfConfigurationModelInterface model;
	public SelfConfigurationView view;
	public Button learnButton;
	public Button mapButton;
	public ProgressIndicator progressIndicator;
	double[] UIparams;

	public SelfConfigurationPanelInterface(SelfConfigurationView view) {
		this.view = view;
	};

	public double[] getUIParams() {

		return this.UIparams;
	}
}
