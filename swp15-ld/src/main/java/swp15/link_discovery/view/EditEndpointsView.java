package swp15.link_discovery.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import swp15.link_discovery.controller.EditEndpointsController;

public class EditEndpointsView implements IEditView {
	private EditEndpointsController controller;
	private TextField[] sourceFields;
	private TextField[] targetFields;
	private ScrollPane rootPane;

	public EditEndpointsView() {
		createRootPane();
	}

	public void setController(EditEndpointsController controller) {
		this.controller = controller;
	}

	private void createRootPane() {
		HBox hbox = new HBox();
		Node sourcePanelWithTitle = createEndpointPane(true);
		HBox.setHgrow(sourcePanelWithTitle, Priority.ALWAYS);
		hbox.getChildren().add(sourcePanelWithTitle);
		Node targetPaneWithTitle = createEndpointPane(false);
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

	private Node createEndpointPane(boolean source) {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(25, 25, 25, 25));
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setMinWidth(Control.USE_PREF_SIZE);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setMinWidth(300);
		column2.setHgrow(Priority.ALWAYS);
		pane.getColumnConstraints().addAll(column1, column2);

		pane.add(new Label("Endpoint URL"), 0, 0);
		TextField endpointURL = new TextField();
		pane.add(endpointURL, 1, 0);

		pane.add(new Label("ID / Namespace"), 0, 1);
		TextField idNamespace = new TextField();
		pane.add(idNamespace, 1, 1);

		pane.add(new Label("Graph"), 0, 2);
		TextField graph = new TextField();
		pane.add(graph, 1, 2);

		pane.add(new Label("Page size"), 0, 3);
		TextField pageSize = new TextField();
		pane.add(pageSize, 1, 3);

		TextField[] textFields = new TextField[] { endpointURL, idNamespace,
				graph, pageSize };
		if (source) {
			sourceFields = textFields;
			return new TitledPane("Source endpoint", pane);
		} else {
			targetFields = textFields;
			return new TitledPane("Target endpoint", pane);
		}
	}

	public void setFields(boolean source, String endpoint, String idNamespace,
			String graph, String pageSize) {
		TextField[] textFields = source ? sourceFields : targetFields;
		textFields[0].setText(endpoint);
		textFields[1].setText(idNamespace);
		textFields[2].setText(graph);
		textFields[3].setText(pageSize);
	}

	@Override
	public void save() {
		controller.save(true, sourceFields[0].getText(),
				sourceFields[1].getText(), sourceFields[2].getText(),
				sourceFields[3].getText());
		controller.save(false, targetFields[0].getText(),
				targetFields[1].getText(), targetFields[2].getText(),
				targetFields[3].getText());
	}
}
