package swp15.link_discovery.model;

import swp15.link_discovery.view.ActiveLearningView;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.genetics.core.Metric;
import de.uni_leipzig.simba.genetics.learner.GeneticActiveLearner;
import de.uni_leipzig.simba.genetics.learner.SupervisedLearnerParameters;
import de.uni_leipzig.simba.genetics.util.PropertyMapping;

/**
 * Handles the data involved in the active learning process
 * 
 * @author Felix Brei
 *
 */
public class ActiveLearningModel {

	/**
	 * The best mapping so far
	 */
	public Mapping bestMapping = new Mapping();

	/**
	 * LIMES' own genetic learning class
	 */
	private GeneticActiveLearner learner;

	/**
	 * Corresponding view to display the results
	 */
	private ActiveLearningView view;

	/**
	 * Stores the properties
	 */
	private PropertyMapping props;

	/**
	 * Stores the parameters for the genetic learning process
	 */
	private SupervisedLearnerParameters params;

	/**
	 * The current configuration
	 */
	private Config currentConfig;

	/**
	 * Default constructor
	 * 
	 * @param v
	 * @param c
	 */
	public ActiveLearningModel(ActiveLearningView v, Config c) {
		this.learner = new GeneticActiveLearner();
		view = v;
		currentConfig = c;

	}

	/**
	 * Calculates the next generation of results and returns the best one
	 * 
	 * @param currentConfig
	 *            The current configuration
	 * @return Best mapping so far
	 */
	public Mapping learn(Config currentConfig) {
		if (!currentConfig.propertyMapping.wasSet()) {
			currentConfig.propertyMapping.setDefault(
					currentConfig.getSourceInfo(),
					currentConfig.getTargetInfo());
		}
		this.params = new SupervisedLearnerParameters(
				currentConfig.getConfigReader(), currentConfig.propertyMapping);
		params.setCrossoverRate((float) view.getCrossProb());
		params.setPopulationSize(view.getPopulationSize());
		params.setMutationRate((float) view.getMutationRate());
		params.setGenerations(view.getNumGenerations());
		params.setTrainingDataSize(view.getNumInquiries());
		try {
			learner.init(currentConfig.getSourceInfo(),
					currentConfig.getTargetInfo(), params);
		} catch (Exception e) {

		}
		bestMapping = learner.learn(bestMapping);
		return bestMapping;
	}

	/**
	 * Gets the currently best metric
	 * 
	 * @return Best metric so far
	 */
	public Metric getMetric() {
		return learner.terminate();
	}
}
