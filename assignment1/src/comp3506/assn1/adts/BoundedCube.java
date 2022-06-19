package comp3506.assn1.adts;

/**
 * A three-dimensional data structure that holds items in a positional relationship to each other.
 * Each cell in the data structure can hold multiple items.
 * A bounded cube has a specified maximum size in each dimension.
 * The root of each dimension is indexed from zero.
 * 
 * Space complexity: O(n), since the private helper class accounts for 4*n, methods returning or
 * removing all elements account for 2*n, and the rest are constant.
 * 
 * @author Howie L. 
 *
 * @param <T> The type of element held in the data structure.
 */
public class BoundedCube<T> implements Cube<T> {
	private IterableQueue<QuadTuple<T>> planesNCoords;
	private int max_X;
	private int max_Y;
	private int max_Z;
	
	/**
	 * Default constructor of the BoundedCube CDT.
	 * 
	 * Time complexity: O(1), because it only checks and sets x, y and z limits of a
	 * queue initialised to empty.
	 * 
	 * @param length  Maximum size in the 'x' dimension.
	 * @param breadth Maximum size in the 'y' dimension.
	 * @param height  Maximum size in the 'z' dimension.
	 * @throws IllegalArgumentException If provided dimension sizes are not positive.
	 */
	public BoundedCube(int length, int breadth, int height) throws IllegalArgumentException {
		if (length <= 0 || breadth <= 0 || height <= 0) {
			throw new IllegalArgumentException();
		}
		this.max_X = length;
		this.max_Y = breadth;
		this.max_Z = height;
		this.planesNCoords = new TraversableQueue<QuadTuple<T>>();
	}
	
	/**
	 * A private helper data structure which stores an object and its three-dimensional 
	 * coordinates in a tuple-like form. Note that the class does not provide x, y and
	 * z value retrieval (get) functions because they are not necessary for implementing 
	 * the parent class.
	 * 
	 * Space complexity: O(1)
	 * 
	 * @author Howie L.
	 *
	 * @param <T> The type of element held in the data structure.
	 */
	private static class QuadTuple<T> {
		private int x_coord;
		private int y_coord;
		private int z_coord;
		private T element;
		
		/**
		 * Default constructor of the helper CDT.
		 * 
		 * Time complexity: O(1)
		 * 		
		 * @param x X coordinate of the position of the element.
		 * @param y Y coordinate of the position of the element.
		 * @param z Z coordinate of the position of the element.
		 * @param t The type of element held at (x, y, z).
		 */
		public QuadTuple(int x, int y, int z, T t) {
			setX(x);
			setY(y);
			setZ(z);
			setT(t);
		}
		
		/**
		 * Set the x coordinate of this tuple.
		 * 
		 * Time complexity: O(1)
		 * 
		 * @param x X coordinate of the position of the element.
		 */
		public void setX(int x) {
			this.x_coord = x;
		}
		
		/**
		 * Set the y coordinate of this tuple.
		 * 
		 * Time complexity: O(1)
		 * 
		 * @param y Y coordinate of the position of the element.
		 */
		public void setY(int y) {
			this.y_coord = y;
		}
		
		/**
		 * Set the z coordinate of this tuple.
		 * 
		 * Time complexity: O(1)
		 * 
		 * @param z Z coordinate of the position of the element.
		 */
		public void setZ(int z) {
			this.z_coord = z;
		}
		
		/**
		 * Set the element (4th entry) of this tuple.
		 * 
		 * Time complexity: O(1)
		 * 
		 * @param t The type of element to be substituted into.
		 */
		public void setT(T t) {
			this.element = t;
		}
		
		/**
		 * Retrieve the element (4th entry) of this tuple.
		 * 
		 * Time complexity: O(1)
		 * 
		 * @return The type of element held.
		 */
		public T getT() {
			return this.element;
		}
		
		/**
		 * Check if a set of coordinates match this tuple (1st-3rd entries).
		 * 
		 * Time complexity: O(1)
		 * 
		 * @param x X coordinate
		 * @param y Y coordinate
		 * @param z Z coordinate
		 * @return true if match
		 */
		public boolean coordsMatch(int x, int y, int z) {
			if (this.x_coord == x  && this.y_coord == y && this.z_coord == z) {
				return true;
			}
			return false;
		}
		
	}
	
	/**
	 * Add an element at a fixed position.
	 * 
	 * Time complexity: O(1), because validating coordinates, creating a tuple and
	 * enqueuing all take constant time.
	 * 
	 * @param element The element to be added at the indicated position.
	 * @param x X Coordinate of the position of the element.
	 * @param y Y Coordinate of the position of the element.
	 * @param z Z Coordinate of the position of the element.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 */
	@Override
	public void add(int x, int y, int z, T element) throws IndexOutOfBoundsException {
		validCoords(x, y, z);
		QuadTuple<T> tp = new QuadTuple<>(x, y, z, element);
		planesNCoords.enqueue(tp);
	}
	
	/**
	 * Return the 'oldest' element at the indicated position.
	 * 
	 * Time complexity: O(n), because in the worst case it needs to iterate through all airplanes
	 * to get the one at the specified position.
	 * 
	 * @param x X Coordinate of the position of the element.
	 * @param y Y Coordinate of the position of the element.
	 * @param z Z Coordinate of the position of the element.
	 * @return 'Oldest' element at this position or null if no elements at the indicated position.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 */
	@Override
	public T get(int x, int y, int z) throws IndexOutOfBoundsException {
		validCoords(x, y, z);
		for (QuadTuple<T> tuple:planesNCoords) {
			if (tuple.coordsMatch(x, y, z)) {
				return tuple.getT();	// Return immediately once the oldest is found
			}
		}
		return null;	// Return null if none found
	}

