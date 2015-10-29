package swp15.link_discovery.view.graphBuilder;

import java.util.ArrayList;
import java.util.Collections;

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
import swp15.link_discovery.view.ToolBox;

public class GraphBuildView extends Canvas {

	/**
	 * Corresponding GraphBuildController
	 */
	public GraphBuildController graphBuildController;

	/**
	 * List of nodes in the Canvas
	 */
	public ArrayList<NodeView> nodeList;

	/**
	 * Reversed List of nodes in Canvas to correctly display dragging
	 */
	public ArrayList<NodeView> reversedNodeList;

	/**
	 * True if Node was clicked
	 */
	private boolean nodeClicked;

	/**
	 * Index of clicked Node
	 */
	private NodeView clickedNode;

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
	 * Boolean to check if another ContextMenu is open
	 */
	public boolean contextMenuIsShown = false;

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
	public GraphBuildView(Config currentConfig, ToolBox toolbox) {
		widthProperty().addListener(evt -> draw());
		heightProperty().addListener(evt -> draw());
		this.nodeList = new ArrayList<NodeView>();
		this.reversedNodeList = nodeList;
		Collections.reverse(reversedNodeList);
		this.nodeClicked = false;
		this.isLinking = false;
		addNode(300, 300, 2, new Output());
		this.graphBuildController = new GraphBuildController(currentConfig,
				this, toolbox);
	}

	/**
	 * Constructor
	 */
	public GraphBuildView(ToolBox toolbox) {
		widthProperty().addListener(evt -> draw());
		heightProperty().addListener(evt -> draw());
		this.nodeList = new ArrayList<NodeView>();
		this.reversedNodeList = nodeList;
		Collections.reverse(reversedNodeList);
		this.nodeClicked = false;
		this.isLinking = false;
		addNode(300, 300, 2, new Output());
		this.graphBuildController = new GraphBuildController(this, toolbox);
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
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					int index = 0;
					// boolean clickedOutput = false;
				for (NodeView node : nodeList) {
					if (node.contains((int) e.getX(), (int) e.getY())) {
						clickedNode = nodeList.get(index);
						break;
					}
					index++;
				}
				if (clickedNode.nodeShape == NodeView.OPERATOR
						|| clickedNode.nodeShape == NodeView.OUTPUT) {
					ThresholdModifyView tmv = new ThresholdModifyView(this,
							clickedNode);
					// edited = true;
			}
		}
	}
}		);

		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			int index = 0;
			for (NodeView node : reversedNodeList) {
				if (node.contains((int) e.getX(), (int) e.getY())) {
					nodeClicked = true;
					clickedNode = reversedNodeList.get(index);
					this.nodeList.remove(clickedNode);
					this.nodeList.add(clickedNode);
					this.reversedNodeList = this.nodeList;
					break;
				}
				index++;
			}

		});
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {

			if (nodeClicked) {
				clickedNode.setXY(
						(int) e.getX() - (clickedNode.getWidth()) / 2,
						(int) e.getY() - (clickedNode.getHeight()) / 2);

				draw();
			}

		});
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
			nodeClicked = false;
		});
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				int index = 0;
				boolean NodeClickedBySecondary = false;
				for (NodeView node : nodeList) {
					if (node.contains((int) e.getX(), (int) e.getY())) {
						clickedNode = nodeList.get(index);
						NodeClickedBySecondary = true;
						break;
					}
					index++;
				}
				if (NodeClickedBySecondary) {
					if (!contextMenuIsShown) {
						contextMenu = new NodeContextMenu(this, clickedNode);
						contextMenu.show(this, e.getScreenX(), e.getScreenY());
					}
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
			gc.strokeLine(linkNode.x + linkNode.getWidth() / 2, linkNode.y
					+ linkNode.getHeight() / 2, mousePosition[0],
					mousePosition[1]);
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
		int[] xy = findFreePlace(x, y);
		if (xy != null) {
			int new_x = xy[0];
			int new_y = xy[1];
			NodeView nv = new NodeView(new_x, new_y, shape, "test", this, node);
			nv.displayNode();
			nodeList.add(nv);
			this.reversedNodeList = nodeList;
			Collections.reverse(reversedNodeList);
		}
	}

	/**
	 * Calculates a place for the new node, which is not already taken
	 * 
	 * @param x
	 *            Proposed position on x-Axis
	 * @param y
	 *            Proposed position on y-Axis
	 * @return int[] containing a free coordinate
	 */
	private int[] findFreePlace(int x, int y) {
		for (int i = 0; i < nodeList.size(); i++) {
			if (nodeList.get(i).x == x || nodeList.get(i).y == y) {
				x += 10;
				y += 10;
				i = -1; // Because it needs to start from 0 and the for-loop
						// does i++
				if (x >= (int) this.widthProperty().doubleValue()
						|| y >= (int) this.heightProperty().doubleValue()) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("Cannot add more nodes! Please move or delete some nodes!");
					alert.showAndWait();
					return null;
				}
			}
		}
		return new int[] { x, y };
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
		this.reversedNodeList = nodeList;
		Collections.reverse(reversedNodeList);
		if (remove) {
			node.deleteNode();
			nodeList.remove(node);
			draw();
		}
	}
}
