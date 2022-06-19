package comp3506.assn2.utils;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A singly-list-like data structure inspired by Goodrich et al. [1] that is
 * adopted from Assignment 1's queue. This is used as the alternative of
 * JCF lists for non-AutoTester modules and only necessary methods are
 * implemented.
 * 
 * Space Complexity: O(N) where N is the number of elements stored
 * 
 * @author Howie L.
 *
 * @param <T> Type of data stored
 */
public class CustomizedList<T> implements Iterable<T> {
	protected int size = 0;
	protected ListNode<T> head = null;
	protected ListNode<T> tail = null;
	
	/**
	 * Constructor to create an empty list.
	 * (from Assignment 1)
	 */
	public CustomizedList() {}
	
	/**
	 * Data structure of a list "unit".
	 * (from Assignment 1)
	 * 
	 * @author Howie L.
	 *
	 * @param <T> Element type to contain in the node.
	 */
	protected static class ListNode<T> {
		private T element;
		private ListNode<T> next;
		
		/**
		 * Default constructor of the node.
		 * (from Assignment 1)
		 * 
		 * Time complexity: O(1)
		 * 		
		 * @param t The type of element held in the data structure.
		 * @param n The next node.
		 */
		public ListNode(T t, ListNode<T> n) {
			this.element= t;
			setNext(n);
		}
		
		/**
		 * (from Assignment 1)
		 * 
		 * Time complexity: O(1)
		 * 
		 * @return The type of element held in this node.
		 */
		public T getElement() {
			return this.element;
		}
		
		/**
		 * (from Assignment 1)
		 * 
		 * Time complexity: O(1)
		 * 
		 * @return The next node linked to this node.
		 */
		public ListNode<T> getNext() {
			return this.next;
		}
		
		/**
		 * Set the next node linked to this node.
		 * (from Assignment 1)
		 * 
		 * Time complexity: O(1)
		 * 
		 * @param n The next node.
		 */
		public void setNext(ListNode<T> n) {
			this.next = n;
		}
	}
	
	/**
	 * Default constructor of the class's iterator. 
	 * (from Assignment 1)
	 * 
	 * Time complexity: O(1)
	 */
	@Override
	public Iterator<T> iterator() {
		return new ListIterator();
	}
	
	/**
	 * The CDT of the list's iterator. 
	 * (from Assignment 1)
	 * 
	 * @author Howie L. 
	 *
	 */
	protected class ListIterator implements Iterator<T> {
		private ListNode<T> iteratorNode;
		private int originalSize;	// For determining special list changes
		
		/**
		 * Default constructor which sets the pointer at the first list element.
		 * (from Assignment 1)
		 * 
		 * Time complexity: O(1)
		 */
		public ListIterator() {
			iteratorNode = head;
			originalSize = size;
		}
		
		/**
		 * Check whether the next node is valid.
		 * (from Assignment 1)
		 * 
		 * Time complexity: O(1)
		 * 
		 * @return true if a next node exists
		 */
		@Override
		public boolean hasNext() {
			if (originalSize == 0 && size != 0) {
				iteratorNode = head;	// Special case where an iterator is created with an empty list
			}
			return iteratorNode != null;
		}
		
		/**
		 * Time complexity: O(1)
		 * (from Assignment 1)
		 * 
		 * @return the next element type stored
		 */
		@Override
		public T next() {
			if (originalSize != 0 && size == 0) {
				iteratorNode = null;	// Special case where the list becomes empty after creating an iterator
			}
			if (!hasNext()) {
				throw new NoSuchElementException();	// If accidental dequeuing has occurred
			}
			T element = iteratorNode.getElement();
			iteratorNode = iteratorNode.getNext();
			return element;
		}
	}
	
	/**
	 * Add a new element to the end of the list. The implementation is based on Goodrich et al. [1].
	 * (from Assignment 1)
	 * 
	 * Time complexity: O(1)
	 * 
	 * @param element The element to be added to the list.
	 */
	public void add(T element) {
		ListNode<T> qn = new ListNode<>(element, null);
		if (isEmpty()) {
			head = qn;
		} else {
			tail.setNext(qn);
		}
		tail = qn;
		size++;
	}
	
	/**
	 * Check whether the list contains a particular element.
	 * 
	 * Time complexity: O(N) where N is the number of elements stored.
	 * 
	 * @param element The element to check
	 * @return true if the list contains that
	 */
	public boolean contains(T element) {
		for (T t : this) {
			if (t.equals(element)) return true;
		}
		return false;
	}
	
	/**
	 * Add all elements from another compatible list.
	 * 
	 * Time complexity: O(N) where N is the number of elements of l.
	 * 
	 * @param l The list to concatenate
	 */
	public void addAll(CustomizedList<T> l) {
		for (T element : l) {
			this.add(element);
		}
	}
	
	/**
	 * (from Assignment 1)
	 * 
	 * @return true if list is empty
	 */
	protected boolean isEmpty() {
		return this.size == 0;
	}
	
	/**
	 * (from Assignment 1)
	 * 
	 * @return size of list
	 */
	public int size() {
		return this.size;
	}
	
	/**
	 * Convert this linked list to an array.
	 * 
	 * Time complexity: O(N) where N is the number of elements stored.
	 * 
	 * @return The array created
	 */
	@SuppressWarnings("unchecked")
	public T[] toArray() {
		if (this.isEmpty()) {
			return null;
		}
		
		// Note that T[] array = (T[]) new Object[size] does not seem to work for Pair and Triple.
		T[] array = (T[]) Array.newInstance(this.tail.getElement().getClass(), this.size);
		int idx = 0;
		for (T element : this) {
			array[idx] = element;
			idx++;
		}
		return array;
	}
}


/**
 * References:
 * [1]	M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data structures and algorithms in Java. John Wiley & Sons, 2014.
 */