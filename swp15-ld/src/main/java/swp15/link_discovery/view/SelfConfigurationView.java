package swp15.link_discovery.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import swp15.link_discovery.controller.SelfConfigurationController;
import swp15.link_discovery.model.GeneticSelfConfigurationModel;

/**
 * UI for selfconfiguration
 * 
 * @author Sascha Hahne, Daniel Obraczka
 *
 */
public class SelfConfigurationView {
	/**
	 * Corresponding controller
	 */
	public SelfConfigurationController controller;

	/**
	 * ChoiceBox to chose selfconfiguration-mode
	 */
	private ChoiceBox<String> selfConfigChooser;

	/**
	 * Vertical Box for the chooser
	 */
	private VBox selfConfigWrapper;

	/**
	 * Parameters given by the User
	 */
	private double[] UIparams;

	/**
	 * Button to start the mapping
	 */
	public Button mapButton;

	/**
	 * Button to start the learning
	 */
	public Button learnButton;

	/**
	 * Constructor initializes controller
	 */
	public SelfConfigurationView() {
		createWindow();
		this.controller = new SelfConfigurationController(this);
	}

	/**
	 * Create the window for User Input
	 */
	public void createWindow() {
		BorderPane root = new BorderPane();
		HBox content = new HBox();
		selfConfigChooser = new ChoiceBox<String>(
				FXCollections.observableArrayList("Genetics", "Meshbased"));
		selfConfigChooser
				.getSelectionModel()
				.selectedIndexProperty()
				.addListener(
						(arg0, value, new_value) -> {
							switch (new_value.intValue()) {
							case 0:
								controller
										.setModel(new GeneticSelfConfigurationModel());
								setGeneticConfigurationView();

								break;
							case 1:
								// TODO Meshbased
								controller
										.setModel(new GeneticSelfConfigurationModel());
								break;
							default:
								break;
							}

						}

				);

		content.getChildren().add(selfConfigChooser);
		selfConfigWrapper = new VBox();
		selfConfigWrapper.setFillWidth(false);
		root.setTop(content);
		root.setCenter(selfConfigWrapper);
		Scene scene = new Scene(root, 800, 600);

		Stage stage = new Stage();
		stage.setTitle("LIMES - Self Configuration");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * UI for genetic selfconfiguration
	 */
	public void setGeneticConfigurationView() {
		selfConfigWrapper.getChildren().clear();
		Slider pseudoF = new Slider();
		Label pseudoFLabel = new Label("1");
		Label pseudoFText = new Label("Beta value for the pseudo-f-Measure");
		selfConfigWrapper.getChildren().add(pseudoFText);
		selfConfigWrapper.getChildren().add(pseudoF);
		selfConfigWrapper.getChildren().add(pseudoFLabel);
		pseudoF.setMin(0.1);
		pseudoF.setMax(2);
		pseudoF.setValue(1);
		pseudoF.setShowTickLabels(true);
		pseudoF.setShowTickMarks(false);
		pseudoF.setMajorTickUnit(1);
		pseudoF.setMinorTickCount(9);
		pseudoF.setSnapToTicks(false);
		pseudoF.setBlockIncrement(0.1);

		pseudoF.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val) {
				pseudoFLabel.setText(String.format("%.1f", new_val));
			}
		});

		Slider crossover = new Slider();
		Label crossoverLabel = new Label("0.4");
		Label crossoverText = new Label("Crossover probabiltiy");
		selfConfigWrapper.getChildren().add(crossoverText);
		selfConfigWrapper.getChildren().add(crossover);
		selfConfigWrapper.getChildren().add(crossoverLabel);
		crossover.setMin(0);
		crossover.setMax(1);
		crossover.setValue(0.4);
		crossover.setShowTickLabels(true);
		crossover.setShowTickMarks(false);
		crossover.setMajorTickUnit(0.5);
		crossover.setMinorTickCount(9);
		crossover.setSnapToTicks(false);
		crossover.setBlockIncrement(0.1);

		crossover.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val) {
				crossoverLabel.setText(String.format("%.1f", new_val));
			}
		});

		Label generationsText = new Label("Number of generations");
		selfConfigWrapper.getChildren().add(generationsText);
		Spinner<Integer> generations = new Spinner<Integer>(5, 100, 5, 5);
		selfConfigWrapper.getChildren().add(generations);

		ChoiceBox<String> classifierChooser = new ChoiceBox<String>(
				FXCollections.observableArrayList("Pseudo F-Measure NGLY12",
						"Pseudo F-Measure NIK+12"));
		classifierChooser.getSelectionModel().selectedIndexProperty()
				.addListener((arg0, value, new_value) -> {
					switch (new_value.intValue()) {
					case 0:
						// TODO

						break;
					case 1:
						// TODO
						break;
					default:
						break;
					}

				});

		Slider mutationRate = new Slider();
		Label mutationRateLabel = new Label("0.4");
		Label mutationRateText = new Label("Mutation rate");
		selfConfigWrapper.getChildren().add(mutationRateText);
		selfConfigWrapper.getChildren().add(mutationRate);
		selfConfigWrapper.getChildren().add(mutationRateLabel);
		mutationRate.setMin(0);
		mutationRate.setMax(1);
		mutationRate.setValue(0.4);
		mutationRate.setShowTickLabels(true);
		mutationRate.setShowTickMarks(false);
		mutationRate.setMajorTickUnit(0.5);
		mutationRate.setMinorTickCount(9);
		mutationRate.setSnapToTicks(false);
		mutationRate.setBlockIncrement(0.1);

		mutationRate.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val) {
				mutationRateLabel.setText(String.format("%.1f", new_val));
			}
		});

		Label populationText = new Label("Population size");
		selfConfigWrapper.getChildren().add(populationText);
		Spinner<Integer> population = new Spinner<Integer>(5, 100, 5, 5);
		selfConfigWrapper.getChildren().add(population);

		HBox buttonWrapper = new HBox();
		learnButton = new Button("Start Learning");
		mapButton = new Button("Show Mapping");
		buttonWrapper.getChildren().add(learnButton);
		buttonWrapper.getChildren().add(mapButton);
		learnButton.setOnAction(e -> {
			double[] params_new = { pseudoF.getValue(), crossover.getValue(),
					generations.getValue(),
					classifierChooser.getSelectionModel().getSelectedIndex(),
					mutationRate.getValue(), population.getValue() };
			this.UIparams = params_new;
			learnButton.setDisable(true);
			controller.learn();

		});
		mapButton.setDisable(true);
		selfConfigWrapper.getChildren().add(buttonWrapper);
	}

	/**
	 * returns the parameters chosen by the User
	 * 
	 * @return double[] parameters
	 */
	public double[] getUIParams() {
		return this.UIparams;
	}

}
