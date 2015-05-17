package de.uni_leipzig.simba.util;

import java.io.Serializable;
import java.util.Vector;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import de.uni_leipzig.simba.io.KBInfo;
/**
 * Subclass of KBInfo that adds a view convenience methods such as different constructors and better error messages.
 * Also contains additional fields like the classes so that you can't downgrade AdvancedKBInfo to KBInfo without some information losses.
 * @author Konrad Höffner */
public class AdvancedKBInfo extends KBInfo implements Serializable
{
	//public String var = "?var";
	public List<String> classes = new LinkedList<String>();

	/** Creates a copy of a KBInfo instance and returns it as AdvancedKBInfo
	 * @param kb the original KBInfo*/
	public AdvancedKBInfo(KBInfo kb)
	{
		this.id = kb.id;
		this.endpoint = kb.endpoint;
		this.graph = kb.graph;
		this.var = kb.var;
		this.properties = kb.properties;
		this.restrictions = kb.restrictions;
		this.functions = kb.functions;
		this.prefixes = kb.prefixes;
		this.pageSize = kb.pageSize;
		this.type = kb.type;
	}

	public AdvancedKBInfo(String id, String endpoint, String var, String graph,
			List<String> properties, Collection<Restriction> restrictions,
			HashMap<String, String> prefixes, int pageSize)
	{
		super();
		this.id = id;
		this.endpoint = endpoint;
		this.var  = var;
		this.graph = graph;
		this.properties = properties;
		if(restrictions!=null)
		{
			for(Restriction restriction: restrictions)
			{
				String p = restriction.getProperty();
				if(p.equals("a")||p.equals("rdf:type")||p.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {classes.add(restriction.getObject().replace("<","" ).replace(">",""));}
				this.restrictions.add(restriction.toString(var));
			}
		}
		this.prefixes = prefixes;
		this.pageSize = pageSize;
	}

	/** Creates an instance of this class without restrictions.**/
	public AdvancedKBInfo(String id, String endpoint,String var,String graph)
	{
		this(id,endpoint,var,graph,Collections.<String>emptyList(),
				null,
				new HashMap<String,String>(),1000);
	}
	/** Creates an instance of this class with a single restriction property and object.**/
	public AdvancedKBInfo(String id, String endpoint,String var,String graph, String restrictionProperty, String restrictionObject)
	{
		this(id,endpoint,var,graph,Collections.<String>emptyList(),
				Collections.singleton(new Restriction(restrictionProperty, restrictionObject)),
				new HashMap<String,String>(),1000);
	}

	//	/** Creates an instance of this class with a single restriction property and object. The variable is set to "?var".**/
	//	public AdvancedKBInfo(String id, String endpoint, String graph, String restrictionProperty, String restrictionObject)
	//	{
	//		this(id,endpoint,"?var",graph,restrictionProperty,restrictionObject);
	//	}

	/** Example: "rdfs:label" -> "http://www.w3.org/2000/01/rdf-schema#label". **/
	// TODO: move somewhere else?
	public static String expandPrefix(String url, Map<String,String> prefixes)
	{
		if(url.startsWith("http")||url.startsWith("<")) return url;
		for(String prefix: prefixes.keySet())
		{
			if(url.startsWith(prefix+':'))
			{
				return '<'+url.replace(prefix+':', prefixes.get(prefix))+'>';
			}
		}
		return url;
	}

	/** Like {@link expandPrefix(String,Map<String,String>)} but does this for all properties in the knowledge base.
	 * @param prefixes A map with prefixes as keys and their corresponding full uris as values.<br/>
	 * Example: map.put("rdfs","http://www.w3.org/2000/01/rdf-schema#");**/
	// TODO: move somewhere else?
	public void expandPrefixes(Map<String,String> prefixes)
	{
		List<String> newProperties = new Vector<String>();
		for(String property: properties)
		{
			newProperties.add(expandPrefix(property,prefixes));
		}
		this.properties = newProperties;
		List<Restriction> newRestrictions = new Vector<Restriction>();
		for(String restrictionStr: restrictions)
		{
			Restriction restriction = Restriction.fromString(restrictionStr);
			String newProperty = expandPrefix(restriction.getProperty(),prefixes);
			String newObject = expandPrefix(restriction.getObject(),prefixes);
			newRestrictions.add(new Restriction(newProperty, newObject));
		}
		this.restrictions.clear();
		for(Restriction restriction : newRestrictions)
		{
			restrictions.add(restriction.toString(var));
		}
	}

	// TODO: necessary?
	public String getRestrictionQuerySubstring()
	{
		StringBuffer where = new StringBuffer();
		for (int i = 0; i < this.restrictions.size(); i++)
		{
			where.append(this.restrictions.get(i)+".\n");
		}
		return where.toString().substring(0, where.length()-1);
	}

}
