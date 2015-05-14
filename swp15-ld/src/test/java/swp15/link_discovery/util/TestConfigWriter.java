package swp15.link_discovery.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.uni_leipzig.simba.io.ConfigReader;
import de.uni_leipzig.simba.io.KBInfo;

public class TestConfigWriter {
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	private Random random = new Random();

	@Test
	public void testRoundtrip() throws Exception {
		// Create config reader and fill it with random values.
		ConfigReader randomConfigReader = new ConfigReader();
		randomConfigReader.acceptanceFile = randomString();
		randomConfigReader.acceptanceRelation = randomString();
		randomConfigReader.acceptanceThreshold = random.nextDouble();
		randomConfigReader.executionPlan = randomString();
		randomConfigReader.exemplars = random.nextInt();
		randomConfigReader.granularity = random.nextInt();
		randomConfigReader.metricExpression = randomString();
		randomConfigReader.outputFormat = randomString();
		randomConfigReader.prefixes = new HashMap<String, String>();
		randomConfigReader.prefixes.put(randomString(), randomString());
		randomConfigReader.sourceInfo = createRandomKBInfo();
		randomConfigReader.targetInfo = createRandomKBInfo();
		randomConfigReader.verificationFile = randomString();
		randomConfigReader.verificationRelation = randomString();
		randomConfigReader.verificationThreshold = random.nextDouble();

		// Save config reader to temporary file.
		File tempFile = tempFolder.newFile();
		ConfigWriter.saveToXML(randomConfigReader, tempFile);

		// Create config reader and read from temporary file.
		ConfigReader compareConfigReader = new ConfigReader();
		boolean valid = compareConfigReader.validateAndRead(tempFile.getPath());
		assertTrue(valid);

		// Check if they equal.
		assertEquals(randomConfigReader.acceptanceFile,
				compareConfigReader.acceptanceFile);
		assertEquals(randomConfigReader.acceptanceRelation,
				compareConfigReader.acceptanceRelation);
		assertEquals(randomConfigReader.acceptanceThreshold,
				compareConfigReader.acceptanceThreshold, 0.0);
		assertEquals(randomConfigReader.executionPlan,
				compareConfigReader.executionPlan);
		// This field is not present in DTD.
		// assertEquals(randomConfigReader.exemplars,
		// compareConfigReader.exemplars);
		assertEquals(randomConfigReader.granularity,
				compareConfigReader.granularity);
		assertEquals(randomConfigReader.metricExpression,
				compareConfigReader.metricExpression);
		assertEquals(randomConfigReader.outputFormat,
				compareConfigReader.outputFormat);
		assertEquals(randomConfigReader.prefixes, compareConfigReader.prefixes);
		checkKBInfo(randomConfigReader.sourceInfo,
				compareConfigReader.sourceInfo);
		checkKBInfo(randomConfigReader.targetInfo,
				compareConfigReader.targetInfo);
		assertEquals(randomConfigReader.verificationFile,
				compareConfigReader.verificationFile);
		assertEquals(randomConfigReader.verificationRelation,
				compareConfigReader.verificationRelation);
		assertEquals(randomConfigReader.verificationThreshold,
				compareConfigReader.verificationThreshold, 0.0);
	}

	private KBInfo createRandomKBInfo() {
		KBInfo kbInfo = new KBInfo();
		kbInfo.endpoint = randomString();
		// kbInfo.functions is build from the list of properties (see below in
		// checkKBInfo()).
		kbInfo.graph = randomString();
		kbInfo.id = randomString();
		kbInfo.pageSize = random.nextInt();
		kbInfo.properties = new ArrayList<String>();
		kbInfo.properties.add(randomString());
		kbInfo.restrictions = new ArrayList<String>();
		kbInfo.restrictions.add(randomString());
		kbInfo.type = randomString();
		kbInfo.var = randomString();
		return kbInfo;
	}

	private void checkKBInfo(KBInfo expected, KBInfo actual) {
		Map<String, Map<String, String>> expectedFunctions = new HashMap<String, Map<String, String>>();
		Map<String, String> function = new HashMap<String, String>();
		function.put(expected.properties.get(0), "");
		expectedFunctions.put(expected.properties.get(0), function);

		assertEquals(expected.endpoint, actual.endpoint);
		assertEquals(expectedFunctions, actual.functions);
		assertEquals(expected.graph, actual.graph);
		assertEquals(expected.id, actual.id);
		assertEquals(expected.pageSize, actual.pageSize);
		assertEquals(expected.properties, actual.properties);
		assertEquals(expected.restrictions, actual.restrictions);
		assertEquals(expected.type, actual.type);
		assertEquals(expected.var, actual.var);
	}

	private String randomString() {
		return Long.toHexString(random.nextLong());
	}
}
