package swp15.link_discovery.model;

import static swp15.link_discovery.util.SourceOrTarget.SOURCE;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import swp15.link_discovery.model.metric.MetricFormatException;
import swp15.link_discovery.model.metric.MetricParser;
import swp15.link_discovery.model.metric.Output;
import swp15.link_discovery.util.ConfigWriter;
import swp15.link_discovery.util.SourceOrTarget;
import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.filter.LinearFilter;
import de.uni_leipzig.simba.genetics.util.PropertyMapping;
import de.uni_leipzig.simba.io.ConfigReader;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.mapper.SetConstraintsMapper;
import de.uni_leipzig.simba.mapper.SetConstraintsMapperFactory;

/**
 * Contains all important information of the LIMES-Query
 * 
 * @author Manuel Jacob, Sascha Hahne, Daniel Obraczka, Felix Brei
 *
 */
public class Config {
	/**
	 * Reader to load linkspec
	 */
	private ConfigReader reader;

	/**
	 * current Metric
	 */
	private Output metric = null;

	/**
	 * Source Endpoint
	 */
	private Endpoint sourceEndpoint;
	/**
	 * Target Endpoint
	 */
	private Endpoint targetEndpoint;

	/**
	 * PorpertyMapping of current query
	 */
	public PropertyMapping propertyMapping;

	/**
	 * Constructor
	 */
	public Config() {
		this(createEmptyReader());
		metric = new Output();
	}

	/**
	 * Create an empty Configreader
	 * 
	 * @return Genrated Configreader
	 */
	private static ConfigReader createEmptyReader() {
		ConfigReader reader = new ConfigReader();
		reader.sourceInfo = new KBInfo();
		reader.sourceInfo.var = "?source";
		reader.targetInfo = new KBInfo();
		reader.targetInfo.var = "?target";
		return reader;
	}

	/**
	 * Constructor
	 * 
	 * @param reader
	 *            LIMES-Config Reader
	 */
	private Config(ConfigReader reader) {
		this.reader = reader;
		this.sourceEndpoint = new Endpoint(reader.sourceInfo);
		this.targetEndpoint = new Endpoint(reader.targetInfo);
		this.propertyMapping = new PropertyMapping();
	}

