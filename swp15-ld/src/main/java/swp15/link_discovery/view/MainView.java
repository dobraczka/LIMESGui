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

public class MainView {
	private MainController controller;
	private MenuItem itemSave;
	private MenuItem itemMap;

	public MainView(Stage stage) {
		showWindow(stage);
	}

	public void setController(MainController controller) {
		this.controller = controller;
	}

	private void showWindow(Stage stage) {
		BorderPane root = new BorderPane();

		MenuBar menuBar = buildMenuBar(stage);
		root.setTop(menuBar);

		Scene scene = new Scene(root, 800, 600);
		stage.setTitle("LIMES");
		stage.setScene(scene);
		stage.show();
	}

	private MenuBar buildMenuBar(Window stage) {
		Menu menuFile = new Menu("File");
		MenuItem itemNew = new MenuItem("New");
		itemNew.setOnAction(e -> controller.newConfig());
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

		Menu menuRun = new Menu("Run");
		itemMap = new MenuItem("Start Mapping");
		itemMap.setOnAction(e -> controller.map(new ResultView()));
		menuRun.getItems().add(itemMap);

		return new MenuBar(menuFile, menuRun);
	}

	public void showLoadedConfig(boolean isLoaded) {
		itemSave.setDisable(!isLoaded);
		itemMap.setDisable(!isLoaded);
	}

	public void showInformationDialog(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void showErrorDialog(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
