package swp15.link_discovery.model.metric;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Operator for metric expressions allows to combine metric values
 * 
 * @author Daniel Obraczka, Sascha Hahne
 *
 */
public class Operator extends Node {
	/**
	 * Constructor
	 * 
	 * @param id
	 */
	public Operator(String id) {
		super(id);
	}

	/**
	 * operators
	 */
	public static final Set<String> identifiers = Collections
			.unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] {
					"add", "and", "diff", "max", "min", "minus", "mult", "or",
					"xor" })));

	/**
	 * returns the identifiers
	 */
	@Override
	public Set<String> identifiers() {
		return identifiers;
	}

	/**
	 * returns max number of childs
	 */
	@Override
	public byte getMaxChilds() {
		return 2;
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
}