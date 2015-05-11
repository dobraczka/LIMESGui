package swp15.link_discovery.view;

import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.metric.Measure;
import swp15.link_discovery.model.metric.Node;
import swp15.link_discovery.model.metric.Operator;
import swp15.link_discovery.model.metric.Property;

public class ToolBox extends VBox {

	private ListView<String> toolBoxSourceProperties;

	private ListView<String> toolBoxTargetProperties;

	private ListView<String> toolBoxOperators;

	private ListView<String> toolBoxMetrics;

	private Label testLabel;

	private Config config;

	public ToolBox() {
		generateView(this);
		setListeners();
		this.config = null;
	}

	private void generateView(VBox box) {
		testLabel = new Label("test");
		box.getChildren().add(testLabel);

		toolBoxSourceProperties = new ListView<String>();
		toolBoxTargetProperties = new ListView<String>();
		toolBoxMetrics = generateListViewFromNodeIdentifiers(Measure.identifiers);
		toolBoxOperators = generateListViewFromNodeIdentifiers(Operator.identifiers);
		box.getChildren().add(new Label("Source Properties"));
		box.getChildren().add(toolBoxSourceProperties);
		box.getChildren().add(new Label("Target Properties"));
		box.getChildren().add(toolBoxTargetProperties);
		box.getChildren().add(new Label("Metrics"));
		box.getChildren().add(toolBoxMetrics);
		box.getChildren().add(new Label("Operators"));
		box.getChildren().add(toolBoxOperators);

	}

	public void setListeners() {
		toolBoxSourceProperties.setOnMouseClicked(e -> {
			generateProperty(toolBoxSourceProperties, true);
		});
		toolBoxTargetProperties.setOnMouseClicked(e -> {
			generateProperty(toolBoxTargetProperties, false);
		});
		toolBoxMetrics.setOnMouseClicked(e -> {
			generateNode(toolBoxMetrics);
		});
		toolBoxOperators.setOnMouseClicked(e -> {
			generateNode(toolBoxOperators);
		});
	}

	private void setListViewFromList(ListView<String> view, List<String> items) {
		ObservableList<String> listItems = FXCollections.observableArrayList();
		items.forEach(itemString -> {
			listItems.add(itemString);
		});
		view.setItems(listItems);

	}

	private ListView<String> generateListViewFromNodeIdentifiers(
			Set<String> nodeIdentifiers) {
		ObservableList<String> listItems = FXCollections.observableArrayList();

		nodeIdentifiers.forEach((identifier) -> {
			listItems.add(identifier);
		});

		return new ListView<String>(listItems);

	}

	public void generateProperty(ListView<String> view, boolean origin) {
		if (view.getSelectionModel().getSelectedItem() != null) {
			Property gen = null;
			if (origin) {
				gen = new Property(config.getPropertyString(view
						.getSelectionModel().getSelectedIndex(), true),
						Property.Origin.SOURCE);
			} else {
				gen = new Property(config.getPropertyString(view
						.getSelectionModel().getSelectedIndex(), false),
						Property.Origin.TARGET);

			}

			setNodeToGraph(gen);
		}
	}

	public void setNodeToGraph(Node e) {

	}

	public void generateNode(ListView<String> view) {
		if (view.getSelectionModel().getSelectedItem() != null) {
			Node node = Node.createNode(view.getSelectionModel()
					.getSelectedItem());
			setNodeToGraph(node);
		}

	}

	public void addSourceProperty() {
		// TODO
	}

	public void addTargetProperty() {
		// TODO
	}

	public void showLoadedConfig(Config config) {
		this.config = config;
		setListViewFromList(toolBoxSourceProperties,
				config.getSourceInfo().properties);
		setListViewFromList(toolBoxTargetProperties,
				config.getTargetInfo().properties);
	}

}
