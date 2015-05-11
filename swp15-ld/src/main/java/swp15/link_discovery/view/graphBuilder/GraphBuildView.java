package swp15.link_discovery.view.graphBuilder;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

public class GraphBuildView extends Canvas {

	public GraphBuildView() {
		this.setWidth(600);
		this.setHeight(600);

	}

	public void draw() {

		this.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {

					}
				});

		this.addEventHandler(MouseEvent.MOUSE_DRAGGED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						addNode((int) e.getX() - 25, (int) e.getY() - 25);
					}
				});

	}

	public NodeView addNode(int x, int y) {
		NodeView nv = new NodeView(x, y, 1, "test", this);
		nv.displayNode();
		return nv;
	}
}
