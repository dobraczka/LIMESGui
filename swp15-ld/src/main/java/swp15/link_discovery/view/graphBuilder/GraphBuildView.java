package swp15.link_discovery.view.graphBuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
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

	public GraphBuildView() {
		this.setWidth(600);
		this.setHeight(600);
		this.nodeList = FXCollections.observableArrayList();
		this.nodeClicked = false;
		addNode(300, 300, 2, new Output());
		addNode(400, 400, 3, Node.createNode("add"));
		addNode(400, 400, 1, Node.createNode("overlap"));
		nodeList.get(0).addChild(nodeList.get(2));
	}

	public void start() {
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
								(int) e.getX()
										- (nodeList.get(nodeIndex).WIDTH) / 2,
								(int) e.getY()
										- (nodeList.get(nodeIndex).HEIGHT) / 2);
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
		draw();
	}

	public void draw() {
		this.getGraphicsContext2D().clearRect(0, 0, 600, 600);
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
