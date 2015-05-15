package swp15.link_discovery.view;

import javafx.scene.control.Button;
import swp15.link_discovery.model.SelfConfigurationModelInterface;

public class SelfConfigurationPanelInterface {
	protected SelfConfigurationModelInterface model;
	public SelfConfigurationView view;
	public Button learnButton;
	public Button mapButton;

	public SelfConfigurationPanelInterface(SelfConfigurationView view) {
		this.view = view;
	}

	public double[] getUIParams() {
		// TODO Auto-generated method stub
		return null;
	}

}
