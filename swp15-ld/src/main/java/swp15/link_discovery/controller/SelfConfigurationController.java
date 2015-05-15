package swp15.link_discovery.controller;

import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.SelfConfigurationModelInterface;
import swp15.link_discovery.view.SelfConfigurationView;

/**
 * Controller of SelfConfigurationView
 * 
 * @author Sascha Hahne, Daniel Obraczka
 *
 */
public class SelfConfigurationController {

	/**
	 * Corresponding SelfConfigurationView
	 */
	private SelfConfigurationView view;

	/**
	 * Corresponding SelfConfigurationModelInterface
	 */
	private SelfConfigurationModelInterface model;

	/**
	 * config which is used
	 */
	private Config currentConfig;

	/**
	 * Constructor
	 * 
	 * @param view
	 *            corresponding view
	 */
	public SelfConfigurationController(SelfConfigurationView view) {
		this.view = view;
	}

	/**
	 * Sets the model
	 * 
	 * @param model
	 *            to be set
	 */
	public void setModel(SelfConfigurationModelInterface model) {
		this.model = model;
	}

	/**
	 * returns the model
	 * 
	 * @return model
	 */
	public SelfConfigurationModelInterface getModel() {
		return this.model;
	}

	/**
	 * sets the config
	 * 
	 * @param conf
	 *            to be set
	 */
	public void setCurrentConfig(Config conf) {
		this.currentConfig = conf;
	}

	/**
	 * starts the learning by calling the corresponding method of the model
	 */
	public void learn() {
		this.model.learn(currentConfig, view.selfConfigPanel);
	}
}
