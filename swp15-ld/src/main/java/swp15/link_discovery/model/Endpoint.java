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

		if (currentClass == null) {
			return;
		}
		String currentClassAsString = currentClass.getUri().toString();
		PrefixHelper.generatePrefix(currentClassAsString);
		String base = PrefixHelper.getBase(currentClassAsString);
		String prefix = PrefixHelper.getPrefix(base);
		info.prefixes.put(prefix, PrefixHelper.getURI(prefix));
	}

	public GetPropertiesTask createGetPropertiesTask() {
		return new GetPropertiesTask(info, model, currentClass);
	}
}
