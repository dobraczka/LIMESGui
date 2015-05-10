package swp15.link_discovery.model.metric;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
public class Operator extends Node
{
	public Operator(String id) {super(id);}
	public static final Set<String> identifiers =
			Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] {"add","and","diff","max","min","minus","mult","or","xor"})));
	@Override public Set<String> identifiers()	{return identifiers;}
	@Override public byte getMaxChilds() {return 2;}
	@SuppressWarnings("unchecked")
	static public final Set<Class<? extends Node>> validChildClasses =
	Collections.unmodifiableSet(new HashSet<Class<? extends Node>>(Arrays.asList((Class<? extends Node>[])new Class[] {Measure.class,Operator.class})));
	@Override public Set<Class<? extends Node>> validChildClasses() {return validChildClasses;}
}