package swp15.link_discovery.view;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import swp15.link_discovery.controller.ActiveLearningResultController;
import swp15.link_discovery.model.ActiveLearningResult;
import swp15.link_discovery.model.InstanceProperty;
import swp15.link_discovery.model.Result;

/**
 * Extends the original result view by adding the possibility to review matches
 * and continue the learning process
 * 
 * @author Felix Brei
 *
 */
public class ActiveLearningResultView extends ResultView {

	/**
	 * Table that contains the results of the learning process, extended by an
	 * 'is match'-flag
	 */
	private TableView<ActiveLearningResult> table;

	/**
	 * Corresponding controller
	 */
	private ActiveLearningResultController controller;

	/**
	 * Lists the Instance Properties of the clicked source Instance
	 */
	private TableView<InstanceProperty> sourceInstanceTable;

	/**
	 * Lists the Instance Properties of the clicked target Instance
	 */
	private TableView<InstanceProperty> targetInstanceTable;

	/**
	 * List of Results from Mapping
	 */

	/**
	 * MenuItem to save Results to File
	 */
	private MenuItem itemSaveResults;

	/**
	 * List of Results from Mapping
	 */
	private ObservableList<Result> results;

	/**
	 * Default constructor
	 */
	public ActiveLearningResultView() {
		this.controller = new ActiveLearningResultController(this);

		Stage stage = new Stage();

		VBox root = new VBox();
		HBox resultProperties = new HBox();

		// Build Menubar for saving of results
		Menu menuFile = new Menu("File");
		itemSaveResults = new MenuItem("Save Results");
		itemSaveResults.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showSaveDialog(stage);
			if (file != null) {
				controller.saveResults(results, file);
			}
		});
		menuFile.getItems().add(itemSaveResults);
		root.getChildren().add(new MenuBar(menuFile));

		// Build table for sourceproperties
		sourceInstanceTable = new TableView<InstanceProperty>();
		TableColumn<InstanceProperty, String> sourceInstancePropertyColumn = new TableColumn<InstanceProperty, String>(
				"Property");
		sourceInstancePropertyColumn
				.setCellValueFactory(new PropertyValueFactory<>("property"));

		sourceInstanceTable.getColumns().add(sourceInstancePropertyColumn);
		TableColumn<InstanceProperty, String> sourceInstanceValueColumn = new TableColumn<InstanceProperty, String>(
				"Value");
		sourceInstanceValueColumn
				.setCellValueFactory(new PropertyValueFactory<>("value"));

		sourceInstanceTable.getColumns().add(sourceInstanceValueColumn);
		resultProperties.getChildren().add(sourceInstanceTable);

		// set size of columns
		sourceInstancePropertyColumn.prefWidthProperty().bind(
				sourceInstanceTable.widthProperty().divide(2));
		sourceInstanceValueColumn.prefWidthProperty().bind(
				sourceInstanceTable.widthProperty().divide(2));

		// Build table for targetproperties
		targetInstanceTable = new TableView<InstanceProperty>();
		TableColumn<InstanceProperty, String> targetInstancePropertyColumn = new TableColumn<InstanceProperty, String>(
				"Property");
		targetInstancePropertyColumn
				.setCellValueFactory(new PropertyValueFactory<>("property"));
		targetInstanceTable.getColumns().add(targetInstancePropertyColumn);
		TableColumn<InstanceProperty, String> targetInstanceValueColumn = new TableColumn<InstanceProperty, String>(
				"Value");
		targetInstanceValueColumn
				.setCellValueFactory(new PropertyValueFactory<>("value"));
		targetInstanceTable.getColumns().add(targetInstanceValueColumn);
		resultProperties.getChildren().add(targetInstanceTable);

		// set size of columns
		targetInstancePropertyColumn.prefWidthProperty().bind(
				targetInstanceTable.widthProperty().divide(2));
		targetInstanceValueColumn.prefWidthProperty().bind(
				targetInstanceTable.widthProperty().divide(2));

		// Build table for instances
		table = new TableView<ActiveLearningResult>();
		TableColumn<ActiveLearningResult, String> columnSource = new TableColumn<ActiveLearningResult, String>(
				"Source URI");
		columnSource
				.setCellValueFactory(new PropertyValueFactory<>("sourceURI"));
		table.getColumns().add(columnSource);
		TableColumn<ActiveLearningResult, String> columnTarget = new TableColumn<ActiveLearningResult, String>(
				"Target URI");
		columnTarget
				.setCellValueFactory(new PropertyValueFactory<>("targetURI"));
		table.getColumns().add(columnTarget);
		TableColumn<ActiveLearningResult, Double> columnValue = new TableColumn<ActiveLearningResult, Double>(
				"value");
		columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
		table.getColumns().add(columnValue);

		TableColumn<ActiveLearningResult, Boolean> columnIsMatch = new TableColumn<ActiveLearningResult, Boolean>(
				"isMatch");
		columnIsMatch
				.setCellValueFactory(new PropertyValueFactory<>("isMatch"));
		table.getColumns().add(columnIsMatch);

		table.setOnMouseClicked(e -> {
			controller.showProperties(table.getSelectionModel()
					.getSelectedItem());
		});

		// set size
		columnSource.prefWidthProperty().bind(
				table.widthProperty().divide(40).multiply(17));
		columnTarget.prefWidthProperty().bind(
				table.widthProperty().divide(40).multiply(17));
		columnValue.prefWidthProperty().bind(table.widthProperty().divide(10));

		root.getChildren().add(resultProperties);
		root.getChildren().add(table);

		// Set sourceInstanceTable and targetInstanceTable to currentWindowsize
		root.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				sourceInstanceTable.setMinWidth(arg2.doubleValue() / 2);
				sourceInstanceTable.setMaxWidth(arg2.doubleValue() / 2);
				sourceInstanceTable.setPrefWidth(arg2.doubleValue() / 2);
				targetInstanceTable.setMinWidth(arg2.doubleValue() / 2);
				targetInstanceTable.setMaxWidth(arg2.doubleValue() / 2);
				targetInstanceTable.setPrefWidth(arg2.doubleValue() / 2);

			}
		});

		Scene scene = new Scene(root, 800, 600);
		sourceInstanceTable.setPrefWidth(scene.getWidth() / 2);
		targetInstanceTable.setPrefWidth(scene.getWidth() / 2);

		stage.setTitle("LIMES");
		stage.setScene(scene);
		stage.show();

	}

	public void showResults(ObservableList<ActiveLearningResult> results) {
		table.setItems(results);
	}

}