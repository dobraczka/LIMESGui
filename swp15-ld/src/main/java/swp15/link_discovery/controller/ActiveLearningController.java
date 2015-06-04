package swp15.link_discovery.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swp15.link_discovery.model.ActiveLearningModel;
import swp15.link_discovery.model.ActiveLearningResult;
import swp15.link_discovery.model.Config;
import swp15.link_discovery.view.ActiveLearningResultView;
import swp15.link_discovery.view.ActiveLearningView;
import de.uni_leipzig.simba.data.Mapping;

/**
 * Class that handles the input on the ActiveLearningView
 * 
 * @author Felix Brei
 *
 */
public class ActiveLearningController {

	/**
	 * Connected view
	 */
	private ActiveLearningView view;

	/**
	 * Corresponding model
	 */
	private ActiveLearningModel model;

	/**
	 * The current config of the query
	 */
	private Config currentConfig;

	/**
	 * Default constructor
	 * 
	 * @param v
	 *            Corresponding view
	 * @param c
	 *            Current config
	 */
	public ActiveLearningController(ActiveLearningView v, Config c) {
		view = v;
		currentConfig = c;
		model = new ActiveLearningModel(v, c);
	}

	/**
	 * Event that fires when the learning process starts
	 */
	public void goButtonPressed() {
		ActiveLearningResultView r = new ActiveLearningResultView(
				currentConfig, model);
		Mapping bestMapping = model.learn(currentConfig);
		ObservableList<ActiveLearningResult> results = FXCollections
				.observableArrayList();
		bestMapping.map.forEach((sourceURI, map2) -> {
			System.out.println(sourceURI + " " + map2);
			map2.forEach((targetURI, value) -> {
				results.add(new ActiveLearningResult(sourceURI, targetURI,
						value));
			});
		});
		r.getActiveLearningResultController().setMatching(results);
		r.showResults(results);
	}
}
