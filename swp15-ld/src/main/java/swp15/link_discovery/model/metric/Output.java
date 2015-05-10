package swp15.link_discovery.model.metric;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Output extends Node
{
	public Output() {super("output");}
	@Override public Set<String> identifiers()	{return new HashSet<String>(Arrays.asList(new String[] {"output"}));}
	@SuppressWarnings("unchecked")
	static public final Set<Class<? extends Node>> validChildClasses =
	Collections.unmodifiableSet(new HashSet<Class<? extends Node>>(Arrays.asList((Class<? extends Node>[])new Class[] {Measure.class,Operator.class})));
	@Override public Set<Class<? extends Node>> validChildClasses() {return validChildClasses;}
	public byte getMaxChilds() { return 1; }
	@Override public String toString()
	{
		return (getChilds().isEmpty()?"":getChilds().iterator().next().toString());//+(param1!=null?"|"+param1:"")+(param2!=null?"|"+param2:"");
	}
}