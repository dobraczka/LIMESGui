package swp15.link_discovery.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Result {
	private final SimpleStringProperty sourceURI;
	private final SimpleStringProperty targetURI;
	private final SimpleDoubleProperty value;

	public Result(String sourceURI, String targetURI, Double value) {
		this.sourceURI = new SimpleStringProperty(sourceURI);
		this.targetURI = new SimpleStringProperty(targetURI);
		this.value = new SimpleDoubleProperty(value);
	}

	public String getSourceURI() {
		return sourceURI.get();
	}

	public String getTargetURI() {
		return targetURI.get();
	}

	public double getValue() {
		return value.get();
	}
}
