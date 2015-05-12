package swp15.link_discovery.view.graphBuilder;

import java.util.List;
import java.util.Vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import swp15.link_discovery.model.metric.Node;

public class NodeView {

	public static final int DIAMOND = 1;
	public static final int ELLIPSE = 2;
	public static final int HEXAGON = 3;
	public static final int RECTANGLE = 4;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	int x, y, nodeShape;
	String label;
	GraphBuildView gbv;

	public Node nodeData;

	public List<NodeView> children = new Vector<NodeView>();

	public NodeView parent = null;

	public NodeView(int x, int y, int nodeShape, String label,
			GraphBuildView gbv, Node node) {
		this.x = x;
		this.y = y;
		this.nodeShape = nodeShape;
		this.label = label;
		this.gbv = gbv;
		this.nodeData = node;
	}

	public void displayNode() {
		GraphicsContext gc = gbv.getGraphicsContext2D();
		// gc.clearRect(0, 0, 500, 400);
		switch (nodeShape) {
		case DIAMOND:
			gc.setFill(Color.BLUE);
			gc.fillRect(x, y, WIDTH, HEIGHT);
			gc.strokeText(nodeData.id, x, y);
		}

	}

	public void moveNode(int x, int y) {
		this.x += x;
		this.y += y;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean contains(int x, int y) {
		int minX = this.x;
		int maxX = this.x + NodeView.WIDTH;
		int minY = this.y;
		int maxY = this.y + NodeView.HEIGHT;
		if (x >= minX && x <= maxX) {
			if (y >= minY && y <= maxY) {
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

	public boolean addChild(NodeView child) {
		boolean test = nodeData.addChild(child.nodeData);
		if (!test) {
			return false;
		}
		children.add(child);
		child.parent = this;
		return true;
	}

	public void drawLink() {
		GraphicsContext gc = gbv.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		children.forEach(nodeView -> {
			gc.strokeLine(x + NodeView.WIDTH / 2, y + NodeView.HEIGHT / 2,
					nodeView.x + NodeView.WIDTH / 2, nodeView.y
							+ NodeView.HEIGHT / 2);
		});
	}

	public void deleteNode() {
		if (parent != null) {
			parent.children.forEach(e -> {
				if (e.nodeData.id.equals(this.nodeData.id)) {
					parent.children.remove(e);
					e.nodeData.removeChild(this.nodeData);
				}
			});
		}
		children.forEach(e -> {
			e.parent = null;
		});
	}

}
