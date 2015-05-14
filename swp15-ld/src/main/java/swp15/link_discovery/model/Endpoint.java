package swp15.link_discovery.model;

import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.cache.HybridCache;
import de.uni_leipzig.simba.io.KBInfo;

public class Endpoint {
	private KBInfo info;
	private Cache cache;

	public Endpoint(KBInfo sourceInfo) {
		this.info = sourceInfo;

	}

	public Cache getCache() {
		if (cache == null) {
			cache = HybridCache.getData(info);
		}
		return cache;
	}
}
