package swp15.link_discovery.controller;

import java.io.File;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.Result;
import swp15.link_discovery.view.EditEndpointsView;
import swp15.link_discovery.view.MainView;
import swp15.link_discovery.view.ResultView;
import swp15.link_discovery.view.SelfConfigurationView;
import swp15.link_discovery.view.WizardView;

/**
 * Controller of MainView
 * 
 * @author Manuel Jacob
 */
public class MainController {
	/**
	 * Corresponding View to the Controller
	 */
	private MainView view;
	/**
	 * Config of the Current Limes-Query
	 */
	private Config currentConfig;

	/**
	 * Constructor
	 * 
	 * @param view
	 *            Corresponding View
	 */
	public MainController(MainView view) {
		this.view = view;
		view.showLoadedConfig(false);
	}

	private void setCurrentConfig(Config currentConfig) {
		this.currentConfig = currentConfig;
		view.showLoadedConfig(currentConfig != null);
	}

	/**
	 * Starts a New Limes Query-Config, drops the current Config
	 * 
	 * @param createWizardView
	 * @param editEndpointsView
	 */
	public void newConfig(WizardView createWizardView,
			EditEndpointsView editEndpointsView) {
		confirmPotentialDataLoss();
		setCurrentConfig(null);
		Config newConfig = new Config();
		new EditEndpointsController(newConfig, editEndpointsView);
		new WizardController(() -> {
			setCurrentConfig(newConfig);
		}, () -> {
		}, createWizardView, editEndpointsView);
	}

	/**
	 * Reads Config from File, drops currentConfig
	 * 
	 * @param file
	 *            Linkpec-Config File in XML format
	 */
	public void loadConfig(File file) {
		confirmPotentialDataLoss();
		try {
			setCurrentConfig(Config.loadFromFile(file));
			view.showLoadedConfig(true);
			view.toolBox.showLoadedConfig(currentConfig);
		} catch (Exception e) {
			view.showErrorDialog(
					"Exception while loading config: " + e.getMessage(),
					e.getMessage());
		}
	}

	/**
	 * Saves Cuurent Config in Valid XML Format to a File
	 * 
	 * @param file
	 *            Location to save the File
	 */
	public void saveConfig(File file) {
		if (currentConfig == null) {
			return;
		}
		try {
			currentConfig.save(file);
		} catch (Exception e) {
			view.showErrorDialog(
					"Exception while saving config: " + e.getMessage(),
					e.getMessage());
		}
	}

	/**
	 * Terminates the Program
	 */
	public void exit() {
		confirmPotentialDataLoss();
		Platform.exit();
	}

	/**
	 * Tests if data could be lost
	 */
	private void confirmPotentialDataLoss() {
		if (currentConfig == null) {
			return;
		}
		// TODO: Prüfe, ob Änderungen seit letztem Speichervorgang vorliegen. Im
		// Moment sind Änderungen noch nicht implementiert, also muss auch kein
		// Dialog angezeigt werden.
		// view.showDataLossDialog();
	}

	/**
	 * Starts the editEndpoint Dialog
	 * 
	 * @param editEndpointsView
	 *            Corresponding View of the Dialog
	 */
	public void editEndpoints(EditEndpointsView editEndpointsView) {
		if (currentConfig == null) {
			return;
		}
		new EditEndpointsController(currentConfig, editEndpointsView);
	}

	/**
	 * Starts the Limes-Query and shows the Results
	 * 
	 * @param resultView
	 *            Corresponding ResultView to show the Results
	 */
	public void map(ResultView resultView) {
		if (currentConfig == null) {
			return;
		}
		ObservableList<Result> results = currentConfig.doMapping();
		resultView.showResults(results, currentConfig);
	}

	public void showSelfConfig() {
		SelfConfigurationView selfConfigView = new SelfConfigurationView();
		selfConfigView.controller.setCurrentConfig(currentConfig);
	}

}
