package swp15.link_discovery;

import javafx.application.Application;
import javafx.stage.Stage;

import org.apache.log4j.BasicConfigurator;

import swp15.link_discovery.controller.MainController;
import swp15.link_discovery.view.MainView;

public class LinkDiscovery extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		MainView mainView = new MainView(primaryStage);
		MainController mainController = new MainController(mainView);
		mainView.setController(mainController);
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();
		launch(args);
	}
}
