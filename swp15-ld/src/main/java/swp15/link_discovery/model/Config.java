package swp15.link_discovery.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swp15.link_discovery.model.metric.MetricParser;
import swp15.link_discovery.model.metric.Output;
import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.cache.HybridCache;
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
 * @author Manuel Jacob, Sascha Hahne, Daniel Obraczka
 *
 */
public class Config {

	/**
	 * Reader to load linkspec
	 */
	private ConfigReader reader;

	/**
	 * Cache of the source
	 */
	private Cache sourceCache;

	/**
	 * Cache of the target
	 */
	private Cache targetCache;

	/**
	 * current Metric
	 */
	private Output metric = null;

	public PropertyMapping propertyMapping;

	/**
	 * Constructor
	 * 
	 * @param reader
	 *            LIMES-Config Reader
	 */
	private Config(ConfigReader reader) {
		this.reader = reader;
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
		return new Config(reader);
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
		throw new Exception("Not implemented yet!");
		// try (OutputStream os = new FileOutputStream(file);
		// BufferedOutputStream bos = new BufferedOutputStream(os);) {
		// }
	}

	/**
	 * realizes the mapping
	 * 
	 * @return results the results of the mapping
	 */
	public ObservableList<Result> doMapping() {
		// Kopiert aus LIMES und angepasst
		sourceCache = HybridCache.getData(reader.getSourceInfo());
		targetCache = HybridCache.getData(reader.getTargetInfo());
		SetConstraintsMapper mapper = SetConstraintsMapperFactory.getMapper(
				reader.executionPlan, reader.sourceInfo, reader.targetInfo,
				sourceCache, targetCache, new LinearFilter(),
				reader.granularity);
		Mapping mapping = mapper.getLinks(reader.metricExpression,
				reader.verificationThreshold);
		// get Writer ready
		if (reader.executionPlan.toLowerCase().startsWith("oneton"))
			mapping = mapping.getBestOneToNMapping();
		else if (reader.executionPlan.toLowerCase().startsWith("onetoone"))
			mapping = Mapping.getBestOneToOneMappings(mapping);
		ObservableList<Result> results = FXCollections.observableArrayList();
		mapping.map.forEach((sourceURI, map2) -> {
			map2.forEach((targetURI, value) -> {
				results.add(new Result(sourceURI, targetURI, value));
			});
		});
		return results;
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
	 * Returns the TargetCache
	 * 
	 * @return targetCache
	 */
	public Cache getTargetCache() {
		return targetCache;
	}

	/**
	 * Returns the SourceCache
	 * 
	 * @return sourceCache
	 */
	public Cache getSourceCache() {
		return sourceCache;
	}

	/**
	 * Sets the sourceCache
	 * 
	 * @param sC
	 *            cache to set
	 */
	public void setSourceCache(Cache sC) {
		this.sourceCache = sC;
	}

	/**
	 * Sets the targetCache
	 * 
	 * @param tC
	 *            cache to set
	 */
	public void setTargetCache(Cache tC) {
		this.targetCache = tC;
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
}
