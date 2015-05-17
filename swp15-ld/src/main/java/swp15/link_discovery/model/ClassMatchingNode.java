package swp15.link_discovery.model;

import java.net.URI;
import java.util.List;

public class ClassMatchingNode {
	private URI uri;
	private String name;
	private List<ClassMatchingNode> children;

	public ClassMatchingNode(URI uri, List<ClassMatchingNode> children) {
		this.uri = uri;
		this.name = uri.getFragment();
		this.children = children;
	}

	public URI getUri() {
		return uri;
	}

	public String getName() {
		return name;
	}

	public List<ClassMatchingNode> getChildren() {
		return children;
	}
}
