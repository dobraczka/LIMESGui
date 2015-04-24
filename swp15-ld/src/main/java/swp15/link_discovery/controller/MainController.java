package swp15.link_discovery.controller;

import java.io.File;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.Result;
import swp15.link_discovery.view.MainView;
import swp15.link_discovery.view.ResultView;

public class MainController {
	private MainView view;
	private Config currentConfig;

	public MainController(MainView view) {
		this.view = view;
		view.showLoadedConfig(false);
	}

	public void newConfig() {
		confirmPotentialDataLoss();
		view.showInformationDialog("Not implemented yet.");
	}

	public void loadConfig(File file) {
		confirmPotentialDataLoss();
		try {
			currentConfig = Config.loadFromFile(file);
			view.showLoadedConfig(true);
		} catch (Exception e) {
			view.showErrorDialog(
					"Exception while loading config: " + e.getMessage(),
					e.getMessage());
		}
	}

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

	public void exit() {
		confirmPotentialDataLoss();
		Platform.exit();
	}

	private void confirmPotentialDataLoss() {
		if (currentConfig == null) {
			return;
		}
		// TODO: Prüfe, ob Änderungen seit letztem Speichervorgang vorliegen. Im
		// Moment sind Änderungen noch nicht implementiert, also muss auch kein
		// Dialog angezeigt werden.
		// view.showDataLossDialog();
	}

	public void map(ResultView resultView) {
		if (currentConfig == null) {
			return;
		}
		ObservableList<Result> results = currentConfig.doMapping();
		//resultView.showResults(results);
		resultView.showResults(results, currentConfig.getSourceInfo(), currentConfig.getTargetInfo());
	}
}
