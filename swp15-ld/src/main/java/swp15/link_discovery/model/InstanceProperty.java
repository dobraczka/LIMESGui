package swp15.link_discovery.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Contains the properties of an instance
 * 
 * @author Sascha Hahne, Daniel Obraczka
 *
 */
public class InstanceProperty {

	/**
	 * name of property
	 */
	private final SimpleStringProperty property;

	/**
	 * value of property
	 */
	private final SimpleStringProperty value;

	/**
	 * Constructor
	 * 
	 * @param property
	 *            name of property
	 * @param value
	 *            value of property
	 */
	public InstanceProperty(String property, String value) {
		this.property = new SimpleStringProperty(property);
		this.value = new SimpleStringProperty(value);
	}

	/**
	 * returns the name of the property
	 * 
	 * @return name of the property
	 */
	public String getProperty() {
		return property.get();
	}

	/**
	 * returns the value of the property
	 * 
	 * @return value of the property
	 */
	public String getValue() {
		return value.get();
	}
}
