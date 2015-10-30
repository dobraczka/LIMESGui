package swp15.link_discovery.view;

import static swp15.link_discovery.util.SourceOrTarget.SOURCE;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import swp15.link_discovery.controller.EditPropertyMatchingController;
import swp15.link_discovery.model.PropertyPair;
import swp15.link_discovery.util.SourceOrTarget;

/**
 * View class for edit properties step in create wizard
 * 
 * @author Manuel Jacob
 */
public class EditPropertyMatchingView implements IEditView {
	private EditPropertyMatchingController controller;
	private ScrollPane rootPane;
	private TableView<PropertyPair> table;
	private ObservableList<String> sourceProperties = FXCollections
			.observableArrayList();
	private ObservableList<String> targetProperties = FXCollections
			.observableArrayList();

	EditPropertyMatchingView() {
		createRootPane();
	}

	/**
	 * Sets the corresponding Controller
	 * 
	 * @param controller
	 */
	public void setController(EditPropertyMatchingController controller) {
		this.controller = controller;
	}

	/**
	 * Creates the rootPane
	 */
	private void createRootPane() {
		sourceProperties.addListener(new ListChangeListener() {
			@Override
			public void onChanged(ListChangeListener.Change change) {
				if (sourceProperties.size() != 0
						&& targetProperties.size() != 0) {
					putAllPropertiesToTable();
				}
			}
		});

		targetProperties.addListener(new ListChangeListener() {
			@Override
			public void onChanged(ListChangeListener.Change change) {
				if (sourceProperties.size() != 0
						&& targetProperties.size() != 0) {
					putAllPropertiesToTable();
				}
			}
		});
		table = new TableView<PropertyPair>();
		// table.setEditable(true);

		TableColumn<PropertyPair, String> columnSource = new TableColumn<PropertyPair, String>(
				"Source Property");
		// columnSource.setCellFactory(ChoiceBoxTableCell.forTableColumn(sourceProperties));
		columnSource
				.setCellValueFactory(new PropertyValueFactory<PropertyPair, String>(
						"sourceProperty"));
		columnSource.prefWidthProperty().bind(table.widthProperty().divide(2));
		table.getColumns().add(columnSource);

		TableColumn<PropertyPair, String> columnTarget = new TableColumn<PropertyPair, String>(
				"Target Property");
		// columnTarget.setCellFactory(ChoiceBoxTableCell.forTableColumn(targetProperties));
		columnTarget
				.setCellValueFactory(new PropertyValueFactory<PropertyPair, String>(
						"targetProperty"));
		columnTarget.prefWidthProperty().bind(table.widthProperty().divide(2));
		table.getColumns().add(columnTarget);

		ButtonBar buttonBar = new ButtonBar();
		Button buttonAdd = new Button("Add");
		buttonAdd.setDisable(true);
		buttonAdd
				.setOnAction((e) -> {
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

					ObservableList<String> missingSourceProperties = getMissingSourceProperties();
					ObservableList<String> missingTargetProperties = getMissingTargetProperties();

					pane.add(new Label("Source Property"), 0, 0);
					ChoiceBox<String> sourceProperty = new ChoiceBox<String>(
							missingSourceProperties);
					pane.add(sourceProperty, 1, 0);

					pane.add(new Label("Target Property"), 0, 1);
					ChoiceBox<String> targetProperty = new ChoiceBox<String>(
							missingTargetProperties);
					pane.add(targetProperty, 1, 1);

					Dialog<Void> dialog = new Dialog<Void>();
					dialog.getDialogPane().setContent(pane);
					dialog.getDialogPane().getButtonTypes()
							.addAll(ButtonType.OK, ButtonType.CANCEL);
					if (dialog.showAndWait().isPresent()) {
						table.getItems().add(
								new PropertyPair(sourceProperty
										.getSelectionModel().getSelectedItem(),
										targetProperty.getSelectionModel()
												.getSelectedItem()));
					}
					if (table.getItems().size() == sourceProperties.size()) {
						buttonAdd.setDisable(true);
					}
				});
		buttonBar.getButtons().add(buttonAdd);
		Button buttonRemove = new Button("Remove");
		buttonRemove.disableProperty().bind(
				table.getSelectionModel().selectedItemProperty().isNull());
		buttonRemove.setOnAction((e) -> {
			table.getItems().remove(
					table.getSelectionModel().getSelectedIndex());
			buttonAdd.setDisable(false);
		});
		buttonBar.getButtons().add(buttonRemove);

		BorderPane pane = new BorderPane();
		pane.setCenter(table);
		pane.setBottom(buttonBar);

		rootPane = new ScrollPane(pane);
		rootPane.setFitToHeight(true);
		rootPane.setFitToWidth(true);
	}

	/**
	 * Returns the rootPane
	 * 
	 * @return rootPane
	 */
	@Override
	public Parent getPane() {
		return rootPane;
	}

	/**
	 * Saves the table items
	 */
	@Override
	public void save() {
		controller.save(table.getItems());
	}

	/**
	 * Shows the available properties
	 * 
	 * @param sourceOrTarget
	 * @param properties
	 */
	public void showAvailableProperties(SourceOrTarget sourceOrTarget,
			List<String> properties) {
		(sourceOrTarget == SOURCE ? sourceProperties : targetProperties)
				.setAll(properties);
	}

	/**
	 * Puts the loaded properties to the table
	 */
	private void putAllPropertiesToTable() {
		int i = 0;
		while (i < this.sourceProperties.size()) {
			table.getItems().add(
					new PropertyPair(this.sourceProperties.get(i),
							this.targetProperties.get(i)));
			i++;
		}
	}

	/**
	 * Returns the Source properties, that are not already in the table
	 * 
	 * @return missingSourceProperties
	 */
	private ObservableList<String> getMissingSourceProperties() {
		ObservableList<String> missingSourceProperties = FXCollections
				.observableArrayList();
		for (String prop : sourceProperties) {
			boolean alreadyContained = false;
			for (int i = 0; i < table.getItems().size(); i++) {
				if (table.getItems().get(i).getSourceProperty() == prop) {
					alreadyContained = true;
				}
			}
			if (!alreadyContained) {
				missingSourceProperties.add(prop);
			}
		}
		return missingSourceProperties;
	}

	/**
	 * Returns the Target properties, that are not already in the table 
	 * @return missingTargetProperties
	 */
	private ObservableList<String> getMissingTargetProperties() {
		ObservableList<String> missingTargetProperties = FXCollections
				.observableArrayList();
		for (String prop : targetProperties) {
			boolean alreadyContained = false;
			for (int i = 0; i < table.getItems().size(); i++) {
				if (table.getItems().get(i).getTargetProperty() == prop) {
					alreadyContained = true;
				}
			}
			if (!alreadyContained) {
				missingTargetProperties.add(prop);
			}
		}
		return missingTargetProperties;
	}

	/**
	 * Shows an error if something went wrong
	 * @param header
	 * @param content
	 */
	public void showError(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
