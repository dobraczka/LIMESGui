package swp15.link_discovery.view;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import swp15.link_discovery.controller.ResultController;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.InstanceProperty;
import swp15.link_discovery.model.Result;

public class ResultView {
	private TableView<Result> table;
	private ResultController controller;
	private TableView<InstanceProperty> sourceInstanceTable;
	private TableView<InstanceProperty> targetInstanceTable;

	public ResultView() {
		createWindow();
		this.controller = new ResultController(this);
	}

	private void createWindow() {
		BorderPane root = new BorderPane();
		BorderPane test = new BorderPane();
		
		sourceInstanceTable = new TableView<InstanceProperty>();
		TableColumn<InstanceProperty, String> sourceInstancePropertyColumn = 
				new TableColumn<InstanceProperty, String>("Property");
		sourceInstancePropertyColumn.setCellValueFactory(new PropertyValueFactory<>("property"));
		sourceInstanceTable.getColumns().add(sourceInstancePropertyColumn);
		TableColumn<InstanceProperty, String> sourceInstanceValueColumn = 
				new TableColumn<InstanceProperty, String>("Value");
		sourceInstanceValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		sourceInstanceTable.getColumns().add(sourceInstanceValueColumn);
		test.setLeft(sourceInstanceTable);

		
		targetInstanceTable = new TableView<InstanceProperty>();
		TableColumn<InstanceProperty, String> targetInstancePropertyColumn = 
				new TableColumn<InstanceProperty, String>("Property");
		targetInstancePropertyColumn.setCellValueFactory(new PropertyValueFactory<>("property"));
		targetInstanceTable.getColumns().add(targetInstancePropertyColumn);
		TableColumn<InstanceProperty, String> targetInstanceValueColumn = 
				new TableColumn<InstanceProperty, String>("Value");
		targetInstanceValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		targetInstanceTable.getColumns().add(targetInstanceValueColumn);
		test.setRight(targetInstanceTable);
		
		table = new TableView<Result>();
		TableColumn<Result, String> columnSource = new TableColumn<Result, String>(
				"Source URI");
		columnSource
				.setCellValueFactory(new PropertyValueFactory<>("sourceURI"));
		table.getColumns().add(columnSource);
		TableColumn<Result, String> columnTarget = new TableColumn<Result, String>(
				"Target URI");
		columnTarget
				.setCellValueFactory(new PropertyValueFactory<>("targetURI"));
		table.getColumns().add(columnTarget);
		TableColumn<Result, Double> columnValue = new TableColumn<Result, Double>(
				"value");
		columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
		table.getColumns().add(columnValue);
		table.setOnMouseClicked(e ->{
			controller.showProperties(table.getSelectionModel().getSelectedItem());
		});
		root.setTop(test);
		root.setCenter(table);

		Scene scene = new Scene(root, 800, 600);
		Stage stage = new Stage();
		stage.setTitle("LIMES");
		stage.setScene(scene);
		stage.show();
	}

	public void showResults(ObservableList<Result> results) {
		table.setItems(results);
	}
	public void showResults(ObservableList<Result> results,Config currentConfig) {
		table.setItems(results);
		this.controller.setCurrentConfig(currentConfig);
	}
	public void showSourceInstance(ObservableList<InstanceProperty> instanceProperty){
		sourceInstanceTable.setItems(instanceProperty);
	}
	public void showTargetInstance(ObservableList<InstanceProperty> instanceProperty){
		targetInstanceTable.setItems(instanceProperty);
	}
}
