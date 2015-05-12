package swp15.link_discovery.view.graphBuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import swp15.link_discovery.model.metric.Node;
import swp15.link_discovery.model.metric.Output;

public class GraphBuildView extends Canvas {

	public ObservableList<NodeView> nodeList;

	private boolean nodeClicked;

	private int nodeIndex;

	private int index;

	private NodeContextMenu contextMenu;

	public boolean isLinking;

	public NodeView linkNode;

	private double[] mousePosition = { 0, 0 };

	public GraphBuildView() {
		this.setWidth(600);
		this.setHeight(600);
		this.nodeList = FXCollections.observableArrayList();
		this.nodeClicked = false;
		this.isLinking = false;
		addNode(300, 300, 2, new Output());
	}

	public void start() {
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (isLinking) {
				for (NodeView node : nodeList) {
					if (node.contains((int) e.getX(), (int) e.getY())) {
						isLinking = false;
						if (node.addChild(linkNode)) {
						} else if (linkNode.addChild(node)) {
						}
						draw();
						break;
					}
				}
			}
		});
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			index = 0;
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
				index = 0;
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
					contextMenu.show(this, e.getX() + 500, e.getY());
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

	public void draw() {
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.clearRect(0, 0, 600, 600);
		if (isLinking) {
			gc.strokeLine(linkNode.x + NodeView.WIDTH / 2, linkNode.y
					+ NodeView.HEIGHT / 2, mousePosition[0], mousePosition[1]);
		}
		nodeList.forEach(e -> {
			e.drawLink();
			;
		});
		nodeList.forEach(e -> {
			e.displayNode();
		});
	}

	public NodeView addNode(int x, int y, int shape, Node node) {
		NodeView nv = new NodeView(x, y, shape, "test", this, node);
		nv.displayNode();
		nodeList.add(nv);
		return nv;
	}

	public void removeNodeView(NodeView node) {
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
