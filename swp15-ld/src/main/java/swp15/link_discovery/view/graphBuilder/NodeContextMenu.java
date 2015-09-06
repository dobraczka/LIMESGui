package swp15.link_discovery.view.graphBuilder;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class NodeContextMenu extends ContextMenu {
	/**
	 * Corresponding View
	 */
	private GraphBuildView graphBuildView;

	/**
	 * Close MenuItem
	 */
	private MenuItem close;

	/**
	 * MenuItem to start Linking
	 */
	private MenuItem linkTo;

	/**
	 * MenutItem to delete Node
	 */
	private MenuItem delete;

	/**
	 * Clicked NodeView
	 */
	private NodeView node;

	/**
	 * Constructor
	 * 
	 * @param view
	 *            Corresponding view
	 * @param nodeIndex
	 *            index in GraphBuildView.NodeViewList of clicked Node
	 */
	public NodeContextMenu(GraphBuildView view, NodeView clickedNode) {
		this.graphBuildView = view;
		this.linkTo = new MenuItem("Link To");
		this.delete = new MenuItem("Delete");
		this.close = new MenuItem("Close");
		addListeners();
		this.getItems().addAll(linkTo, delete, close);
		this.node = clickedNode;
	}

	/**
	 * Add Listeners to the MenuItems
	 */
	private void addListeners() {

		this.delete.setOnAction(e -> {
			if (!node.nodeData.id.equals("output")) {
				graphBuildView.removeNodeView(node);
			}

		});
		this.linkTo.setOnAction(e -> {
			graphBuildView.isLinking = true;
			graphBuildView.linkNode = this.node;

		});
	}

}
