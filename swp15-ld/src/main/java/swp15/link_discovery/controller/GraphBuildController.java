package swp15.link_discovery.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.metric.MetricParser;
import swp15.link_discovery.model.metric.Node;
import swp15.link_discovery.model.metric.Output;
import swp15.link_discovery.view.graphBuilder.GraphBuildView;
import swp15.link_discovery.view.graphBuilder.NodeView;

public class GraphBuildController {
	private Config currentConfig;
	private GraphBuildView view;

	public GraphBuildController(Config currentConfig, GraphBuildView view) {
		this.currentConfig = currentConfig;
		this.view = view;
	}

	public GraphBuildController(GraphBuildView view) {

		this.view = view;
	}

	public void setConfig(Config currentConfig) {
		this.currentConfig = currentConfig;
	}

	public void generateGraphFromConfig() {
		Output out = MetricParser.parse(
				currentConfig.getConfigReader().metricExpression,
				currentConfig.getSourceInfo().var.replaceAll("\\?", ""));
		ObservableList<NodeView> newNodeList = FXCollections
				.observableArrayList();
		NodeView outView = new NodeView(200, 200, NodeView.OUTPUT, out.id,
				view, out);
		newNodeList.add(outView);
		view.nodeList = drawChildRek(outView,
				outView.nodeData.getChilds().get(0), newNodeList);

		view.draw();
	}

	public ObservableList<NodeView> drawChildRek(NodeView parent, Node node,
			ObservableList<NodeView> nodeList) {
		int nodeShape = 1;
		NodeView thisNode = new NodeView(200, 200, nodeShape, node.id, view,
				node);
		nodeList.add(thisNode);
		parent.addChildWithOutDataLinking(thisNode);
		if (node.getMaxChilds() == 0) {
			return nodeList;
		} else {
			drawChildRek(thisNode, node.getChilds().get(0), nodeList);
			drawChildRek(thisNode, node.getChilds().get(1), nodeList);
			return nodeList;
		}
	}

}
