package swp15.link_discovery.view.graphBuilder;

import java.util.List;
import java.util.Vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import swp15.link_discovery.model.metric.Node;

public class NodeView {

	/**
	 * Node-Shape Integer Metric
	 */
	public static final int METRIC = 1; // Metric

	/**
	 * Node-Shape Integer Output
	 */
	public static final int OUTPUT = 2; // Output

	/**
	 * Node-Shape Integer Operator
	 */
	public static final int OPERATOR = 3; // Operator

	/**
	 * Node-Shape Integer Source
	 */
	public static final int SOURCE = 4; // SourceProperty

	/**
	 * Node-Shape Integer Target
	 */
	public static final int TARGET = 5; // TargetProperty

	/**
	 * Width of Nodes
	 */
	public static final int WIDTH = 50;

	/**
	 * Height of Nodes
	 */
	public static final int HEIGHT = 50;
	/**
	 * Image of Add-Node
	 */
	public final Image add = new Image("add.png", 50.0, 50.0, true, true);
	/**
	 * Image of And-Node
	 */
	public final Image and = new Image("and.png", 50.0, 50.0, true, true);
	/**
	 * Image of Diff-Node
	 */
	public final Image diff = new Image("diff.png", 50.0, 50.0, true, true);
	/**
	 * Image of Metric-Node
	 */
	public final Image metric = new Image("Hexagon.png", 50.0, 50.0, true, true);
	/**
	 * Image of Max-Node
	 */
	public final Image max = new Image("max.png", 50.0, 50.0, true, true);
	/**
	 * Image of Min-Node
	 */
	public final Image min = new Image("min.png", 50.0, 50.0, true, true);
	/**
	 * Image of Minus-Node
	 */
	public final Image minus = new Image("minus.png", 50.0, 50.0, true, true);
	/**
	 * Image of Mult-Node
	 */
	public final Image mult = new Image("mult.png", 50.0, 50.0, true, true);
	/**
	 * Image of Or-Node
	 */
	public final Image or = new Image("or.png", 50.0, 50.0, true, true);
	/**
	 * Image of Linkarrow
	 */
	public final Image arrow = new Image("arrow.png", 10.0, 10.0, true, true);

	/**
	 * Position on x-Axis
	 */
	public int x;
	/**
	 * Position on y-Axis
	 */
	public int y;

	/**
	 * Offset to get the Text centered
	 */
	private int xOffset;
	/**
	 * Shape int of Node
	 */
	public int nodeShape;
	/**
	 * Label of Node
	 */
	String label;

	/**
	 * Canvas to draw Node
	 */
	GraphBuildView gbv;

	/**
	 * Node Data Model
	 */
	public Node nodeData;

	/**
	 * Children of Node
	 */
	public List<NodeView> children = new Vector<NodeView>();
	/**
	 * Parent of Node
	 */
	public NodeView parent = null;

	/**
	 * Constructor
	 * 
	 * @param x
	 *            Position on x-Axis
	 * @param y
	 *            Position on y-Axis
	 * @param nodeShape
	 *            Shape int of Node
	 * @param label
	 *            Label to display
	 * @param gbv
	 *            Canvas to Draw Node
	 * @param node
	 *            Node-Datamodel
	 */
	public NodeView(int x, int y, int nodeShape, String label,
			GraphBuildView gbv, Node node) {
		this.x = x;
		this.y = y;
		this.nodeShape = nodeShape;
		this.label = label;
		this.gbv = gbv;
		this.nodeData = node;
		this.xOffset = calculateOffset();
	}

	/**
	 * Draw Node to GraphBuildView
	 */
	public void displayNode() {
		GraphicsContext gc = gbv.getGraphicsContext2D();
		// gc.clearRect(0, 0, 500, 400);
		switch (this.nodeShape) {
		case METRIC:
			gc.drawImage(metric, x, y);
			gc.strokeText(nodeData.id, x + xOffset, y + HEIGHT / 2);
			break;
		case OUTPUT:
			gc.setFill(Color.GREY);
			gc.fillOval(x, y, WIDTH, HEIGHT);
			gc.strokeText(nodeData.id, x + xOffset, y + HEIGHT / 2);
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
			gc.strokeText(nodeData.id, x + xOffset, y + HEIGHT / 2);
			break;
		case TARGET:
			gc.setFill(Color.GREEN);
			gc.fillRect(x, y, WIDTH, HEIGHT);
			gc.strokeText(nodeData.id, x + xOffset, y + HEIGHT / 2);
			break;
		}

	}

	/**
	 * Calculate the offset, to get the text centered
	 */
	public int calculateOffset() {
		Text label = new Text(nodeData.id);
		double labelWidth = label.getLayoutBounds().getWidth();
		return (int) (WIDTH / 2.0 - labelWidth / 2.0);
	}

	/**
	 * Set Position of NodeView on Canvas
	 * 
	 * @param x
	 *            Position on x-Axis
	 * @param y
	 *            Position on y-Axis
	 */
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Proof if Point is in the Node
	 * 
	 * @param x
	 *            Position on x-Axis to proof
	 * @param y
	 *            Position on y-Axis to proof
	 * @return True it Position is in drawed sector
	 */
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

	/**
	 * Adds a Child to the NodeView, and links the Data Models
	 * 
	 * @param child
	 *            Child to add
	 * @return True if successful
	 */
	public boolean addChild(NodeView child) {
		boolean test = nodeData.addChild(child.nodeData);
		if (!test) {
			return false;
		}
		children.add(child);
		child.parent = this;
		return true;
	}

	/**
	 * Adds a Parent to the NodeView, and links the Data Models
	 * 
	 * @param parent
	 *            parent to add
	 * @return True if successful
	 */
	public boolean addParent(NodeView parent) {
		boolean test = parent.nodeData.addChild(nodeData);
		if (!test) {
			return false;
		}
		parent.children.add(this);
		this.parent = parent;
		return true;

	}

	/**
	 * Adds a Child without Linking the Data Models
	 * 
	 * @param child
	 *            Child to Added
	 */
	public void addChildWithOutDataLinking(NodeView child) {
		children.add(child);
		child.parent = this;
		child.nodeData.overwriteParent(this.nodeData);
	}

	/**
	 * Draw the Links on Canvas to the Childs
	 */
	public void drawLink() {
		GraphicsContext gc = gbv.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		children.forEach(nodeView -> {
			int x1 = x + NodeView.WIDTH / 2;
			int y1 = y + NodeView.HEIGHT / 2;
			int x2 = nodeView.x + NodeView.WIDTH / 2;
			int y2 = nodeView.y + NodeView.HEIGHT / 2;
			gc.strokeLine(x1, y1, x2, y2);

			double linkMidX = (x1 + x2) / 2.0;
			double linkMidY = (y1 + y2) / 2.0;
			double rotate = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1)) + 225;
			gc.setTransform(new Affine(new Rotate(rotate, linkMidX, linkMidY)));
			double arrowX = linkMidX - (arrow.getWidth() * 3 / 4);
			double arrowY = linkMidY - (arrow.getWidth() / 4);
			gc.drawImage(arrow, arrowX, arrowY);
			gc.setTransform(new Affine());
		});
	}

	/**
	 * Delete the Node unlink Children and Parent in Data and View Model
	 */
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
}
