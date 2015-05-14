package swp15.link_discovery.view.graphBuilder;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class NodeContextMenu extends ContextMenu {
	private GraphBuildView view;

	private MenuItem close;

	private MenuItem linkTo;

	public MenuItem delete;

	private NodeView node;

	public NodeContextMenu(GraphBuildView view, int nodeIndex) {
		this.view = view;
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
			if (!node.nodeData.id.equals("output")) {
				view.removeNodeView(node);
			}

		});
		this.linkTo.setOnAction(e -> {
			view.isLinking = true;
			view.linkNode = this.node;

		});
	}

}
