package swp15.link_discovery.model;

import javafx.beans.property.SimpleStringProperty;

public class PropertyPair {
	private SimpleStringProperty sourceProperty = new SimpleStringProperty();
	private SimpleStringProperty targetProperty = new SimpleStringProperty();

	public PropertyPair(String sourceProperty, String targetProperty) {
		setSourceProperty(sourceProperty);
		setTargetProperty(targetProperty);
	}

	public java.lang.String getSourceProperty() {
		return sourceProperty.get();
	}

	public void setSourceProperty(java.lang.String sourceProperty) {
		this.sourceProperty.set(sourceProperty);
	}

	public java.lang.String getTargetProperty() {
		return targetProperty.get();
	}

	public void setTargetProperty(java.lang.String targetProperty) {
		this.targetProperty.set(targetProperty);
	}
}
