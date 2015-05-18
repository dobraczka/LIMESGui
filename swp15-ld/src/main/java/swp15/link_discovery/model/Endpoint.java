package swp15.link_discovery.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;

import de.konrad.commons.sparql.PrefixHelper;
import de.konrad.commons.sparql.SPARQLHelper;
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

	public ClassMatchingNode getCurrentClass() {
		return currentClass;
	}

	public void setCurrentClass(ClassMatchingNode currentClass) {
		this.currentClass = currentClass;

		String currentClassAsString = currentClass.getUri().toString();
		PrefixHelper.generatePrefix(currentClassAsString);
		String base = PrefixHelper.getBase(currentClassAsString);
		String prefix = PrefixHelper.getPrefix(base);
		info.prefixes.put(prefix, PrefixHelper.getURI(prefix));
	}

	/**
	 * Get classes for matching.
	 */
	public List<ClassMatchingNode> getClassesForMatching() {
		Set<String> rootClasses = SPARQLHelper.rootClasses(info.endpoint,
				info.graph, model);
		List<ClassMatchingNode> result = getClassMatchingNodes(rootClasses);
		return result;
	}

	private List<ClassMatchingNode> getClassMatchingNodes(Set<String> classes) {
		List<ClassMatchingNode> result = new ArrayList<ClassMatchingNode>();
		for (String class_ : classes) {
			try {
				List<ClassMatchingNode> children = getClassMatchingNodes(SPARQLHelper
						.subclassesOf(info.endpoint, info.graph, class_, model));
				result.add(new ClassMatchingNode(new URI(class_), children));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public List<String> getPossibleProperties() {
		List<String> result = new ArrayList<String>();
		for (String property : SPARQLHelper.properties(info.endpoint,
				info.graph, currentClass.getUri().toString(), model)) {
			result.add(PrefixHelper.abbreviate(property));
		}
		return result;
	}
}
