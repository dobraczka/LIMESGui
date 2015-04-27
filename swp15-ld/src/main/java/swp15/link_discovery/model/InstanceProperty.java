package swp15.link_discovery.model;

import javafx.beans.property.SimpleStringProperty;

public class InstanceProperty {
	private final SimpleStringProperty property;
	private final SimpleStringProperty value;
	
	
	public InstanceProperty(String property, String value){
		this.property = new SimpleStringProperty(property);
		this.value = new SimpleStringProperty(value);
	}
	public String getProperty(){
		return property.get();
	}
	public String getValue(){
		return value.get();
	}
}
