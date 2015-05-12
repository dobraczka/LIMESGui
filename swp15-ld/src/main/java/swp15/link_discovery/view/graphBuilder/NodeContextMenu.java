package swp15.link_discovery.view.graphBuilder;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class NodeContextMenu extends ContextMenu {
	private int nodeIndex;
	private GraphBuildView view;

	private MenuItem close;

	private MenuItem linkTo;

	public MenuItem delete;

	private NodeView node;

	public NodeContextMenu(GraphBuildView view, int nodeIndex) {
		this.view = view;
		this.nodeIndex = nodeIndex;
		this.linkTo = new MenuItem("Link To");
		this.delete = new MenuItem("Delete");
		this.close = new MenuItem("Close");
		addListeners();
		this.getItems().addAll(linkTo, delete, close);
		this.node = view.nodeList.get(nodeIndex);
	}

	public NodeContextMenu(GraphBuildView view, NodeView node) {
		this.view = view;
		this.node = node;
		this.linkTo = new MenuItem("Link To");
		this.delete = new MenuItem("Delete");
		this.close = new MenuItem("Close");
		addListeners();
		this.getItems().addAll(linkTo, delete, close);
	}

	private void addListeners() {

		this.delete.setOnAction(e -> {
			System.out.println("called");
			view.removeNodeView(node);

		});

	}

}