	/**
	 * Return all the elements at the indicated position.
	 * 
	 * Time complexity: O(n), because in the worst case it needs to iterate through all airplanes
	 * to get the ones at the specified position.
	 * 
	 * @param x X Coordinate of the position of the element(s).
	 * @param y Y Coordinate of the position of the element(s).
	 * @param z Z Coordinate of the position of the element(s).
	 * @return An IterableQueue of all elements at this position or null if no elements at the indicated position.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 */
	@Override
	public IterableQueue<T> getAll(int x, int y, int z) throws IndexOutOfBoundsException {
		validCoords(x, y, z);
		IterableQueue<T> planeQueue = new TraversableQueue<T>();
		for (QuadTuple<T> tuple:planesNCoords) {
			if (tuple.coordsMatch(x, y, z)) {
				planeQueue.enqueue(tuple.getT());
			}
		}
		if (planeQueue.size() == 0) {
			return null;	// Return null if empty
		}
		return planeQueue;
	}

	/**
	 * Indicates whether there are more than one elements at the indicated position.
	 * 
	 * Time complexity: O(n), because in the worst case it needs to iterate through all airplanes
	 * to determine whether the count is greater than 1.
	 * 
	 * @param x X Coordinate of the position of the element(s).
	 * @param y Y Coordinate of the position of the element(s).
	 * @param z Z Coordinate of the position of the element(s).
	 * @return true if there are more than one elements at the indicated position, false otherwise.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 */
	@Override
	public boolean isMultipleElementsAt(int x, int y, int z) throws IndexOutOfBoundsException {
		validCoords(x, y, z);
		int count = 0;
		for (QuadTuple<T> tuple:planesNCoords) {
			if (tuple.coordsMatch(x, y, z)) {
				count++;
				if (count > 1) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Removes the specified element at the indicated position.
	 * 
	 * Time complexity: O(n), because in the worst case it needs to iterate through all airplanes
	 * to find the specified airplane.
	 * 
	 * @param element The element to be removed from the indicated position.
	 * @param x X Coordinate of the position.
	 * @param y Y Coordinate of the position.
	 * @param z Z Coordinate of the position.
	 * @return true if the element was removed from the indicated position, false otherwise.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 */
	@Override
	public boolean remove(int x, int y, int z, T element) throws IndexOutOfBoundsException {
		validCoords(x, y, z);
		IterableQueue<QuadTuple<T>> temp = new TraversableQueue<QuadTuple<T>>();
		boolean skipped = false;
		for (QuadTuple<T> tuple:planesNCoords) {
			T plane = tuple.getT();
			if (tuple.coordsMatch(x, y, z)
					&& plane.equals(element)) {
				skipped = true;
				continue;
			}
			temp.enqueue(tuple);	// Copy the remaining ones
		}
		this.planesNCoords = temp;
		return skipped;
	}

	/**
	 * Removes all elements at the indicated position.
	 * 
	 * Time complexity: O(n), because in the worst case it needs to iterate through all airplanes
	 * to remove the airplanes at the specified coordinates.
	 * 
	 * @param x X Coordinate of the position.
	 * @param y Y Coordinate of the position.
	 * @param z Z Coordinate of the position.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 */
	@Override
	public void removeAll(int x, int y, int z) throws IndexOutOfBoundsException {
		validCoords(x, y, z);
		IterableQueue<QuadTuple<T>> temp = new TraversableQueue<QuadTuple<T>>();
		for (QuadTuple<T> tuple:planesNCoords) {
			if (tuple.coordsMatch(x, y, z)) {
				continue;
			}
			temp.enqueue(tuple);	// Copy the remaining ones
		}
		this.planesNCoords = temp;
	}

	/**
	 * Removes all elements stored in the cube.
	 * 
	 * Time complexity: O(1)
	 */
	@Override
	public void clear() {
		this.planesNCoords = new TraversableQueue<QuadTuple<T>>();
	}
	
	/**
	 * Check whether the given coordinates are invalid and will raise exceptions.
	 * 
	 * @param x X Coordinate of the position.
	 * @param y Y Coordinate of the position.
	 * @param z Z Coordinate of the position.
	 */
	private void validCoords(int x, int y, int z) throws IndexOutOfBoundsException {
		if (x > max_X || y > max_Y || z > max_Z
				|| x < 0 || y < 0 || z < 0) {
			throw new IndexOutOfBoundsException();
		}
	}
	
}

/**
 * Design justifications:
 * The cube CDT is heavily based on the queue CDT implemented and the memory space concern. It is storing the actual
 * elements with their coordinates assigned, rather than a segmented spatial cube. Considering OneSky, if the latter 
 * is implemented, the CDT will need at least 5321*3428*35 cells (each of which involves 2*3 bytes for only the location
 * information). It can be a challenge for 8GB RAM in a multitask computer with to store this much data. An airplane 
 * can occupy one cell, indicating that most of the cells will be empty after loading 20000 airplanes. It is inefficient
 * to create these empty cells and perhaps access useful data (depending on the implementation, it is possible that every
 * cell needs to be checked). Storing the elements and their locations (20000*2*3 bytes maximum) significantly reduces 
 * memory usage, with the linked-list-based queue.
 * 
 * To bind the element and its coordinates data, a quadruple tuple-like data structure is created. Then 20000 instances
 * of such objects can be stored in the queue member (without using other collection structures). The cube also makes use
 * of the queue iterator to find matching locations and elements, which leads to linear time complexity. However, accessing 
 * may not be as fast as the memory-intense 3D spatial cube, for example if implemented in arrays it can index directly via 
 * the coordinates. Therefore, this implementation can be improved by sorting the elements by coordinates (but still using
 * a queue, maybe tree-based) and time complexity can become O(log(n)).
 */
