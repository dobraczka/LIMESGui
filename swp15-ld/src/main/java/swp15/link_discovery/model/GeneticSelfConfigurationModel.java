package swp15.link_discovery.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import org.jgap.InvalidConfigurationException;


import swp15.link_discovery.view.ResultView;
import swp15.link_discovery.view.SelfConfigurationView;
import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.genetics.core.Metric;
import de.uni_leipzig.simba.genetics.learner.UnSupervisedLearnerParameters;
import de.uni_leipzig.simba.genetics.selfconfig.BasicGeneticSelfConfigurator;
import de.uni_leipzig.simba.genetics.selfconfig.GeneticSelfConfigurator;
import de.uni_leipzig.simba.selfconfig.PseudoMeasures;
import de.uni_leipzig.simba.selfconfig.ReferencePseudoMeasures;

/**
 * Class to perform genetic self configuration using parameters from SelfConfigurationView
 * @author Sascha Hahne, Daniel Obraczka
 *
 */
public class GeneticSelfConfigurationModel implements SelfConfigurationModelInterface {
	/**
	 * Contains the parameters of the currentConfig
	 */
	private UnSupervisedLearnerParameters params;
	
	/**
	 * Output metric of the selfconfiguration
	 */
	private Metric learnedMetric;
	
	/**
	 * learned Mapping of the selfconfiguration
	 */
	private Mapping learnedMapping = new Mapping();
	
	/**
	 * thread for better performance
	 */
	private Thread thread;
	
	/**
	 * Constructor
	 */
	public GeneticSelfConfigurationModel () {
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * Performs genetic self configuration.
	 * Uses the parameters of the currentConfig and of the UserInterface in SelfConfigurationView
	 * @param currentConfig the currently used config
	 * @param view the corresponding view
	 */
	public void learn(Config currentConfig, SelfConfigurationView view) {
				//check if PropertyMapping was set else set to default
				if(!currentConfig.propertyMapping.wasSet()){
					currentConfig.propertyMapping.setDefault(currentConfig.getSourceInfo(),
							currentConfig.getTargetInfo());
				}
				//Get Parameters
				this.params = new UnSupervisedLearnerParameters(currentConfig.getConfigReader(),
						currentConfig.propertyMapping);
				double[] UIparams = view.getUIParams();
				params.setPFMBetaValue(UIparams[0]);
				params.setCrossoverRate((float)UIparams[1]);
				params.setGenerations((int)UIparams[2]);
				PseudoMeasures pseudoMeasure = new PseudoMeasures();
				if((int)UIparams[3] == 1) {
					pseudoMeasure = new ReferencePseudoMeasures();
				}
				params.setPseudoFMeasure(pseudoMeasure);
				params.setMutationRate((float)UIparams[4]);
				params.setPopulationSize((int)UIparams[5]);
				
//				params.setPFMBetaValue(1);
//				params.setCrossoverRate(0.4f);
//				params.setGenerations(10);
//				PseudoMeasures pseudoMeasure = new PseudoMeasures();
//				params.setPseudoFMeasure(pseudoMeasure);
//				params.setMutationRate(0.4f);
//				params.setPopulationSize(10);
		
			
				//Start Learning
				thread = new Thread(){
					public void run(){
						HybridCache sourceCache = HybridCache.getData(currentConfig.getSourceInfo());
						HybridCache targetCache = HybridCache.getData(currentConfig.getTargetInfo());
						GeneticSelfConfigurator learner = new BasicGeneticSelfConfigurator();
						try {
							learnedMetric = learner.learn(params);
						} catch (InvalidConfigurationException e) {
							e.printStackTrace();
						}

						currentConfig.setMetricExpression(learnedMetric.getExpression());
						currentConfig.setAcceptanceThreshold(learnedMetric.getThreshold());

						learnedMapping = learner.getMapping();
						System.out.println("während run" + sourceCache);
						onFinish(currentConfig, view);
					}
				};
				thread.start();
				
	}
	private void onFinish(Config currentConfig, SelfConfigurationView view) {
		view.learnButton.setDisable(false);
		view.mapButton.setOnAction( 
				e -> {
					ObservableList<Result> results = FXCollections.observableArrayList();
					learnedMapping.map.forEach((sourceURI, map2) ->{
						map2.forEach((targetURI,value)->{
							results.add(new Result(sourceURI, targetURI, value));
						});
					});
					ResultView resultView = new ResultView();
					resultView.showResults(results, currentConfig);
				});
		if(learnedMapping!= null && learnedMapping.size()>0) {
			view.mapButton.setDisable(false);
		}
	}

}
