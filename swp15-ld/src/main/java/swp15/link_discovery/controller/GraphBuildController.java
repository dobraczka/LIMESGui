package swp15.link_discovery.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.metric.Measure;
import swp15.link_discovery.model.metric.MetricParser;
import swp15.link_discovery.model.metric.Node;
import swp15.link_discovery.model.metric.Operator;
import swp15.link_discovery.model.metric.Output;
import swp15.link_discovery.model.metric.Property;
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

		Output out = currentConfig.getMetric();
		ObservableList<NodeView> newNodeList = FXCollections
				.observableArrayList();
		NodeView outView = new NodeView(200, 200, NodeView.OUTPUT, out.id,
				view, out);
		newNodeList.add(outView);
		view.nodeList = drawChildRek(outView,
				outView.nodeData.getChilds().get(0), newNodeList);

		layoutGraph();
	}

	public void setConfigFromGraph() {
		currentConfig.setMetricExpression(MetricParser.parse(
				view.nodeList.get(0).nodeData.toString(),
				currentConfig.getSourceInfo().var.replaceAll("\\?", ""))
				.toString());
		System.out.println(MetricParser.parse(
				view.nodeList.get(0).nodeData.toString(),
				currentConfig.getSourceInfo().var.replaceAll("\\?", ""))
				.toString());
	}

	public void deleteGraph() {
		view.nodeList.clear();
		view.edited = true;
		view.addNode(300, 300, 2, new Output());
		view.draw();
	};

	public void layoutGraph() {
		double h = view.getHeight();
		double w = view.getWidth();
		List<Integer> stages = new ArrayList<Integer>();
		List<Integer> stages2;
		view.nodeList.forEach(e -> {
			int i = 0;
			NodeView test = e;
			while (test.parent != null) {
				test = test.parent;
				i++;
			}
			try {
				stages.get(i);
			} catch (IndexOutOfBoundsException exception) {
				try {
					stages.add(i, 0);
				} catch (IndexOutOfBoundsException e2) {
					rekListAdder(i, stages);
				}
			}
			stages.set(i,
					Integer.sum(Integer.max(stages.get(i).intValue(), 0), 1));

		});
		stages2 = new ArrayList<Integer>(stages);
		view.nodeList.forEach(e -> {
			int i = 0;
			int hInt = stages.size();
			NodeView test = e;
			while (test.parent != null) {
				test = test.parent;
				i++;
			}
			e.setXY((int) (w - ((w * stages2.get(i)) / stages.get(i))
					+ (w / (2 * stages.get(i))) - NodeView.WIDTH), (int) (h
					+ NodeView.HEIGHT - (((h * (i + 1)) / hInt))));
			stages2.set(i, stages2.get(i) - 1);
		});
		view.draw();
	}

	public List<Integer> rekListAdder(int index, List<Integer> stages) {
		try {
			stages.add(index - 1, 0);
			stages.add(index, 0);
			return stages;
		} catch (IndexOutOfBoundsException e) {
			rekListAdder(index - 1, stages);
			stages.add(index, 0);
			return stages;
		}
	}

	public ObservableList<NodeView> drawChildRek(NodeView parent, Node node,
			ObservableList<NodeView> nodeList) {
		int nodeShape;
		if (Measure.identifiers.contains(node.id)) {
			nodeShape = NodeView.METRIC;
		} else if (Operator.identifiers.contains(node.id)) {
			nodeShape = NodeView.OPERATOR;
		} else {
			Property castedNode = (Property) node;
			if (castedNode.getOrigin() == Property.Origin.SOURCE) {
				nodeShape = NodeView.SOURCE;
			} else {
				nodeShape = NodeView.TARGET;
			}
		}
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
