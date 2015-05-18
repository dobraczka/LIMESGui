package swp15.link_discovery.model;

import swp15.link_discovery.view.SelfConfigurationPanelInterface;

/**
 * Interface to add different Panels in SelfConfigView
 * 
 * @author Sascha Hahne
 *
 */
public interface SelfConfigurationModelInterface {

	/**
	 * Performs the selfconfiguration. Uses the parameters from the
	 * currentConfig and from the UI in SelfConfigurationView
	 * 
	 * @param currentConfig
	 * @param view
	 */
	public void learn(Config currentConfig, SelfConfigurationPanelInterface view);

}
