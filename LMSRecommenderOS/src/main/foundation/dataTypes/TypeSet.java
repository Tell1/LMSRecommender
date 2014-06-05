package main.foundation.dataTypes;

import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jcolibri.connector.TypeAdaptor;
import main.domain.CMS.LearningObject;

/**
 * Abstract class that represents a set extending the TypeAdaptor class to be
 * able to used in the connecters of jColibri.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 */
public abstract class TypeSet<E extends LearningObject> extends AbstractSet<E>
		implements TypeAdaptor {

	Set<E> set = new HashSet<E>();

	public TypeSet(Set<E> set) {
		super();
		this.set = set;
	}

	public TypeSet() {
		super();
	}

	public Set<E> getSet() {
		return set;
	}

	public void setSet(Set<E> set) {
		this.set = set;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((set == null) ? 0 : set.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeSet<?> other = (TypeSet<?>) obj;
		if (set == null) {
			if (other.set != null)
				return false;
		} else if (other.set.isEmpty()) {
			return false;
		} else if (!set.contains(other.set.iterator().next()))
			return false;
		return true;
	}

	/**
	 * Returns an iterator over the elements in this set. The elements are
	 * returned in no particular order.
	 * 
	 * @return an Iterator over the elements in this set
	 * @see ConcurrentModificationException
	 */
	@Override
	public Iterator<E> iterator() {
		return set.iterator();
	}

	/**
	 * Returns the number of elements in this set (its cardinality).
	 * 
	 * @return the number of elements in this set (its cardinality)
	 */
	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean add(E e) {
		return set.add(e);
	}

	@Override
	public String toString() {
		return set.toString();
	}

}
