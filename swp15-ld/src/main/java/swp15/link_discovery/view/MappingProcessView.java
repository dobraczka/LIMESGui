package swp15.link_discovery.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.Result;

public class MappingProcessView {
	public Config currentConfig;
	public ObservableList<Result> results;
	public Task<Void> mapTask;

	/**
	 * Constructor creates a new MappingProcessView object and sets the config
	 * and results
	 * 
	 * @param currentConfig
	 *            to be used
	 * @param results
	 *            to be used
	 */
	public MappingProcessView(Config currentConfig,
			ObservableList<Result> results) {
		this.currentConfig = currentConfig;
		this.results = results;
	}

	/**
	 * show the window asking the user to start the mapping or to cancel it if
	 * the user presses the 'start' button the function createMappingTask() of
	 * the currentConfig is called this realizes the mapping the window is
	 * closed after the task has succeded 'cancel' interrupts the mappingTask
	 * and closes the window
	 */
	public void showWindow() {
		// Build Window
		Stage stage = new Stage();
		stage.setAlwaysOnTop(true);
		stage.setTitle("Mapping");
		Group root = new Group();
		Scene scene = new Scene(root, 250, 80, Color.WHITE);

		BorderPane mainPane = new BorderPane();
		root.getChildren().add(mainPane);

		final Label label = new Label("Mapping Process :");
		final ProgressBar progressBar = new ProgressBar(0);

		final HBox hb = new HBox();
		hb.setSpacing(5);
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().addAll(label, progressBar);
		mainPane.setTop(hb);

		final Button startButton = new Button("Start");
		final Button cancelButton = new Button("Cancel");
		final HBox hb2 = new HBox();
		hb2.setSpacing(5);
		hb2.setAlignment(Pos.CENTER);
		hb2.getChildren().addAll(startButton, cancelButton);
		mainPane.setBottom(hb2);

		// EventListener vor 'start' button
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			// create mappingTask and animate Progressbar
			public void handle(ActionEvent event) {
				startButton.setDisable(true);
				progressBar.setProgress(0);
				cancelButton.setDisable(false);
				mapTask = currentConfig.createMappingTask(results);

				progressBar.progressProperty().unbind();
				progressBar.progressProperty().bind(mapTask.progressProperty());

				mapTask.messageProperty().addListener(
						new ChangeListener<String>() {
							public void changed(
									ObservableValue<? extends String> observable,
									String oldValue, String newValue) {
								System.out.println(newValue);
							}
						});

				/*
				 * called when Task is finished creates new ResultView with
				 * results and closes the MappingProcess window
				 */
				mapTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
						stage.close();
						ResultView resultView = new ResultView();
						resultView.showResults(results, currentConfig);
					}
				});

				new Thread(mapTask).start();
			}
		});

		// cancels the mapping and closes the window
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				startButton.setDisable(false);
				cancelButton.setDisable(true);
				mapTask.cancel(true);
				progressBar.progressProperty().unbind();
				progressBar.setProgress(0);
				stage.close();
			}
		});
		stage.setScene(scene);
		stage.show();
	}

}
