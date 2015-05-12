package swp15.link_discovery.view.graphBuilder;

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

}
