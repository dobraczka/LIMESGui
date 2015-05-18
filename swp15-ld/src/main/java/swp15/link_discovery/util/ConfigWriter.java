package swp15.link_discovery.util;

import java.io.File;
import java.io.FileOutputStream;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.uni_leipzig.simba.io.ConfigReader;
import de.uni_leipzig.simba.io.KBInfo;

/**
 * Class for saving LIMES config to file
 * 
 * @author Manuel Jacob
 */
public class ConfigWriter {
	public static void saveToXML(ConfigReader configReader, File file) {
		// Basierend auf Code aus SAIM.
		try {
			Element rootElement = new Element("LIMES");

			configReader.prefixes.forEach((prefix, namespace) -> {
				Element prefixElement = new Element("PREFIX");
				prefixElement.addContent(new Element("NAMESPACE")
						.setText(namespace));
				prefixElement.addContent(new Element("LABEL").setText(prefix));
				rootElement.addContent(prefixElement);
			});

			Element sourceElement = new Element("SOURCE");
			fillKBElement(sourceElement, configReader.sourceInfo);
			rootElement.addContent(sourceElement);
			Element targetElement = new Element("TARGET");
			fillKBElement(targetElement, configReader.targetInfo);
			rootElement.addContent(targetElement);

			rootElement.addContent(new Element("METRIC")
					.setText(configReader.metricExpression));

			Element acceptanceElement = new Element("ACCEPTANCE");
			acceptanceElement
					.addContent(new Element("THRESHOLD").setText(Double
							.toString(configReader.acceptanceThreshold)));
			acceptanceElement.addContent(new Element("FILE")
					.setText(configReader.acceptanceFile));
			acceptanceElement.addContent(new Element("RELATION")
					.setText(configReader.acceptanceRelation));
			rootElement.addContent(acceptanceElement);

			Element reviewElement = new Element("REVIEW");
			reviewElement.addContent(new Element("THRESHOLD").setText(Double
					.toString(configReader.verificationThreshold)));
			reviewElement.addContent(new Element("FILE")
					.setText(configReader.verificationFile));
			reviewElement.addContent(new Element("RELATION")
					.setText(configReader.verificationRelation));
			rootElement.addContent(reviewElement);

			rootElement.addContent(new Element("EXECUTION")
					.setText(configReader.executionPlan));
			rootElement.addContent(new Element("GRANULARITY").setText(Integer
					.toString(configReader.granularity)));
			rootElement.addContent(new Element("OUTPUT")
					.setText(configReader.outputFormat));

			Document document = new Document(rootElement, new DocType("LIMES",
					"limes.dtd"));
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			out.output(document, new FileOutputStream(file, false));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static void fillKBElement(Element element, KBInfo kb) {
		element.addContent(new Element("ID").setText(kb.id));
		element.addContent(new Element("ENDPOINT").setText(kb.endpoint));
		element.addContent(new Element("GRAPH").setText(kb.graph));
		element.addContent(new Element("VAR").setText(String.valueOf(kb.var)));
		element.addContent(new Element("PAGESIZE").setText(String
				.valueOf(kb.pageSize)));
		for (String restriction : kb.restrictions) {
			if (restriction != null && restriction.trim().length() > 0) {
				element.addContent(new Element("RESTRICTION")
						.setText(restriction));
			}
		}
		for (String property : kb.properties) {
			if (property != null && property.trim().length() > 0) {
				element.addContent(new Element("PROPERTY").setText(property));
			}
		}
		if (kb.type != null && kb.type.length() > 0) {
			element.addContent(new Element("TYPE").setText(kb.type));
		}
	}
}
