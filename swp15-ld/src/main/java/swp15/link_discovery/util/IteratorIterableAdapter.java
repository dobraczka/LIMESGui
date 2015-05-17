package swp15.link_discovery.util;

import java.util.Iterator;

public class IteratorIterableAdapter<E> implements Iterable<E> {
	private Iterator<E> iterator;

	public IteratorIterableAdapter(Iterator<E> iterator) {
		this.iterator = iterator;
	}

	@Override
	public Iterator<E> iterator() {
		return iterator;
	}
}
