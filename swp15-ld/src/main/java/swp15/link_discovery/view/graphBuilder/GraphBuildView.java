package swp15.link_discovery.view.graphBuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import swp15.link_discovery.model.metric.Node;
import swp15.link_discovery.model.metric.Output;

public class GraphBuildView extends Canvas {

	private ObservableList<NodeView> nodeList;

	private boolean nodeClicked;

	private int nodeIndex;

	private int index;

	public GraphBuildView() {
		this.setWidth(600);
		this.setHeight(600);
		this.nodeList = FXCollections.observableArrayList();
		this.nodeClicked = false;
		nodeList.add(addNode(300, 300, 2, new Output()));

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
		draw();
	}

	public void draw() {
		this.getGraphicsContext2D().clearRect(0, 0, 600, 600);
		nodeList.forEach(e -> {
			e.displayNode();
		});
		// this.addEventHandler(MouseEvent.MOUSE_CLICKED,
		// new EventHandler<MouseEvent>() {
		// @Override
		// public void handle(MouseEvent e) {
		//
		// }
		// });
		//
		// this.addEventHandler(MouseEvent.MOUSE_DRAGGED,
		// new EventHandler<MouseEvent>() {
		// @Override
		// public void handle(MouseEvent e) {
		// addNode((int) e.getX() - 25, (int) e.getY() - 25);
		// }
		// });

	}

	public NodeView addNode(int x, int y, int shape, Node node) {
		NodeView nv = new NodeView(x, y, shape, "test", this, node);
		nv.displayNode();
		nodeList.add(nv);
		return nv;
	}

}
