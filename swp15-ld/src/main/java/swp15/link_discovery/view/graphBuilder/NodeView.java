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
			ArrowHelper helper = new ArrowHelper(x, y, nodeView.x, nodeView.y);
			gc.strokeLine(x + NodeView.WIDTH / 2, y + NodeView.HEIGHT / 2,
					nodeView.x + NodeView.WIDTH / 2, nodeView.y
							+ NodeView.HEIGHT / 2);
			// gc.fillRect(helper.middleX, helper.middleY, 10, 10);
			// gc.strokeLine(helper.middleX, helper.middleY, helper.leftX,
			// helper.leftY);
			// gc.strokeLine(helper.middleX, helper.middleY, helper.rightX,
			// helper.rightY);
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

	private class ArrowHelper {
		int parentX;
		int parentY;
		int childX;
		int childY;
		public double leftX;
		public double leftY;
		public double rightX;
		public double rightY;
		int meanDisX;
		int meanDisY;
		public int middleX, middleY;

		public ArrowHelper(int parentX, int parentY, int childX, int childY) {
			this.parentX = parentX;
			this.parentY = parentY;
			this.childX = childX;
			this.childY = childY;
			this.middleX = x
					+ NodeView.WIDTH
					/ 2
					+ ((childX + NodeView.WIDTH / 2) - (parentX + NodeView.WIDTH / 2))
					/ 2;
			this.middleY = y
					+ NodeView.HEIGHT
					/ 2
					+ ((childY + NodeView.WIDTH / 2) - (parentY + NodeView.WIDTH / 2))
					/ 2;
			this.meanDisX = ((childX + NodeView.WIDTH / 2) - (parentX + NodeView.WIDTH / 2)) / 2;
			this.meanDisY = ((childY + NodeView.WIDTH / 2) - (parentY + NodeView.WIDTH / 2)) / 2;
			setLeft();
			setRight();
		}

		private void setLeft() {
			double l = Math.sqrt(Math.pow(childX - parentX, 2)
					+ Math.pow(childY - parentY, 2));
			leftX = middleX
					+ 20
					/ l
					* (Math.cos(Math.PI / 2) * (childX - parentX) - Math
							.sin(Math.PI / 2) * (childY - parentY));
			leftY = middleY
					+ 20
					/ l
					* (Math.sin(Math.PI / 2) * (childY - parentY) + Math
							.cos(Math.PI / 2) * (childX - parentX));

		}

		private void setRight() {
			double l = Math.sqrt(Math.pow(childX - parentX, 2)
					+ Math.pow(childY - parentY, 2));
			rightX = middleX
					+ 20
					/ l
					* (Math.cos(5 * Math.PI / 2) * (childX - parentX) - Math
							.sin(5 * Math.PI / 2) * (childY - parentY));
			rightY = middleY
					+ 20
					/ l
					* (Math.sin(5 * Math.PI / 2) * (childY - parentY) + Math
							.cos(5 * Math.PI / 2) * (childX - parentX));

		}

	}
}