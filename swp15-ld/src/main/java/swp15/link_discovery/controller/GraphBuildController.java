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

/**
 * Controller of GraphBuildView, Controlls drawing and moving auf Node Elements
 * 
 * @author Daniel Obraczka, Sascha Hahne
 *
 */
public class GraphBuildController {

	/**
	 * CurrentConfig of the Limes query
	 */
	private Config currentConfig;
	/**
	 * Corresponding GraphBuildView
	 */
	private GraphBuildView graphBuildView;

	/**
	 * Constructor
	 * 
	 * @param currentConfig
	 *            Current Limes query Config
	 * @param view
	 *            Corresponding View
	 */
	public GraphBuildController(Config currentConfig, GraphBuildView view) {
		this.currentConfig = currentConfig;
		this.graphBuildView = view;
	}

	/**
	 * Constructor
	 * 
	 * @param view
	 *            Corresponding View
	 */
	public GraphBuildController(GraphBuildView view) {

		this.graphBuildView = view;
	}

	/**
	 * Set currentConfig
	 * 
	 * @param currentConfig
	 *            CurrentConfig of Limes query
	 */
	public void setConfig(Config currentConfig) {
		this.currentConfig = currentConfig;
	}

	/**
	 * Takes the Configuration and generates the Graph
	 */
	public void generateGraphFromConfig() {

		Output out = currentConfig.getMetric();
		ObservableList<NodeView> newNodeList = FXCollections
				.observableArrayList();
		NodeView outView = new NodeView(200, 200, NodeView.OUTPUT, out.id,
				graphBuildView, out);
		newNodeList.add(outView);
		graphBuildView.nodeList = drawChildRek(outView, outView.nodeData
				.getChilds().get(0), newNodeList);

		layoutGraph();
	}

	/**
	 * Takes the Graph and writes the Information to the Config
	 */
	public void setConfigFromGraph() {
		currentConfig.setMetricExpression(MetricParser.parse(
				graphBuildView.nodeList.get(0).nodeData.toString(),
				currentConfig.getSourceInfo().var.replaceAll("\\?", ""))
				.toString());
		System.out.println(MetricParser.parse(
				graphBuildView.nodeList.get(0).nodeData.toString(),
				currentConfig.getSourceInfo().var.replaceAll("\\?", ""))
				.toString());
	}

	/**
	 * Delete CurrentGraph
	 */
	public void deleteGraph() {
		graphBuildView.nodeList.clear();
		graphBuildView.edited = true;
		graphBuildView.addNode(300, 300, 2, new Output());
		graphBuildView.draw();
	};

	/**
	 * Refresh the Layout of the Graph
	 */
	public void layoutGraph() {
		double h = graphBuildView.getHeight();
		double w = graphBuildView.getWidth();
		List<Integer> stages = new ArrayList<Integer>();
		List<Integer> stages2;
		graphBuildView.nodeList.forEach(e -> {
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
		graphBuildView.nodeList.forEach(e -> {
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
		graphBuildView.draw();
	}

	/**
	 * Helper function for Layout Graph
	 * 
	 * @param index
	 *            index to set to 0
	 * @param stages
	 *            List of nodes per generation
	 * @return modified Stages
	 */
	private List<Integer> rekListAdder(int index, List<Integer> stages) {
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

	/**
	 * Recursive Function to Link NodeView as there Datamodell is linked
	 * 
	 * @param parent
	 *            Parent NodeView
	 * @param node
	 *            Node to be prooved
	 * @param nodeList
	 *            NodeList to be modified
	 * @return modified NodeList
	 */
	private ObservableList<NodeView> drawChildRek(NodeView parent, Node node,
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
		NodeView thisNode = new NodeView(200, 200, nodeShape, node.id,
				graphBuildView, node);

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
