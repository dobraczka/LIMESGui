package swp15.link_discovery.model;

import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swp15.link_discovery.view.MeshBasedSelfConfigurationPanel;
import swp15.link_discovery.view.ResultView;
import swp15.link_discovery.view.SelfConfigurationPanelInterface;
import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.genetics.util.PropertyMapping;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.selfconfig.ComplexClassifier;
import de.uni_leipzig.simba.selfconfig.DisjunctiveMeshSelfConfigurator;
import de.uni_leipzig.simba.selfconfig.LinearMeshSelfConfigurator;
import de.uni_leipzig.simba.selfconfig.MeshBasedSelfConfigurator;
import de.uni_leipzig.simba.selfconfig.SimpleClassifier;

public class MeshBasedSelfConfigurationModel implements
		SelfConfigurationModelInterface {

	private Mapping learnedMapping;

	ObservableList<ResultsList> savedResults = FXCollections
			.observableArrayList();
	MeshBasedSelfConfigurator selfConfigurator;
	List<SimpleClassifier> classifiers;
	ComplexClassifier cc;
	ObservableList<String> metricList = FXCollections.observableArrayList();

	@Override
	public void learn(Config currentConfig, SelfConfigurationPanelInterface view) {

		Thread thr = new Thread() {
			public void run() {
				Cache sourceCache = currentConfig.getSourceEndpoint()
						.getCache();
				Cache targetCache = currentConfig.getTargetEndpoint()
						.getCache();
				double[] params = view.getUIParams();
				switch ((int) params[1]) {
				case 0:
					selfConfigurator = new MeshBasedSelfConfigurator(
							sourceCache, targetCache, params[5], params[0]);
					break;
				case 1:
					selfConfigurator = new LinearMeshSelfConfigurator(
							sourceCache, targetCache, params[5], params[0]);
					break;
				case 2:
					selfConfigurator = new DisjunctiveMeshSelfConfigurator(
							sourceCache, targetCache, params[5], params[0]);
					break;
				default:
					selfConfigurator = new MeshBasedSelfConfigurator(
							sourceCache, targetCache, params[5], params[0]);
					break;
				}

				classifiers = selfConfigurator.getBestInitialClassifiers();
				showSimpleClassifiers(view, currentConfig);

				if (classifiers.size() > 0) {
					try {
						classifiers = selfConfigurator
								.learnClassifer(classifiers);
						cc = selfConfigurator.getZoomedHillTop((int) params[2],
								(int) params[3], classifiers);

						classifiers = cc.classifiers;

						String generatedMetricexpression = generateMetric(
								cc.classifiers, "", currentConfig);
						showComplexClassifier(view);

						if (cc.mapping != null && cc.mapping.size() > 0)
							learnedMapping = cc.mapping;
						currentConfig
								.setMetricExpression(generatedMetricexpression);
						metricList.add(generatedMetricexpression);
						currentConfig
								.setAcceptanceThreshold(getThreshold(cc.classifiers));
						System.out.println("SelfConfig class= "
								+ selfConfigurator.getClass()
										.getCanonicalName());

						onFinish(currentConfig, view);
					} catch (Exception e) {

					}
					//
				} else {
					// indicator.setValue(new Float(5f/steps));
					// stepPanel.setCaption(messages.getString("MeshBasedSelfConfigPanel.nosimpleclassifiers"));
				}

				onFinish(currentConfig, view);
			}

		};
		thr.start();

	}

	private void onFinish(Config currentConfig,
			SelfConfigurationPanelInterface view) {
		view.view.view.graphBuild.graphBuildController.setConfig(currentConfig);
		ObservableList<Result> results = FXCollections.observableArrayList();
		learnedMapping.map.forEach((sourceURI, map2) -> {
			map2.forEach((targetURI, value) -> {
				results.add(new Result(sourceURI, targetURI, value));
			});
		});
		savedResults.add(new ResultsList(results));
		view.learnButton.setDisable(false);
		view.mapButton.setOnAction(e -> {
			int i = ((MeshBasedSelfConfigurationPanel) view).resultSelect
					.getSelectionModel().getSelectedIndex();
			ResultView resultView = new ResultView();
			resultView.showResults(savedResults.get(i).getResults(),
					currentConfig);
			currentConfig.setMetricExpression(metricList.get(i));
			view.view.view.graphBuild.graphBuildController
					.setConfig(currentConfig);

		});

		if (learnedMapping != null && learnedMapping.size() > 0) {
			view.mapButton.setDisable(false);
			view.progressIndicator.setVisible(false);
			System.out
					.println(currentConfig.getConfigReader().metricExpression);
			view.view.view.graphBuild.graphBuildController.setConfigFromGraph();
		}
	}

	private void showSimpleClassifiers(SelfConfigurationPanelInterface view,
			Config currentConfig) {
		if (classifiers.size() > 0) {
			currentConfig.propertyMapping = new PropertyMapping();
		}
		for (SimpleClassifier c : classifiers) {
			((MeshBasedSelfConfigurationPanel) view).resultSelect
					.setVisible(true);
			((MeshBasedSelfConfigurationPanel) view).resultSelect.getItems()
					.add(c);
			((MeshBasedSelfConfigurationPanel) view).resultSelect
					.getSelectionModel().select(c);
			// if(c.measure.equalsIgnoreCase("euclidean"){
			// currentConfig.propertyMapping.a
			// }
		}

	}

	private void showComplexClassifier(SelfConfigurationPanelInterface view) {

	}

	private String generateMetric(SimpleClassifier sl, Config currentConfig) {
		KBInfo source = currentConfig.getSourceInfo();
		KBInfo target = currentConfig.getTargetInfo();
		String metric = ""; //$NON-NLS-1$

		metric += sl.measure
				+ "(" + source.var.replaceAll("\\?", "") + "." + sl.sourceProperty; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		metric += "," + target.var.replaceAll("\\?", "") + "." + sl.targetProperty + ")|" + sl.threshold; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		return metric;
	}

	private String generateMetric(List<SimpleClassifier> originalCCList,
			String expr, Config currentConfig) {
		// need to copy them
		List<SimpleClassifier> sCList = new LinkedList<SimpleClassifier>();
		for (SimpleClassifier sC : originalCCList)
			sCList.add(sC);

		if (sCList.size() == 0)
			return expr;
		if (expr.length() == 0) {// nothing generated before
			if (sCList.size() == 1) {
				String metric = generateMetric(sCList.get(0), currentConfig);
				return metric.substring(0, metric.lastIndexOf("|"));
			} else {// recursive
				String nestedExpr = "AND("
						+ generateMetric(sCList.remove(0), currentConfig) + ","
						+ generateMetric(sCList.remove(0), currentConfig) + ")";
				return generateMetric(sCList, nestedExpr, currentConfig);
			}
		} else { // have to combine, recursive
			String nestedExpr = "AND(" + expr + ","
					+ generateMetric(sCList.remove(0), currentConfig) + ")";
			return generateMetric(sCList, nestedExpr, currentConfig);
		}
	}

	private double getThreshold(List<SimpleClassifier> classifiers) {
		double min = Double.MAX_VALUE;
		for (SimpleClassifier sC : classifiers) {
			if (sC.threshold <= min)
				min = sC.threshold;
		}
		return min > 1 ? 0.5d : min;
	}

	private static class ResultsList {
		private ObservableList<Result> res;

		public ResultsList(ObservableList<Result> res) {
			this.res = res;
		}

		public ObservableList<Result> getResults() {
			return this.res;
		}

	}

}
