package swp15.link_discovery.model.metric;

import java.util.Collections;
import java.util.Set;

import lombok.Getter;

public class Property extends Node
{
	@Override public Set<String> identifiers()	{return Collections.<String>emptySet();}
	public byte getMaxChilds() {return 0;}
	public enum Origin {SOURCE,TARGET};
	@Getter protected final Origin origin;
	@Override public Set<Class<? extends Node>> validChildClasses() {return Collections.emptySet();}
	@Override public boolean acceptsChild(Node n)	{return false;}

	
	public Property(String id, Origin origin)
	{
		super(id);
		String regex = "\\w+\\.\\w+:?\\w+";
		if(!id.matches(regex)) throw new MetricFormatException("id \""+id+"\" does not confirm to the regex "+regex);
		this.origin=origin;
	}
}