	/**
	 * loads the linkspec from file
	 * 
	 * @param file
	 *            file that should be loaded
	 * @return Config Config with loaded file
	 * @throws Exception
	 *             FileNotFoundException
	 */
	public static Config loadFromFile(File file) throws Exception {
		ConfigReader reader = new ConfigReader();
		boolean success;
		try (InputStream is = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);) {
			success = reader.validateAndRead(bis, file.getPath());
		}
		if (!success) {
			throw new Exception("Error parsing config");
		}
		Config config = new Config(reader);
		config.metric = MetricParser.parse(reader.metricExpression,
				config.getSourceInfo().var.replaceAll("\\?", ""));
		config.metric.param1 = reader.acceptanceThreshold;
		config.metric.param2 = reader.verificationThreshold;
		return config;
	}

	/**
	 * saves the config to file
	 * 
	 * @param file
	 *            file in which the config should be saved
	 * @throws Exception
	 *             FileNotFoundException
	 */
	public void save(File file) throws Exception {
		if (!metric.isComplete()) {
			throw new MetricFormatException();
		}
		reader.metricExpression = metric.toString();
		reader.acceptanceThreshold = getAcceptanceThreshold();
		reader.verificationThreshold = getVerificationThreshold();
		reader.prefixes = new HashMap<String, String>();
		reader.prefixes.putAll(sourceEndpoint.getInfo().prefixes);
		reader.prefixes.putAll(targetEndpoint.getInfo().prefixes);
		ConfigWriter.saveToXML(reader, file);
	}

	/**
	 * Returns the Acceptance Threshold
	 * 
	 * @return the Acceptance Threshold
	 */
	public double getAcceptanceThreshold() {
		if (metric == null || metric.param1 == null) {
			return 1d;
		}
		return metric.param1;
	}

	/**
	 * Returns the Verification Threshold
	 * 
	 * @return Verification Threshold
	 */
	public double getVerificationThreshold() {
		if (metric == null || metric.param2 == null) {
			DecimalFormat twoDForm = new DecimalFormat("#.####");
			System.out.println("guessed verfication threshold: "
					+ (getAcceptanceThreshold() - 0.1d));
			NumberFormat format = NumberFormat.getInstance();
			Number number;
			try {
				number = format.parse(twoDForm
						.format(getAcceptanceThreshold() - 0.1d));
			} catch (Exception e) {
				System.err.println(e);
				return 0.8d;
			}
			return number.doubleValue();
		} else
			return metric.param2;
	}

	/**
	 * creates the Task in which the mappingProcess is realized
	 * 
	 * @param results
	 *            of the mapping
	 * @return null
	 */
	public Task<Void> createMappingTask(ObservableList<Result> results) {
		return new Task<Void>() {
			@Override
			protected Void call() {
				// Kopiert aus LIMES und angepasst
				Cache sourceCache = sourceEndpoint.getCache();
				Cache targetCache = targetEndpoint.getCache();
				SetConstraintsMapper mapper = SetConstraintsMapperFactory
						.getMapper(reader.executionPlan, reader.sourceInfo,
								reader.targetInfo, sourceCache, targetCache,
								new LinearFilter(), reader.granularity);
				Mapping mapping = mapper.getLinks(reader.metricExpression,
						reader.verificationThreshold);
				// get Writer ready
				mapping = mapper.getLinks(reader.metricExpression,
						getAcceptanceThreshold());
				mapping.map.forEach((sourceURI, map2) -> {
					map2.forEach((targetURI, value) -> {
						results.add(new Result(sourceURI, targetURI, value));
					});
				});
				return null;
			}
		};

	}

	/**
	 * Getter SourceEndpoint
	 * 
	 * @return SourceEndpoint
	 */
	public Endpoint getSourceEndpoint() {
		return sourceEndpoint;
	}

	/**
	 * Getter TargetEndpoint
	 * 
	 * @return TargetEndpoint
	 */
	public Endpoint getTargetEndpoint() {
		return targetEndpoint;
	}

	/**
	 * Returns the SourceInfo
	 * 
	 * @return sourceInfo
	 */
	public KBInfo getSourceInfo() {
		return reader.sourceInfo;
	}

	/**
	 * Returns the TargetInfo
	 * 
	 * @return targetInfo
	 */
	public KBInfo getTargetInfo() {
		return reader.targetInfo;
	}

	/**
	 * Returns the configReader
	 * 
	 * @return configReader
	 */
	public ConfigReader getConfigReader() {
		return reader;
	}

	/**
	 * Sets the metric to the metricExpression and source using the MetricParser
	 * 
	 * @param metricExpression
	 *            to be written to metric
	 */
	public void setMetricExpression(String metricExpression) {
		reader.metricExpression = metricExpression;
		if (metric != null) {
			double param1 = 2.0d;
			double param2 = 2.0d;
			if (metric.param1 != null)
				param1 = metric.param1;
			if (metric.param2 != null)
				param2 = metric.param2;
			metric = MetricParser.parse(metricExpression,
					getSourceInfo().var.replaceAll("\\?", ""));
			if (param1 <= 1)
				metric.param1 = param1;
			if (param2 <= 1)
				metric.param2 = param2;
		} else {
			metric = MetricParser.parse(metricExpression,
					getSourceInfo().var.replaceAll("\\?", ""));
		}
	}

	/**
	 * Sets the acceptanceThreshold
	 * 
	 * @param acceptanceThreshold
	 */
	public void setAcceptanceThreshold(double acceptanceThreshold) {
		if (metric == null)
			metric = new Output();
		metric.param1 = acceptanceThreshold;
	}

	/**
	 * Returns the property Label
	 * 
	 * @param index
	 *            Index of Porperty
	 * @param sourceOrTarget
	 *            is Source or Target
	 * @return Property String
	 */
	public String getPropertyString(int index, SourceOrTarget sourceOrTarget) {
		if (sourceOrTarget == SOURCE) {
			return getSourceInfo().var.substring(1) + "."
					+ getSourceInfo().properties.get(index);

		} else {
			return getTargetInfo().var.substring(1) + "."
					+ getTargetInfo().properties.get(index);
		}
	}

	/**
	 * Getter Metric as Output
	 * 
	 * @return Metric
	 */
	public Output getMetric() {
		return this.metric;
	}

	/**
	 * Setter PropertyMatching
	 * 
	 * @param propertyPairs
	 *            Pairs of
	 *            Properties
	 */
	public void setPropertiesMatching(ListView<String> sourcePropertiesToAdd, ListView<String> targetPropertiesToAdd) {
		List<String> sourceProperties = sourceEndpoint.getInfo().properties;
		List<String> targetProperties = targetEndpoint.getInfo().properties;
		sourceProperties.clear();
		targetProperties.clear();
		for(String sourceProp : sourcePropertiesToAdd.getItems()){
			sourceProperties.add(sourceProp);
			addFunction(sourceEndpoint, sourceProp);
		}
		for(String targetProp : targetPropertiesToAdd.getItems()){
			targetProperties.add(targetProp);
			addFunction(targetEndpoint, targetProp);
		}
	}

	private void addFunction(Endpoint endpoint, String property) {
		KBInfo info = endpoint.getInfo();
		String abbr = PrefixHelper.abbreviate(property);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(abbr, "nolang->lowercase");
		info.functions.put(abbr, map);
		
		String[] parts = property.split(":");
		String prefixToAdd = parts[0];
		String classAbbr = PrefixHelper.abbreviate(endpoint.getCurrentClass().getUri().toString());
		info.prefixes.put(prefixToAdd, PrefixHelper.getURI(prefixToAdd));
		info.restrictions.add(info.var + " " + property + " " + classAbbr);
	}
}
