package comp3506.assn2.utils;


/**
 * A basic map-like data structure to store key-value pairs, inspired by the UnsortedTableMap 
 * by Goodrich et al. [1]. The class is for searching titles (as key) and get the corresponding
 * values (line numbers) in the index file encoded in this data structure. Only methods needed
 * for this application are implemented.
 * 
 * Space Complexity: O(N) where N is the number of pairs stored
 * 
 * @author Howie L.
 *
 * @param <K> Type of key to be held
 * @param <V> Type of value to be held
 */
public class CustomizedMap<K, V> {
	// Reuse the Pair rather than creating a MapEntry inner class
	private CustomizedList<Pair<K, V>> table;
	
	/*
	 * Default constructor of the map.
	 */
	public CustomizedMap() {
		this.table = new CustomizedList<Pair<K, V>>();
	}
	
	/**
	 * Add a key-value pair into the map, overwriting the corresponding value
	 * if the key already exists.
	 * 
	 * Time Complexity: O(N) where N is the number of pairs stored
	 * 
	 * @param key Key of the pair
	 * @param value Value of the pair
	 */
	public void put(K key, V value) {
		for (Pair<K, V> entry : this.table) {
			if (entry.getLeftValue().equals(key)) {
				entry.setRightValue(value);
				return;
			}
		}
		this.table.add(new Pair<K, V>(key, value));
	}
	
	/**
	 * Retrieve the value corresponding to the given key.
	 * 
	 * Time Complexity: O(N) where N is the number of pairs stored
	 * 
	 * @param key Key of the pair
	 * @return Value of the pair if key exists, otherwise null
	 */
	public V get(K key) {
		for (Pair<K, V> entry : this.table) {
			if (entry.getLeftValue().equals(key)) {
				return entry.getRightValue();
			}
		}
		return null;
	}
}

/**
 * References:
 * [1]	M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data structures and algorithms in Java. John Wiley & Sons, 2014.
 */
