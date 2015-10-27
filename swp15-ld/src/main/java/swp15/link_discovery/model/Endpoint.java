package swp15.link_discovery.model;

import com.hp.hpl.jena.rdf.model.Model;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.query.ModelRegistry;
import de.uni_leipzig.simba.query.QueryModuleFactory;

/**
 * Represents an LIMES endpoint
 * 
 * @author Manuel Jacob
 */
public class Endpoint {
	private KBInfo info;
	private Cache cache;
	private Model model;
	private ClassMatchingNode currentClass;

	public Endpoint(KBInfo info) {
		this.info = info;
		update();
	}

	public void update() {
		if (info.endpoint == null) {
			return;
		}
		String fileType = info.endpoint.substring(info.endpoint
				.lastIndexOf(".") + 1);
		QueryModuleFactory.getQueryModule(fileType, info);
		model = ModelRegistry.getInstance().getMap().get(info.endpoint);
	}

	public KBInfo getInfo() {
		return info;
	}

	public Cache getCache() {
		if (cache == null) {
			cache = HybridCache.getData(info);
		}
		return cache;
	}

	public GetClassesTask createGetClassesTask() {
		return new GetClassesTask(info, model);
	}

	public ClassMatchingNode getCurrentClass() {
		return currentClass;
	}

	public void setCurrentClass(ClassMatchingNode currentClass) {
		this.currentClass = currentClass;
		info.prefixes.clear();
		info.restrictions.clear();

		if (currentClass == null) {
			return;
		}

		String currentClassAsString = currentClass.getUri().toString();
		String[] abbr = PrefixHelper.generatePrefix(currentClassAsString);
		info.prefixes.put(abbr[0], abbr[1]);

		info.prefixes.put("rdf", PrefixHelper.getURI("rdf"));
		String classAbbr = PrefixHelper.abbreviate(currentClassAsString);
		info.restrictions.add(info.var + " rdf:type " + classAbbr);
		
//		info.prefixes.put("rdfs", PrefixHelper.getURI("rdfs"));
//		info.restrictions.add(info.var + " rdfs:label " + classAbbr);
	}

	public GetPropertiesTask createGetPropertiesTask() {
		return new GetPropertiesTask(info, model, currentClass);
	}
}
