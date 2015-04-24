package swp15.link_discovery.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.filter.LinearFilter;
import de.uni_leipzig.simba.io.ConfigReader;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.mapper.SetConstraintsMapper;
import de.uni_leipzig.simba.mapper.SetConstraintsMapperFactory;

public class Config {
	private ConfigReader reader;

	private Config(ConfigReader reader) {
		this.reader = reader;
	}

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

	public void save(File file) throws Exception {
		throw new Exception("Not implemented yet.");
		// try (OutputStream os = new FileOutputStream(file);
		// BufferedOutputStream bos = new BufferedOutputStream(os);) {
		// }
	}

	public ObservableList<Result> doMapping() {
		// Kopiert aus LIMES und angepasst
		Cache source = HybridCache.getData(reader.getSourceInfo());
		Cache target = HybridCache.getData(reader.getTargetInfo());
		SetConstraintsMapper mapper = SetConstraintsMapperFactory.getMapper(
				reader.executionPlan, reader.sourceInfo, reader.targetInfo,
				source, target, new LinearFilter(), reader.granularity);
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
	public KBInfo getSourceInfo(){
		return reader.sourceInfo;
	}
	public KBInfo getTargetInfo(){
		return reader.targetInfo;
	}
	
}
