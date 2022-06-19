package comp3506.assn2.utils;

/**
 * A set data structure as a child class of the singly-linked list
 * which does not allow duplicates.
 * 
 * Space Complexity: O(N) where N is the number of elements stored
 * 
 * @author Howie L.
 *
 * @param <T> Type of element to be stored in the set.
 */
public class CustomizedSet<T> extends CustomizedList<T> {
	/**
	 * Default constructor of the set.
	 */
	public CustomizedSet() { 
		super(); 
	}
	
	/**
	 * Add an element to the collection after checking that the element
	 * does not exist.
	 * 
	 * Time Complexity: O(N) where N is the number of elements stored
	 * 
	 * @param element The element to be added to the set.
	 */
	@Override
	public void add(T element) {
		if (!this.contains(element)) {
			ListNode<T> qn = new ListNode<>(element, null);
			if (isEmpty()) {
				head = qn;
			} else {
				tail.setNext(qn);
			}
			tail = qn;
			size++;
		}
	}
}
