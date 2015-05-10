package swp15.link_discovery.model.metric;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Output of metric
 * 
 * @author Daniel Obraczka, Sascha Hahne
 *
 */
public class Output extends Node {

	/**
	 * Constructor
	 */
	public Output() {
		super("output");
	}

	/**
	 * returns HashSet of identifiers
	 * 
	 * @return identifiers
	 */
	@Override
	public Set<String> identifiers() {
		return new HashSet<String>(Arrays.asList(new String[] { "output" }));
	}

	/**
	 * unmodifiable HashSet of validChildClasses
	 */
	@SuppressWarnings("unchecked")
	static public final Set<Class<? extends Node>> validChildClasses = Collections
			.unmodifiableSet(new HashSet<Class<? extends Node>>(Arrays
					.asList((Class<? extends Node>[]) new Class[] {
							Measure.class, Operator.class })));

	/**
	 * returns validChildClasses
	 * 
	 * @return validChildClasses
	 */
	@Override
	public Set<Class<? extends Node>> validChildClasses() {
		return validChildClasses;
	}

	/**
	 * returns maxChilds
	 * 
	 * @return 1
	 */
	public byte getMaxChilds() {
		return 1;
	}

	/**
	 * Output to String
	 */
	@Override
	public String toString() {
		return (getChilds().isEmpty() ? "" : getChilds().iterator().next()
				.toString());// +(param1!=null?"|"+param1:"")+(param2!=null?"|"+param2:"");
	}
}