package swp15.link_discovery.view;

import static swp15.link_discovery.util.SourceOrTarget.SOURCE;
import static swp15.link_discovery.util.SourceOrTarget.TARGET;

import java.util.List;
import java.util.function.Predicate;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import swp15.link_discovery.controller.EditClassMatchingController;
import swp15.link_discovery.model.ClassMatchingNode;
import swp15.link_discovery.util.SourceOrTarget;

public class EditClassMatchingView extends Application implements IEditView {
	private EditClassMatchingController controller;
	private ScrollPane rootPane;
	private TreeView<ClassMatchingNode> sourceTreeView;
	private TreeView<ClassMatchingNode> targetTreeView;

	EditClassMatchingView() {
		createRootPane();
	}

	public void setController(EditClassMatchingController controller) {
		this.controller = controller;
	}

	private void createRootPane() {
		HBox hbox = new HBox();
		Node sourcePanelWithTitle = createClassMatchingPane(SOURCE);
		HBox.setHgrow(sourcePanelWithTitle, Priority.ALWAYS);
		hbox.getChildren().add(sourcePanelWithTitle);
		Node targetPaneWithTitle = createClassMatchingPane(TARGET);
		HBox.setHgrow(targetPaneWithTitle, Priority.ALWAYS);
		hbox.getChildren().add(targetPaneWithTitle);

		rootPane = new ScrollPane(hbox);
		rootPane.setFitToHeight(true);
		rootPane.setFitToWidth(true);
	}

	@Override
	public Parent getPane() {
		return rootPane;
	}

	private Node createClassMatchingPane(SourceOrTarget sourceOrTarget) {
		BorderPane pane = new BorderPane();

		TreeView<ClassMatchingNode> treeView = new TreeView<ClassMatchingNode>();
		treeView.setCellFactory(tv -> new ClassMatchingTreeCell());
		pane.setCenter(treeView);

		if (sourceOrTarget == SOURCE) {
			sourceTreeView = treeView;
			return new TitledPane("Source class", pane);
		} else {
			targetTreeView = treeView;
			return new TitledPane("Target class", pane);
		}
	}

	private void addTreeChildren(TreeItem<ClassMatchingNode> parent,
			List<ClassMatchingNode> childNodes,
			Predicate<ClassMatchingNode> shouldSelect) {
		for (ClassMatchingNode childNode : childNodes) {
			TreeItem<ClassMatchingNode> child = new TreeItem<ClassMatchingNode>(
					childNode);
			parent.getChildren().add(child);
			addTreeChildren(child, childNode.getChildren(), shouldSelect);
		}
	}

	public void showTree(SourceOrTarget sourceOrTarget,
			List<ClassMatchingNode> items, ClassMatchingNode currentClass) {
		TreeItem<ClassMatchingNode> root = new TreeItem<ClassMatchingNode>();
		addTreeChildren(root, items, node -> node == currentClass);
		TreeView<ClassMatchingNode> treeView = sourceOrTarget == SOURCE ? sourceTreeView
				: targetTreeView;
		treeView.setShowRoot(false);
		treeView.setRoot(root);
	}

	@Override
	public void save() {
		TreeItem<ClassMatchingNode> selectedSourceClass = sourceTreeView
				.getSelectionModel().getSelectedItem();
		TreeItem<ClassMatchingNode> selectedTargetClass = targetTreeView
				.getSelectionModel().getSelectedItem();
		controller.save(
				selectedSourceClass == null ? null : selectedSourceClass
						.getValue(),
				selectedTargetClass == null ? null : selectedTargetClass
						.getValue());
	}

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(new EditClassMatchingView().getPane(), 800, 600);
		stage.setTitle("LIMES");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}
