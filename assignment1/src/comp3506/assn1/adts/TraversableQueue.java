package comp3506.assn1.adts;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A FIFO collection CDT with iteration functionalities, part of which is based on Goodrich et al.'s 
 * singly linked list and its queue implementation [1] without some unnecessary functions.
 * Designed mainly for OneSky, the maximum amount of items is 20000 which can be easily 
 * modified or removed for other applications if required.
 * 
 * Space complexity: O(n)
 * 
 * @author Howie L. 
 *
 * @param <T> The type of element held in the data structure.
 */
public class TraversableQueue<T> implements IterableQueue<T> {
	private static final int MAX_AIRPLANE = 20000;
	private int sz = 0;
	private QueueNode<T> head = null;
	private QueueNode<T> tail = null;
	
	/**
	 * A helper node class for defining a singly linked list.
	 * 
	 * @author Goodrich et al. [1]
	 *
	 * @param <T> The type of element held in the data structure.
	 */
	private static class QueueNode<T> {
		private T element;
		private QueueNode<T> next;
		
		/**
		 * Default constructor of the node.
		 * 
		 * Time complexity: O(1)
		 * 		
		 * @param t The type of element held in the data structure.
		 * @param n The next node.
		 */
		public QueueNode(T t, QueueNode<T> n) {
			this.element= t;
			setNext(n);
		}
		
		/**
		 * Time complexity: O(1)
		 * 
		 * @return The type of element held in this node.
		 */
		public T getT() {
			return this.element;
		}
		
		/**
		 * Time complexity: O(1)
		 * 
		 * @return The next node linked to this node.
		 */
		public QueueNode<T> getNext() {
			return this.next;
		}
		
		/**
		 * Set the next node linked to this node
		 * 
		 * Time complexity: O(1)
		 * 
		 * @param n The next node.
		 */
		public void setNext(QueueNode<T> n) {
			this.next = n;
		}
	}
	
	/**
	 * Default constructor of the class, which represents an inital empty list.
	 * 
	 * Time complexity: O(1)
	 */
	public TraversableQueue() {}
	
	/**
	 * Default constructor of the class's iterator which is abstract.
	 * 
	 * Time complexity: O(1)
	 */
	@Override
	public Iterator<T> iterator() {
		return new QueueIterator();
	}
	
	/**
	 * The CDT of the queue's iterator.
	 * 
	 * @author Howie L. 
	 *
	 */
	private class QueueIterator implements Iterator<T> {
		private QueueNode<T> iteratorNode;
		private int originalSize;	// For determining special queue changes
		
		/**
		 * Default constructor. Set the pointer at the first list element.
		 * 
		 * Time complexity: O(1)
		 */
		public QueueIterator() {
			iteratorNode = head;
			originalSize = sz;
		}
		
		/**
		 * Check whether the next node is valid.
		 * 
		 * Time complexity: O(1)
		 * 
		 * @return true if a next node exists
		 */
		@Override
		public boolean hasNext() {
			if (originalSize == 0 && sz != 0) {
				iteratorNode = head;	// Special case where an iterator is created with an empty queue
			}
			return iteratorNode != null;
		}
		
		/**
		 * Time complexity: O(1)
		 * 
		 * @return the next element type stored
		 */
		@Override
		public T next() {
			if (originalSize != 0 && sz == 0) {
				iteratorNode = null;	// Special case where the queue becomes empty after creating an iterator
			}
			if (!hasNext()) {
				throw new NoSuchElementException();	// If accidental dequeuing has occured
			}
			T element = iteratorNode.getT();
			iteratorNode = iteratorNode.getNext();
			return element;
		}
	}
	
	/**
	 * Add a new element to the end of the queue. The implementation is based on Goodrich et al. [1],
	 * which is the addLast() method of a singly linked list.
	 * 
	 * Time complexity: O(1)
	 * 
	 * @param element The element to be added to the queue.
	 * @throws IllegalStateException Queue cannot accept a new element (e.g. queue space is full).
	 */
	@Override
	public void enqueue(T element) throws IllegalStateException {
		if (sz == MAX_AIRPLANE) {	// Can change this for different capacities
			throw new IllegalStateException();
		}
		QueueNode<T> qn = new QueueNode<>(element, null);
		if (isEmpty()) {
			head = qn;
		} else {
			tail.setNext(qn);
		}
		tail = qn;
		sz++;
	}
	
	/**
	 * Remove and return the element at the head of the queue. The implementation is based on Goodrich et al. 
	 * [1], which is the removeFirst() method of a singly linked list.
	 * 
	 * Time complexity: O(1)
	 * 
	 * @return Element at that was at the head of the queue.
	 * @throws IndexOutOfBoundsException Queue is empty and nothing can be dequeued.
	 */
	@Override
	public T dequeue() throws IndexOutOfBoundsException {
		if (isEmpty()) {
			throw new IndexOutOfBoundsException();
		}
		T element = head.getT();
		head = head.getNext();
		sz--;
		if (isEmpty()) {
			tail = null;
		}
		return element;
	}

	/**
	 * Time complexity: O(1)
	 * 
	 * @return Number of elements in the queue.
	 */
	@Override
	public int size() {
		return sz;
	}
	
	/**
	 * @return true if queue is empty
	 */
	private boolean isEmpty() {
		return this.sz == 0;
	}
	
}

/**
 * Design justifications:
 * This CDT is implemented with a linear singly linked list. Compared to fixed size arrays, dynamic list will always
 * consume less memory before reaching the specified capacity. However, the iterator is slightly more complex than
 * that of array since memory allocation is not contiguous and there is no native indexing. The iterator has to go
 * through the elements one by one given the next pointer information. In a queue data structure where indexing is
 * not the focus (only considering the in and out behaviours), the iterator time complexity is not worse than array.
 * A capacity limit is added, solely because of the IllegalStateException in design specifications. The flexible
 * size of a singly linked list without preallocating memory means it is not limited to the OneSky application which
 * holds 20000 airplanes at maximum.
 * 
 * References:
 * [1]	M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data structures and algorithms in Java. John Wiley & Sons, 2014.
 * 
 */
