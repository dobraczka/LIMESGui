package swp15.link_discovery.view;

import java.io.File;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import swp15.link_discovery.controller.MainController;
import swp15.link_discovery.view.graphBuilder.GraphBuildView;

/**
 * Main View of the Application
 * 
 * @author Manuel Jacob
 *
 */
public class MainView {
	/**
	 * Corresponding Controller
	 */
	private MainController controller;
	/**
	 * MenuItem to do a Save Operation
	 */
	private MenuItem itemSave;

	/**
	 * MenuItem to start Mapping
	 */
	private MenuItem itemMap;

	/**
	 * MenuItem to start the SelfConfiguration Dialog
	 */
	private MenuItem itemSelfConfiguration;

	/**
	 * Toolbox of the MainView adds Nodes to Graph
	 */
	public ToolBox toolBox;

	/**
	 * GraphBuildView to Model and View the Metric
	 */
	public GraphBuildView graphBuild;

	/**
	 * MenuItem to start the Active Learning Dialog
	 */
	private MenuItem itemActiveLearning;

	/**
	 * Constructor
	 * 
	 * @param stage
	 *            Used Stage of the Application
	 */
	public MainView(Stage stage) {
		showWindow(stage);
	}

	/**
	 * Sets corresponding Controller
	 * 
	 * @param controller
	 *            Corresponding Controller
	 */
	public void setController(MainController controller) {
		this.controller = controller;
	}

	/**
	 * Builds and Shows the Window
	 * 
	 * @param stage
	 */
	private void showWindow(Stage stage) {
		BorderPane root = new BorderPane();

		MenuBar menuBar = buildMenuBar(stage);
		toolBox = new ToolBox(this);
		graphBuild = new GraphBuildView();
		root.setTop(menuBar);
		root.setLeft(toolBox);
		root.setRight(graphBuild);
		graphBuild.widthProperty().bind(
				root.widthProperty().subtract(toolBox.widthProperty()));
		graphBuild.heightProperty().bind(
				root.heightProperty().subtract(menuBar.heightProperty()));
		graphBuild.start();

		Scene scene = new Scene(root, 800, 600);
		stage.setMinHeight(600);
		stage.setMinWidth(600);
		stage.setTitle("LIMES");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Builds and returns MenuBar for the MainView
	 * 
	 * @param stage
	 *            Used Stage of the Application
	 * @return MenuBar of the Application
	 */
	private MenuBar buildMenuBar(Window stage) {
		Menu menuFile = new Menu("File");
		MenuItem itemNew = new MenuItem("New");
		itemNew.setOnAction(e -> controller.newConfig(new WizardView(),
				new EditEndpointsView(), new EditClassMatchingView(),
				new EditPropertyMatchingView()));
		menuFile.getItems().add(itemNew);
		menuFile.getItems().add(new SeparatorMenuItem());
		MenuItem itemLoad = new MenuItem("Load config");
		itemLoad.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(stage);
			if (file != null) {
				controller.loadConfig(file);
			}
		});
		menuFile.getItems().add(itemLoad);
		itemSave = new MenuItem("Save config");
		itemSave.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showSaveDialog(stage);
			if (file != null) {
				controller.saveConfig(file);
			}
		});
		menuFile.getItems().add(itemSave);
		menuFile.getItems().add(new SeparatorMenuItem());
		MenuItem itemExit = new MenuItem("Exit");
		itemExit.setOnAction(e -> controller.exit());
		menuFile.getItems().add(itemExit);

		Menu menuLayout = new Menu("Layout");
		MenuItem layoutGraph = new MenuItem("Refresh Layout");
		layoutGraph.setOnAction(e -> {
			graphBuild.graphBuildController.layoutGraph();
		});
		MenuItem deleteGraph = new MenuItem("Delete Graph");
		deleteGraph.setOnAction(e -> {
			graphBuild.graphBuildController.deleteGraph();
		});
		menuLayout.getItems().addAll(layoutGraph, deleteGraph);

		Menu menuRun = new Menu("Run");
		itemMap = new MenuItem("Start Mapping");
		itemMap.setOnAction(e -> {
			controller.map();
		});
		menuRun.getItems().add(itemMap);

		itemSelfConfiguration = new MenuItem("Self Configuration");
		itemSelfConfiguration.setOnAction(e -> {
			controller.showSelfConfig();
		});
		itemActiveLearning = new MenuItem("Active Learning");
		itemActiveLearning.setOnAction(e -> {
			controller.showActiveLearning();
		});
		menuRun.getItems().add(itemActiveLearning);
		menuRun.getItems().add(itemSelfConfiguration);
		return new MenuBar(menuFile, menuLayout, menuRun);
	}

	/**
	 * Shows the Loaded Config, if it is Loaded
	 * 
	 * @param isLoaded
	 *            True if Config is Loaded
	 */
	public void showLoadedConfig(boolean isLoaded) {
		itemSave.setDisable(!isLoaded);
		itemMap.setDisable(!isLoaded);
		itemSelfConfiguration.setDisable(!isLoaded);
		itemActiveLearning.setDisable(!isLoaded);
	}

	/**
	 * Shows an Information Dialog with a messag
	 * 
	 * @param message
	 *            Message To Show
	 */
	public void showInformationDialog(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(message);
		alert.showAndWait();
	}

	/**
	 * Shows if an Error occurred
	 * 
	 * @param header
	 *            Caption of the Error
	 * @param content
	 *            Error Message
	 */
	public void showErrorDialog(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
