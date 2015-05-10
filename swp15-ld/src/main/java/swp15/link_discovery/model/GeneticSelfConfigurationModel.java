package swp15.link_discovery.model;

import org.jgap.InvalidConfigurationException;

import swp15.link_discovery.view.SelfConfigurationView;
import de.uni_leipzig.simba.cache.HybridCache;
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
				
				//Start Learning
				Thread thread = new Thread(){
					public void run(){
						HybridCache sourceCache = HybridCache.getData(currentConfig.getSourceInfo());
						HybridCache targetCache = HybridCache.getData(currentConfig.getTargetInfo());
						GeneticSelfConfigurator learner = new BasicGeneticSelfConfigurator();
						try {
							learnedMetric = learner.learn(params);
						} catch (InvalidConfigurationException e) {
							e.printStackTrace();
						}
//TODO implementieren dieser Methoden in Config
//						config.setMetricExpression(learnedMetric.getExpression());
//						config.setAcceptanceThreshold(learnedMetric.getThreshold());
//
//						learnedMapping = learner.getMapping();
//						onFinish(sourceCache, targetCache);
					}
				};
				//thread.start();
				
	}

}
