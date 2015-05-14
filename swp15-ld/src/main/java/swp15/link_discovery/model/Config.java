package swp15.link_discovery.model;

import static swp15.link_discovery.util.SourceOrTarget.SOURCE;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import swp15.link_discovery.model.metric.MetricParser;
import swp15.link_discovery.model.metric.Output;
import swp15.link_discovery.util.SourceOrTarget;
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
 * @author Manuel Jacob, Sascha Hahne, Daniel Obraczka, Felix Brei
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

	public Config() {
		reader = new ConfigReader();
		reader.sourceInfo = new KBInfo();
		reader.targetInfo = new KBInfo();
	}

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
		// throw new Exception("Not implemented yet!");
		try {
			Document document = null;
			// document = new SAXBuilder().build(getClass().getClassLoader()
			// .getResourceAsStream("template.xml.template"));
			SAXBuilder builder = new SAXBuilder();
			document = builder
					.build(new File("resource/template.xml.template"));
			Element rootElement = document.getRootElement();
			{ // Prefixes
				Map<String, String> prefixes = new HashMap<String, String>(
						reader.sourceInfo.prefixes);
				prefixes.putAll(reader.targetInfo.prefixes);
				int i = 0;
				for (String prefix : prefixes.keySet()) {
					Element prefixElement;
					rootElement.addContent(i++, prefixElement = new Element(
							"PREFIX"));
					prefixElement.addContent(new Element("NAMESPACE")
							.setText(prefixes.get(prefix)));
					prefixElement.addContent(new Element("LABEL")
							.setText(prefix));
				}
			}
			Element sourceElement = rootElement.getChild("SOURCE");
			sourceElement.removeChildren("RESTRICTION");
			sourceElement.removeChildren("PROPERTY");
			Element targetElement = rootElement.getChild("TARGET");
			targetElement.removeChildren("RESTRICTION");
			targetElement.removeChildren("PROPERTY");
			fillKBElement(sourceElement, reader.sourceInfo);
			fillKBElement(targetElement, reader.targetInfo);

			if (metric != null) {
				rootElement.getChild("METRIC").setText(metric.toString());
				System.out.println(metric.toString());
			}

			{
				Element acceptanceElement = rootElement.getChild("ACCEPTANCE");
				acceptanceElement.getChild("FILE").setText(
						reader.sourceInfo.endpoint + '-'
								+ reader.targetInfo.endpoint + "-accept");
				acceptanceElement.getChild("THRESHOLD").setText(
						Double.toString(getAcceptanceThreshold()));
			}
			{
				Element reviewElement = rootElement.getChild("REVIEW");

				reviewElement.getChild("FILE").setText(
						reader.sourceInfo.endpoint + '-'
								+ reader.targetInfo.endpoint + "-review");
				reviewElement.getChild("THRESHOLD").setText(
						Double.toString(getVerificationThreshold()));

			}

			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			out.output(document, new FileOutputStream(file, false));

			// getElementById("/LIMES/SOURCE/VAR")
		} catch (Exception e) {
			System.err.println();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param element
	 *            Element to be filled
	 * @param kb
	 *            Source of information
	 */
	private void fillKBElement(Element element, KBInfo kb) {
		if (kb == null)
			return;
		element.getChild("ID").setText(kb.id);
		element.getChild("ENDPOINT").setText(kb.endpoint);
		element.getChild("GRAPH").setText(kb.graph);
		element.getChild("VAR").setText(String.valueOf(kb.var));
		element.getChild("PAGESIZE").setText(String.valueOf(kb.pageSize));
		for (String restriction : kb.restrictions) {
			if (restriction != null && restriction.trim().length() > 0) {
				Element restrictionElement = new Element("RESTRICTION");

				restrictionElement.setText(restriction);
				element.addContent(restrictionElement);
			}
		}
		for (String property : kb.properties) {
			if (property != null && property.trim().length() > 0) {
				Element propertyElement = new Element("PROPERTY");
				propertyElement.setText(property);
				System.out.println("Adding property " + propertyElement);
				element.addContent(propertyElement);
			}
		}
		if (kb.type != null && kb.type.length() > 0) {
			Element type = new Element("TYPE");
			type.setText(kb.type);
			element.addContent(type);
		}
	}

	public double getAcceptanceThreshold() {
		if (metric == null || metric.param1 == null) {
			return 1d;
		}
		return metric.param1;
	}

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

	public String getPropertyString(int index, SourceOrTarget sourceOrTarget) {
		if (sourceOrTarget == SOURCE) {
			return getSourceInfo().var.substring(1) + "."
					+ getSourceInfo().properties.get(index);

		} else {
			return getTargetInfo().var.substring(1) + "."
					+ getTargetInfo().properties.get(index);
		}
	}
}
