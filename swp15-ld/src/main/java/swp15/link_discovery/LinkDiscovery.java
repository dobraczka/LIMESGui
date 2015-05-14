package swp15.link_discovery;

import javafx.application.Application;
import javafx.stage.Stage;

import org.apache.log4j.BasicConfigurator;

import swp15.link_discovery.controller.MainController;
import swp15.link_discovery.view.MainView;

/**
 * Starts the LinkDiscovery Application
 * 
 * @author Manuel Jacob
 *
 */
public class LinkDiscovery extends Application {

	@Override
	/**
	 * Opens a new Window for the Application
	 * @param primaryStage View to initialize Application
	 * @exception Thrown if initialization didn't work properly
	 */
	public void start(Stage primaryStage) throws Exception {
		MainView mainView = new MainView(primaryStage);
		MainController mainController = new MainController(mainView);
		mainView.setController(mainController);
	}

	/**
	 * Main function Entry Point for the Application
	 * 
	 * @param args
	 *            optional arguments on StartUp, No Options implemented yet
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		launch(args);
	}
}
