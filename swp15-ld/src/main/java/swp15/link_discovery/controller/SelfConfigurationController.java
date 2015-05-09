package swp15.link_discovery.controller;

import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.SelfConfigurationModelInterface;
import swp15.link_discovery.view.SelfConfigurationView;

public class SelfConfigurationController {

	private SelfConfigurationView view;
	private SelfConfigurationModelInterface model;
	private Config currentConfig;
	
	public SelfConfigurationController(SelfConfigurationView view) {
		this.view = view;
	}
	
	public void setModel(SelfConfigurationModelInterface model){
		this.model = model;
	}
	public SelfConfigurationModelInterface getModel(){
		return this.model;
	}
	public void setCurrentConfig(Config conf){
		this.currentConfig = conf;
	}
	public void learn(){
		this.model.learn(currentConfig, view);
	}
}
