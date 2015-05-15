package swp15.link_discovery.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import swp15.link_discovery.model.GeneticSelfConfigurationModel;

public class GeneticSelfConfigurationPanel extends
		SelfConfigurationPanelInterface {

	double[] UIparams;

	public GeneticSelfConfigurationPanel(SelfConfigurationView view) {
		super(view);
		createWindow();
		view.controller.setModel(new GeneticSelfConfigurationModel());

	}

	private void createWindow() {
		view.selfConfigWrapper.getChildren().clear();
		Slider pseudoF = new Slider();
		Label pseudoFLabel = new Label("1");
		Label pseudoFText = new Label("Beta value for the pseudo-f-Measure");
		HBox pseudoBox = new HBox();

		view.selfConfigWrapper.getChildren().add(pseudoFText);
		pseudoBox.getChildren().add(pseudoF);
		pseudoBox.getChildren().add(pseudoFLabel);
		view.selfConfigWrapper.getChildren().add(pseudoBox);

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
		HBox crossoverBox = new HBox();

		view.selfConfigWrapper.getChildren().add(crossoverText);
		crossoverBox.getChildren().add(crossover);
		crossoverBox.getChildren().add(crossoverLabel);
		view.selfConfigWrapper.getChildren().add(crossoverBox);

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
		Spinner<Integer> generations = new Spinner<Integer>(5, 100, 5, 5);

		view.selfConfigWrapper.getChildren().add(generationsText);
		view.selfConfigWrapper.getChildren().add(generations);

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
		HBox mutationsBox = new HBox();
		Slider mutationRate = new Slider();
		Label mutationRateLabel = new Label("0.4");
		Label mutationRateText = new Label("Mutation rate");

		view.selfConfigWrapper.getChildren().add(mutationRateText);
		mutationsBox.getChildren().add(mutationRate);
		mutationsBox.getChildren().add(mutationRateLabel);
		view.selfConfigWrapper.getChildren().add(mutationsBox);

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
		Spinner<Integer> population = new Spinner<Integer>(5, 100, 5, 5);

		view.selfConfigWrapper.getChildren().add(populationText);
		view.selfConfigWrapper.getChildren().add(population);

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
			view.controller.learn();

		});
		mapButton.setDisable(true);
		view.selfConfigWrapper.getChildren().add(buttonWrapper);
	}

	public double[] getUIParams() {
		return this.UIparams;
	}

}
