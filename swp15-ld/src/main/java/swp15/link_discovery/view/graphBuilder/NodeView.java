package swp15.link_discovery.view.graphBuilder;

import javafx.scene.canvas.GraphicsContext;

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

	public NodeView(int x, int y, int nodeShape, String label,
			GraphBuildView gbv) {
		this.x = x;
		this.y = y;
		this.nodeShape = nodeShape;
		this.label = label;
		this.gbv = gbv;
	}

	public void displayNode() {
		GraphicsContext gc = gbv.getGraphicsContext2D();
		// gc.clearRect(0, 0, 500, 400);
		switch (nodeShape) {
		case DIAMOND:

			gc.fillRect(x, y, WIDTH, HEIGHT);

		}
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

}
