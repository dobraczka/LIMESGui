package swp15.link_discovery.model.metric;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import swp15.link_discovery.model.metric.Property.Origin;

public class Measure extends Node
{
	public Measure(String id) {super(id);}
	public static final Set<String> identifiers = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
			new String[] {"cosine","euclidean","jaccard","levenshtein","overlap","trigrams" })));

	@Override public Set<String> identifiers()	{return identifiers;}
	@Override public byte getMaxChilds() {return 2;}

	static public final Set<Class<? extends Node>> validChildClasses =
			Collections.<Class<? extends Node>>singleton(Property.class);
	@Override public Set<Class<? extends Node>> validChildClasses() {return validChildClasses;}


	@Override public boolean acceptsChild(Node node)
	{
		synchronized(this)
		{
			synchronized (node)
			{
				return super.acceptsChild(node)&&
						(getChilds().isEmpty()
								||((Property)getChilds().iterator().next()).origin!=((Property)node).origin);
			}
		}
	}

	@Override
	public Acceptance acceptsChildWithReason(Node node)
	{
		synchronized(this)
		{
			synchronized(node)
			{
				Acceptance acceptance = super.acceptsChildWithReason(node);
				if(acceptance!=Acceptance.OK) {return acceptance;}
				if(!acceptsChild(node))
				{
					if((((Property)getChilds().iterator().next()).origin==Origin.SOURCE)) {return Acceptance.TARGET_PROPERTY_EXPECTED;}
					return Acceptance.SOURCE_PROPERTY_EXPECTED;
				}
				return Acceptance.OK;
			}
		}
	}
}
