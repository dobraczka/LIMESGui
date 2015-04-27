package swp15.link_discovery.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.InstanceProperty;
import swp15.link_discovery.model.Result;
import swp15.link_discovery.view.ResultView;
import de.uni_leipzig.simba.data.Instance;

public class ResultController {
	private ResultView view;
	private Config currentConfig;
	
	public ResultController(ResultView view){
		this.view = view;
	}
	public void setCurrentConfig(Config c){
		
		this.currentConfig = c;
		
	}
	

	public void showProperties(Result item){
		String sourceURI = item.getSourceURI();
		String targetURI = item.getTargetURI();
		ObservableList<InstanceProperty> sourcePropertyList =FXCollections.observableArrayList();
		ObservableList<InstanceProperty> targetPropertyList =FXCollections.observableArrayList();
		
		Instance i1 = currentConfig.getSourceCache().getInstance(sourceURI);
		Instance i2 = currentConfig.getTargetCache().getInstance(targetURI);
		for(String prop : i1.getAllProperties()) {
			String value = "";
			for(String s : i1.getProperty(prop)) {
				value+= s+" ";
			}
			sourcePropertyList.add(new InstanceProperty(prop, value));
			System.out.println(prop + value);
		}
		view.showSourceInstance(sourcePropertyList);
		
		for(String prop : i2.getAllProperties()) {
			String value = "";
			for(String s : i2.getProperty(prop)) {
				value+= s+" ";
			}
			targetPropertyList.add(new InstanceProperty(prop, value));
			System.out.println(prop + value);
		}
		view.showTargetInstance(targetPropertyList);
	
		
	}
}
