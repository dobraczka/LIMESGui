package swp15.link_discovery.view;

import java.util.List;
import java.util.Set;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.metric.Measure;
import swp15.link_discovery.model.metric.Node;
import swp15.link_discovery.model.metric.Operator;

public class ToolBox extends VBox {
	private TreeView<String> toolBoxTree;

	private TreeItem<String> toolBoxSourceProperties;

	private TreeItem<String> toolBoxTargetProperties;

	private TreeItem<String> toolBoxOperators;

	private TreeItem<String> toolBoxMetrics;

	public ToolBox() {
		this.toolBoxTree = generateTreeView();
		this.getChildren().add(toolBoxTree);

	}

	private TreeView<String> generateTreeView() {
		TreeItem<String> root = new TreeItem<String>("Tool Box");
		TreeView<String> result = new TreeView<String>(root);
		toolBoxSourceProperties = new TreeItem<String>("Target Properties");
		toolBoxTargetProperties = new TreeItem<String>("Target Properties");
		toolBoxMetrics = generateTreeViewFromNodeIdentifiers("Metrics",
				Measure.identifiers);
		toolBoxOperators = generateTreeViewFromNodeIdentifiers("Operators",
				Operator.identifiers);
		root.getChildren().add(toolBoxSourceProperties);
		root.getChildren().add(toolBoxTargetProperties);
		root.getChildren().add(toolBoxMetrics);
		root.getChildren().add(toolBoxOperators);

		return result;

	}

	private TreeItem<String> generateTreeViewFromNodeIdentifiers(
			String rootName, Set<String> nodeIdentifiers) {

		TreeItem<String> root = new TreeItem<String>(rootName);
		// TreeItem<String> result = new TreeItem<String>(root);
		nodeIdentifiers.forEach((identifier) -> {
			TreeItem<String> item = new TreeItem<String>(identifier);

			root.getChildren().add(item);
		});

		return root;

	}

	public Node generateNode() {
		// TODO
		return null;
	}

	public void addSourceProperty() {
		// TODO
	}

	public void addTargetProperty() {
		// TODO
	}

	public void setTreeItems(TreeItem<String> root, List<String> items) {
		root.getChildren().clear();
		items.forEach(itemString -> {
			TreeItem<String> item = new TreeItem<String>(itemString);
			root.getChildren().add(item);
		});
	}

	public void showLoadedConfig(Config config) {
		setTreeItems(toolBoxSourceProperties, config.getSourceInfo().properties);
		setTreeItems(toolBoxTargetProperties, config.getTargetInfo().properties);
	}
}
