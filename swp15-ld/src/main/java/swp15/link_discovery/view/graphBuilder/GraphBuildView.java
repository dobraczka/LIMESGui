package swp15.link_discovery.view.graphBuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import swp15.link_discovery.controller.GraphBuildController;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.metric.Node;
import swp15.link_discovery.model.metric.Output;

public class GraphBuildView extends Canvas {

	/**
	 * Corresponding GraphBuildController
	 */
	public GraphBuildController graphBuildController;

	/**
	 * List of nodes in the Canvas
	 */
	public ObservableList<NodeView> nodeList;

	/**
	 * True if Node was clicked
	 */
	private boolean nodeClicked;

	/**
	 * Index of clicked Node
	 */
	private int nodeIndex;

	/**
	 * Context Menu to show on secondary MouseClick
	 */
	private NodeContextMenu contextMenu;

	/**
	 * True if Linking Process is running
	 */
	public boolean isLinking;

	/**
	 * Node to Link in linking Process
	 */
	public NodeView linkNode;

	/**
	 * Boolean to check if Graph was edited
	 */
	public boolean edited = false;

	/**
	 * Mouseposition on Canvas
	 */
	private double[] mousePosition = { 0, 0 };

	/**
	 * Constructor
	 * 
	 * @param currentConfig
	 *            Current used Configmodel
	 */
	public GraphBuildView(Config currentConfig) {
		widthProperty().addListener(evt -> draw());
		heightProperty().addListener(evt -> draw());
		this.nodeList = FXCollections.observableArrayList();
		this.nodeClicked = false;
		this.isLinking = false;
		addNode(300, 300, 2, new Output());
		this.graphBuildController = new GraphBuildController(currentConfig,
				this);
	}

	/**
	 * Constructor
	 */
	public GraphBuildView() {
		widthProperty().addListener(evt -> draw());
		heightProperty().addListener(evt -> draw());
		this.nodeList = FXCollections.observableArrayList();
		this.nodeClicked = false;
		this.isLinking = false;
		addNode(300, 300, 2, new Output());
		this.graphBuildController = new GraphBuildController(this);
	}

	/**
	 * Set Resizablity to true,overwrite default Canvas Property
	 */
	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	/**
	 * Set Width of Canvas
	 * @param width
	 */
	public double prefWidth(double height) {
		return getWidth();
	}

	/**
	 * Set Height of Canvas
	 * 
	 * @params height
	 */
	@Override
	public double prefHeight(double width) {
		return getHeight();
	}

	/**
	 * Set Current Config model
	 * 
	 * @param config
	 *            Currently used Config
	 */
	public void setCurrentConfig(Config config) {
		this.graphBuildController.setConfig(config);
	}

	/**
	 * Add eventlisteners Begin drawing
	 */
	public void start() {
		this.addEventHandler(
				MouseEvent.MOUSE_CLICKED,
				e -> {
					if (isLinking) {
						for (NodeView node : nodeList) {
							if (node.contains((int) e.getX(), (int) e.getY())) {
								isLinking = false;
								if (linkNode.addParent(node)) {
								} else {
									Alert alert = new Alert(
											AlertType.INFORMATION);
									alert.setContentText("Clicked Node is no valid Parent!");
									alert.showAndWait();
								}
								edited = true;
								draw();
								break;
							}
						}
					}
				});
		this.addEventHandler(
				MouseEvent.MOUSE_PRESSED,
				e -> {
					if (e.getButton().equals(MouseButton.PRIMARY)) {
						if (e.getClickCount() == 2) {
							int index = 0;
							boolean clickedOutput = false;
							for (NodeView node : nodeList) {
								if (node.contains((int) e.getX(),
										(int) e.getY())) {
									nodeIndex = index;
									if (node.nodeShape == 2) {
										clickedOutput = true;
									}
									break;
								}
								index++;
							}
							if (clickedOutput) {
								ThresholdModifyView tmv = new ThresholdModifyView(
										this);
								edited = true;
								draw();
							}
						}
					}
				});

		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			int index = 0;
			for (NodeView node : nodeList) {
				if (node.contains((int) e.getX(), (int) e.getY())) {
					nodeClicked = true;
					nodeIndex = index;
					break;
				}
				index++;
			}
		});
		this.addEventHandler(
				MouseEvent.MOUSE_DRAGGED,
				e -> {
					if (nodeClicked) {
						nodeList.get(nodeIndex).setXY(
								(int) e.getX() - (NodeView.WIDTH) / 2,
								(int) e.getY() - (NodeView.HEIGHT) / 2);
						draw();
					}
				});
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
			nodeClicked = false;
		});
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				int index = 0;
				boolean clickedNode = false;
				for (NodeView node : nodeList) {
					if (node.contains((int) e.getX(), (int) e.getY())) {
						nodeIndex = index;
						clickedNode = true;
						break;
					}
					index++;
				}
				if (clickedNode) {
					contextMenu = new NodeContextMenu(this, nodeIndex);
					contextMenu.show(this, e.getScreenX(), e.getScreenY());
				}
			}
		});
		this.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
			if (isLinking) {
				mousePosition[0] = e.getX();
				mousePosition[1] = e.getY();
				draw();
			}
		});
		draw();
	}

	/**
	 * Draw Nodes and Links to the Canvas
	 */
	public void draw() {
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
		if (isLinking) {
			gc.strokeLine(linkNode.x + NodeView.WIDTH / 2, linkNode.y
					+ NodeView.HEIGHT / 2, mousePosition[0], mousePosition[1]);
		}
		nodeList.forEach(e -> {
			e.drawLink();
		});
		nodeList.forEach(e -> {
			e.displayNode();
		});
	}

	/**
	 * Adds a Node to the Canvas
	 * 
	 * @param x
	 *            Position on x-Axis
	 * @param y
	 *            Position on y-Axis
	 * @param shape
	 *            Shape of Node
	 * @param node
	 *            Node Data Model
	 */
	public void addNode(int x, int y, int shape, Node node) {
		NodeView nv = new NodeView(x, y, shape, "test", this, node);
		nv.displayNode();
		nodeList.add(nv);
	}

	/**
	 * Remove Node From Canvas
	 * 
	 * @param node
	 *            Node to be removed
	 */
	public void removeNodeView(NodeView node) {
		edited = true;
		boolean remove = false;
		for (NodeView item : nodeList) {
			if (node.nodeData.id.equals(item.nodeData.id)) {
				remove = true;
			}
		}
		if (remove) {
			node.deleteNode();
			nodeList.remove(node);
			draw();
		}
	}

}
