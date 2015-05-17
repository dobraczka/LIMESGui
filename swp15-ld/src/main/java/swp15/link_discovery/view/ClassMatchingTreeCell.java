package swp15.link_discovery.view;

import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import swp15.link_discovery.model.ClassMatchingNode;

public class ClassMatchingTreeCell extends TreeCell<ClassMatchingNode> {
	@Override
	protected void updateItem(ClassMatchingNode item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setTooltip(null);
		} else {
			setText(item.getName());
			setTooltip(new Tooltip(item.getUri().toString()));
		}
	}
}
