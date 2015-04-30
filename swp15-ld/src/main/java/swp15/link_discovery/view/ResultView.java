package swp15.link_discovery.view;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import swp15.link_discovery.controller.ResultController;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.InstanceProperty;
import swp15.link_discovery.model.Result;

/**
 * View to show the Results of the LIMES-query, and their Instances
 * @author Daniel Obraczka, Sascha Hahne
 *
 */

public class ResultView {
	
	/**
	 * Table with the list of InstanceMatches
	 */
	private TableView<Result> table;
	
	/**
	 * Corresponding Controller for the Resultview
	 */
	private ResultController controller;
	
	/**
	 * Lists the Instance Properties of the clicked source Instance
	 */
	private TableView<InstanceProperty> sourceInstanceTable;

	/**
	 * Lists the Instance Properties of the clicked target Instance
	 */
	private TableView<InstanceProperty> targetInstanceTable;

	/**
	 * Constructor
	 */
	public ResultView() {
		createWindow();
		this.controller = new ResultController(this);
	}

	/**
	 * Creates the Window, with the 3 Tables, which show the matched Instances,
	 * and the Properties of clicked Soruce and Target
	 */
	private void createWindow() {
		BorderPane root = new BorderPane();
		HBox resultProperties = new HBox();
		
		sourceInstanceTable = new TableView<InstanceProperty>();
		TableColumn<InstanceProperty, String> sourceInstancePropertyColumn = 
				new TableColumn<InstanceProperty, String>("Property");
		sourceInstancePropertyColumn.setCellValueFactory(new PropertyValueFactory<>("property"));
		
		sourceInstanceTable.getColumns().add(sourceInstancePropertyColumn);
		TableColumn<InstanceProperty, String> sourceInstanceValueColumn = 
				new TableColumn<InstanceProperty, String>("Value");
		sourceInstanceValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
	
		sourceInstanceTable.getColumns().add(sourceInstanceValueColumn);
		resultProperties.getChildren().add(sourceInstanceTable);
		

		
		targetInstanceTable = new TableView<InstanceProperty>();
		TableColumn<InstanceProperty, String> targetInstancePropertyColumn = 
				new TableColumn<InstanceProperty, String>("Property");
		targetInstancePropertyColumn.setCellValueFactory(new PropertyValueFactory<>("property"));
		targetInstanceTable.getColumns().add(targetInstancePropertyColumn);
		TableColumn<InstanceProperty, String> targetInstanceValueColumn = 
				new TableColumn<InstanceProperty, String>("Value");
		targetInstanceValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		targetInstanceTable.getColumns().add(targetInstanceValueColumn);
		resultProperties.getChildren().add(targetInstanceTable);
		
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
		resultProperties.setMaxHeight(120);
		root.setTop(resultProperties);
		root.setCenter(table);

		Scene scene = new Scene(root, 800, 600);
		sourceInstanceTable.setPrefWidth(scene.getWidth()/2);
		targetInstanceTable.setPrefWidth(scene.getWidth()/2);

		Stage stage = new Stage();
		stage.setTitle("LIMES");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Puts the Results of a LIMES-query in the Table table
	 * @param results List of the Limes results following the Model of Results
	 */
	public void showResults(ObservableList<Result> results) {
		table.setItems(results);
	}
	
	/**
	 * Puts the Results of a LIMES-query in the Table table and transfers the Config to the Controller
	 * @param results List of the Limes results following the Model of Results
	 * @param currentConfig Config of current LIMES-query
	 */
	public void showResults(ObservableList<Result> results,Config currentConfig) {
		table.setItems(results);
		this.controller.setCurrentConfig(currentConfig);
	}
	
	/**
	 * Show the Items of instanceProperty in sourceInstanceTable
	 * @param instanceProperty List of Source-InstanceProperties
	 */
	public void showSourceInstance(ObservableList<InstanceProperty> instanceProperty){
		sourceInstanceTable.setItems(instanceProperty);
	}

	/**
	 * Show the Items of instanceProperty in targetInstanceTable
	 * @param instanceProperty List of Target-InstanceProperties
	 */
	public void showTargetInstance(ObservableList<InstanceProperty> instanceProperty){
		targetInstanceTable.setItems(instanceProperty);
	}
}
