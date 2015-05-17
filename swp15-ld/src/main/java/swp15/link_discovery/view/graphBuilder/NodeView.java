package swp15.link_discovery.view.graphBuilder;

import java.util.List;
import java.util.Vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import swp15.link_discovery.model.metric.Node;

public class NodeView {

	public static final int METRIC = 1; // Metric
	public static final int OUTPUT = 2; // Output
	public static final int OPERATOR = 3; // Operator
	public static final int SOURCE = 4; // SourceProperty
	public static final int TARGET = 5; // TargetProperty
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public final Image add = new Image("add.png", 50.0, 50.0, true, true);
	public final Image and = new Image("and.png", 50.0, 50.0, true, true);
	public final Image diff = new Image("diff.png", 50.0, 50.0, true, true);
	public final Image metric = new Image("Hexagon.png", 50.0, 50.0, true, true);
	public final Image max = new Image("max.png", 50.0, 50.0, true, true);
	public final Image min = new Image("min.png", 50.0, 50.0, true, true);
	public final Image minus = new Image("minus.png", 50.0, 50.0, true, true);
	public final Image mult = new Image("mult.png", 50.0, 50.0, true, true);
	public final Image or = new Image("or.png", 50.0, 50.0, true, true);
	public final Image arrow = new Image("arrow.png", 10.0, 10.0, true, true);

	public int x, y, nodeShape;
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
		switch (this.nodeShape) {
		case METRIC:
			gc.drawImage(metric, x, y);
			gc.strokeText(nodeData.id, x, y + HEIGHT / 2);
			break;
		case OUTPUT:
			gc.setFill(Color.GREY);
			gc.fillOval(x, y, WIDTH, HEIGHT);
			gc.strokeText(nodeData.id, x, y + HEIGHT / 2);
			gc.strokeText("Acceptance Threshold: " + nodeData.param1, x - 50,
					(y + HEIGHT / 2) - 20);
			gc.strokeText("Verification Threshold: " + nodeData.param2, x - 50,
					(y + HEIGHT / 2) - 40);
			break;
		case OPERATOR:
			switch (nodeData.id) {
			case "add":
				gc.drawImage(add, x, y);
				break;
			case "and":
				gc.drawImage(and, x, y);
				break;
			case "diff":
				gc.drawImage(diff, x, y);
				break;
			case "max":
				gc.drawImage(max, x, y);
				break;
			case "min":
				gc.drawImage(min, x, y);
				break;
			case "minus":
				gc.drawImage(minus, x, y);
				break;
			case "mult":
				gc.drawImage(mult, x, y);
				break;
			case "or":
				gc.drawImage(or, x, y);
				break;
			}
			break;
		case SOURCE:
			gc.setFill(Color.RED);
			gc.fillRect(x, y, WIDTH, HEIGHT);
			gc.strokeText(nodeData.id, x, y + HEIGHT / 2);
			break;
		case TARGET:
			gc.setFill(Color.GREEN);
			gc.fillRect(x, y, WIDTH, HEIGHT);
			gc.strokeText(nodeData.id, x, y + HEIGHT / 2);
			break;
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

	public void addChildWithOutDataLinking(NodeView child) {
		children.add(child);
		child.parent = this;
		child.nodeData.overwriteParent(this.nodeData);
	}

	public void drawLink() {
		GraphicsContext gc = gbv.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		children.forEach(nodeView -> {
			int middleX = x
					+ NodeView.WIDTH
					/ 2
					+ ((nodeView.x + NodeView.WIDTH / 2) - (this.x + NodeView.WIDTH / 2))
					/ 2;
			int middleY = y
					+ NodeView.HEIGHT
					/ 2
					+ ((nodeView.y + NodeView.WIDTH / 2) - (this.y + NodeView.WIDTH / 2))
					/ 2;
			gc.strokeLine(x + NodeView.WIDTH / 2, y + NodeView.HEIGHT / 2,
					nodeView.x + NodeView.WIDTH / 2, nodeView.y
							+ NodeView.HEIGHT / 2);
			gc.save();
			double deg = ArrowHelper.getAngle(nodeView.x, nodeView.y, this.x,
					this.y) + 180;
			gc.rotate(deg);
			int[] rot = ArrowHelper.rotate(middleX, middleY, -deg);
			gc.drawImage(arrow, rot[0], rot[1]);
			gc.restore();
		});
	}

	public void deleteNode() {
		if (parent != null) {
			parent.nodeData.removeChild(this.nodeData);
			parent.children.remove(this);

		}
		children.forEach(e -> {
			e.nodeData.removeParent();
			e.parent = null;
		});
		this.parent = null;
		this.nodeData.removeParent();
	}

	private static class ArrowHelper {
		public static int[] rotate(int x, int y, double degree) {
			int[] res = { 0, 0 };
			double angle = Math.toRadians(degree);
			res[0] = (int) ((x - 7) * Math.cos(angle) - y * Math.sin(angle));
			res[1] = (int) ((x - 7) * Math.sin(angle) + y * Math.cos(angle));
			return res;

		}

		public static double getAngle(int startX, int startY, int endX, int endY) {
			double res;
			int degoff = 0;
			int x = endX - startX;
			int y = endY - startY;
			if (x > 0) {
				degoff = 180;
			}
			if (x == 0) {
				return -225;
			}
			res = Math.toDegrees(Math.atan(y / x));
			return res - degoff + 270 - 45;
		}
	}
}
