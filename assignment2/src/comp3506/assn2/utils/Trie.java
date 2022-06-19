package comp3506.assn2.utils;

/**
 * A standard trie data structure to encode all vocabularies and their occurrences
 * from a text file. Assuming character types are the 26 lower-case letters and
 * the apostrophe.
 * 
 * Space complexity: O(N) where N is the total string size
 * 
 * @author Howie L.
 */
public class Trie {
	private TrieNode root;
	private static final int CHAR_NUM = 27;	// 26 + 1
	
	/**
	 * A trie unit. An array is used to store children for speed.
	 * 
	 * Space complexity: O(1)
	 * 
	 * @author Howie L.
	 */
	private static class TrieNode {
		private char c;
		private TrieNode[] children;
		private boolean isEnd;
		private CustomizedList<Triple<Integer, Integer, Integer>> wordOccurrences;
		
		/**
		 * Constructor when the node is the root of a trie.
		 */
		public TrieNode() {
			this.init();
		}
		
		/**
		 * Constructor when the node is not the root of a trie.
		 */
		public TrieNode(char c) {
			this.init();
			this.setCharacter(c);
		}
		
		/**
		 * Initialize data structure members.
		 * 
		 * Time complexity: O(1)
		 * 
		 */
		private void init() {
			this.isEnd = false;
			this.children = new TrieNode[CHAR_NUM];	// Children are initialized to null
			this.wordOccurrences = new CustomizedList<Triple<Integer, Integer, Integer>>();
		}
		
		/**
		 * Mark the node as the last character of a word.
		 * 
		 * Time complexity: O(1)
		 * 
		 * @param lineNumber Line number of the word in the whole text
		 * @param colNumber Column number of the word in the whole text
		 * @param idx Index of the word with respect to all words in the whole text, which is currently
		 * 			used for phrase (contiguous words) searching
		 */
		public void setWordEnd(int lineNumber, int colNumber, int idx) {
			this.isEnd = true;
			this.wordOccurrences.add(new Triple<Integer, Integer, Integer>(lineNumber, colNumber, idx));
		}
		
		/**
		 * Retrieve the child (next character) by character index.
		 * 
		 * Time complexity: O(1)
		 * 
		 * @param idx Index of the character, from 0 to 26 by default.
		 * @return The child node representing the corresponding character.
		 */
		public TrieNode getChild(int idx) {
			return this.children[idx];
		}
		
		/**
		 * Initialize the child at the given character index.
		 * 
		 * Time complexity: O(1)
		 * 
		 * @param idx Index of the character, from 0 to 26 by default.
		 * @param tn The node to set child
		 */
		public void setChild(int idx, TrieNode tn) {
			this.children[idx] = tn;
		}
		
		/**
		 * Time complexity: O(1)
		 * 
		 * @return A list of occurrences of the word that ends at this node. 
		 */
		public CustomizedList<Triple<Integer, Integer, Integer>> getWordOccurrences() {
			return this.wordOccurrences;
		}
		
		/**
		 * Time complexity: O(1)
		 * 
		 * @return The character represented by this node.
		 */
		public char getCharacter() {
			return this.c;
		}
		
		/**
		 * Set the character for this node.
		 * 
		 * Time complexity: O(1)
		 * 
		 * @param c Character, lower case letters or apostrophe expected by default
		 */
		public void setCharacter(char c) {
			this.c = c;
		}
		
		/**
		 * Time complexity: O(1)
		 * 
		 * @return true if the node represents the end character of a word
		 */
		public boolean isEndOfWord() {
			return this.isEnd;
		}
	}
	
	/**
	 * Default constructor of trie. Set the root to represent a null character.
	 */
	public Trie() {
		this.root = new TrieNode();
	}
	
	/**
	 * Insert a word into the trie.
	 * 
	 * Time complexity: O(N) where N is the length of word
	 * 
	 * @param wordItem A triple containing line number, column number and the word itself
	 * @param idx Index of the word with respect to all words in the whole text
	 */
	public void put(Triple<Integer, Integer, String> wordItem, int idx) {
		TrieNode temp = this.root;
		String word = wordItem.getRightValue();
		for (int i = 0; i < word.length(); ++i) {
			char c = word.charAt(i);
			int charIdx = getCharacterIndex(c);	// the word should only contain assumed characters
			if (temp.getChild(charIdx) == null) {
				temp.setChild(charIdx, new TrieNode(c));
			}
			temp = temp.getChild(charIdx);
		}
		temp.setWordEnd(wordItem.getLeftValue(), wordItem.getCentreValue(), idx);
	}
	
	/**
	 * Default method to search a word in the trie which is from the root node.
	 * 
	 * Time complexity: O(N) where N is the length of word
	 * 
	 * @param word Word to search
	 * @return A list of triples consist of line numbers, column numbers and word indices
	 */
	public CustomizedList<Triple<Integer, Integer, Integer>> searchWord(String word) {
		return searchWord(this.root, word);
	}
	
	/**
	 * Overloaded method to search a word in the trie starting from the specified node.
	 * 
	 * Time complexity: O(N) where N is the length of word
	 * 
	 * @param word Word to search
	 * @return A list of triples consist of line numbers, column numbers and word indices.
	 * 			Return null if none found.
	 */
	public static CustomizedList<Triple<Integer, Integer, Integer>> searchWord(TrieNode tn, String word) {
		TrieNode temp = tn;
		for (int i = 0; i < word.length(); ++i) {
			int charIdx = getCharacterIndex(word.charAt(i));
			if (temp.getChild(charIdx) == null) {
				return null;
			}
			temp = temp.getChild(charIdx);
		}
		return temp.getWordOccurrences();
	}
	
	/**
	 * Search a phrase which contains contiguous words. Note that this method may time out for
	 * the default 500ms JUnit4 test. The algorithm needs to be improved.
	 * 
	 * Time complexity: O(N^M) where N is the average length of each word and M is the number of words
	 * 					in the phrase.
	 * 
	 * @param phraseWords A sorted array of words from the phrase to search, AutoTester pre-processing expected.
	 * @return A list of line number and column number pairs indicating the occurrences in text.
	 */
	@SuppressWarnings("unchecked")
	public CustomizedList<Pair<Integer, Integer>> searchPhrase(String[] phraseWords) {
		if (phraseWords == null) {
			return null;
		}
		CustomizedList<Triple<Integer, Integer, Integer>> firstWordResults = this.searchWord(phraseWords[0]);
		if (phraseWords.length == 1) {
			return listLocationPairsOnly(firstWordResults);
		}
		CustomizedList<?>[] wordResults = new CustomizedList[phraseWords.length];
		wordResults[0] = firstWordResults;
		for (int i = 1; i < phraseWords.length; ++i) {
			CustomizedList<Triple<Integer, Integer, Integer>> wordResult = this.searchWord(phraseWords[i]);
			wordResults[i] = wordResult;
		}
		// TODO find a better logic to reduce runtime
		CustomizedList<Triple<Integer, Integer, Integer>> validFirstWordResults = 
				new CustomizedList<Triple<Integer, Integer, Integer>>();
		Triple<Integer, Integer, Integer>[] firstWordResultArray = firstWordResults.toArray();
		boolean[] isContiguous = new boolean[firstWordResultArray.length];
		
		for (int firstWordResultIdx = 0; firstWordResultIdx < firstWordResultArray.length; ++firstWordResultIdx) {
			int startIdxInText = firstWordResultArray[firstWordResultIdx].getRightValue();
			for (int j = 1; j < phraseWords.length; ++j) {
				CustomizedList<Triple<Integer, Integer, Integer>> intermediateWordResults = 
						(CustomizedList<Triple<Integer, Integer, Integer>>) wordResults[j];
				for (Triple<Integer, Integer, Integer> nextWordInstance : intermediateWordResults) {
					isContiguous[firstWordResultIdx] = (nextWordInstance.getRightValue() - j == startIdxInText);
					if (isContiguous[firstWordResultIdx]) {
						break;
					}
				}
				if (!isContiguous[firstWordResultIdx]) {
					break;
				}
			}
			if (isContiguous[firstWordResultIdx]) {
				validFirstWordResults.add(firstWordResultArray[firstWordResultIdx]);
			}
		}
		return listLocationPairsOnly(validFirstWordResults);
	}
	
	/**
	 * Search each words in a word list and append all results.
	 * 
	 * Time complexity: O(N*M) where N is the average word length and M is the list length
	 * 
	 * @param words A list of word to search
	 * @return A list of triples, each of which contains line number, column number and the corresponding word.
	 * 			Return an empty list if none found.
	 */
	public CustomizedList<Triple<Integer, Integer, String>> searchWords(CustomizedList<String> words) {
		CustomizedList<Triple<Integer, Integer, String>> results = new CustomizedList<Triple<Integer, Integer, String>>();
		for (String word : words) {
			results.addAll(listIncludeWord(this.searchWord(word), word));
		}
		return results;
	}
	
	/**
	 * Search for lines that contain all the words in a word list.
	 * 
	 * Time complexity: O(N*M) where N is the average word length and M is the list length
	 * 
	 * @param words A list of words to search
	 * @return A list of line numbers, empty if none found.
	 */
	public CustomizedList<Integer> searchAllWordsOnLine(CustomizedList<String> words) {
		if (words.size() == 0) {
			return null;
		}
		String[] searchWords = words.toArray();
		CustomizedList<Integer> lineNumbers = listLineNumbersOnly(this.searchWord(searchWords[0]));
		for (int i = 1; i < searchWords.length; ++i) {
			lineNumbers = listIntersection(lineNumbers, listLineNumbersOnly(this.searchWord(searchWords[i])));
		}
		return lineNumbers;
	}
	
	/**
	 * Search for lines that contain any of the words in a word list.
	 * 
	 * Time complexity: O(N*M) where N is the average word length and M is the list length
	 * 
	 * @param words A list of words to search
	 * @return A list of line numbers, empty if none found.
	 */
	public CustomizedList<Integer> searchAnyWordsOnLine(CustomizedList<String> words) {
		CustomizedList<Integer> lineNumbers = new CustomizedList<Integer>();
		for (String word : words) {
			lineNumbers = listUnion(lineNumbers, listLineNumbersOnly(searchWord(word)));
		}
		return lineNumbers;
	}
	
	/**
	 * Search for lines that contain all the words in the 'wordsRequired' list
	 * and none of the words in the 'wordsExcluded' list.
	 * 
	 * Time complexity: O(N*M) where N is the average word length and M is the average list length
	 * 
	 * @param wordsRequired A list of words to search
	 * @param wordsExcluded A list of words to exclude line results
	 * @return A list of line numbers, empty if none found.
	 */
	public CustomizedList<Integer> searchAllWordsOnLineWithExclusions(CustomizedList<String> wordsRequired, 
			CustomizedList<String> wordsExcluded) {
		CustomizedList<Integer> lineNumbersAll = searchAllWordsOnLine(wordsRequired);
		CustomizedList<Integer> lineNumbersAny = searchAnyWordsOnLine(wordsExcluded);
		return listDifference(lineNumbersAll, lineNumbersAny);
	}
	
	/**
	 * Search words that have the given prefix.
	 * 
	 * Time complexity: O(N*M) where N is the prefix length and M is the total string size of the trie.
	 * 
	 * @param prefix Prefix to search
	 * @return A list of pairs containing line and column numbers, empty if none found.
	 */
	public CustomizedList<Pair<Integer, Integer>> searchPrefix(String prefix) {
		TrieNode temp = this.root;
		for (int i = 0; i < prefix.length(); ++i) {
			int charIdx = getCharacterIndex(prefix.charAt(i));
			if (temp.getChild(charIdx) == null) {
				return new CustomizedList<Pair<Integer, Integer>>();	// empty list
			}
			temp = temp.getChild(charIdx);
		}
		// Jump to the node of the last character of prefix
		return listLocationPairsOnly(retrieveWordsWithPrefix(temp, prefix));
	}
	
	/**
	 * Search all words in a word list in a specified section.
	 * 
	 * Time complexity: O(N*M) where N is the average word length and M is the list length
	 * 
	 * @param words A list of words to search
	 * @param startLine Line number of the start of the section
	 * @param endLine Line number of the end of the section
	 * @return A list of triples, each of which contains line number, column number and the corresponding word.
	 * 			Return null if not all the words are found in the section.
	 */
	public CustomizedList<Triple<Integer, Integer, String>> searchWordsInSection(CustomizedList<String> words, 
			int startLine, int endLine) {
		CustomizedList<Triple<Integer, Integer, String>> results = new CustomizedList<Triple<Integer, Integer, String>>();
		for (String word : words) {
			CustomizedList<Triple<Integer, Integer, String>> wordResult = this.searchWordInSection(word, startLine, endLine);
			if (wordResult.size() == 0) {
				return null;
			}
			results.addAll(wordResult);
		}
		return results;
	}
	
	/**
	 * Search all words in the 'words' list given none of the words in the 'exclusions' list exists 
	 * in a specified section.
	 * 
	 * Time complexity: O(N*M) where N is the average word length and M is the average list length
	 * 
	 * @param words A list of words to search
	 * @param exclusions A list of words to exclude former found results
	 * @param startLine Line number of the start of the section
	 * @param endLine Line number of the end of the section
	 * @return A list of triples, each of which contains line number, column number and the corresponding word.
	 * 			Return null if any of the exclusion words are found in this section.
	 */
	public CustomizedList<Triple<Integer, Integer, String>> searchWordsInSectionWithExclusions(CustomizedList<String> words, 
			CustomizedList<String> exclusions, int startLine, int endLine) {
		if (exclusions != null) {
			for (String excludedWord : exclusions) {
				if (this.searchWordInSection(excludedWord, startLine, endLine).size() != 0) {
					return null;
				}
			}
		}
		return searchWordsInSection(words, startLine, endLine);
	}
	
	/**
	 * Search all words in the 'words' list given any of the words in the 'orWords' list appears 
	 * in a specified section.
	 * 
	 * Time complexity: O(N*M) where N is the average word length and M is the average list length
	 * 
	 * @param words A list of words to search
	 * @param exclusions A list of words to exclude former found results
	 * @param startLine Line number of the start of the section
	 * @param endLine Line number of the end of the section
	 * @return A list of triples, each of which contains line number, column number and the corresponding word.
	 * 			Return null if none of the or words is found in this section.
	 */
	public CustomizedList<Triple<Integer, Integer, String>> searchWordsInSectionWithOrWords(CustomizedList<String> words, 
			CustomizedList<String> orWords, int startLine, int endLine) {
		CustomizedList<Triple<Integer, Integer, String>> results = this.searchWordsInSection(words, startLine, endLine);
		if (results == null) {
			return results;
		}
		CustomizedList<Triple<Integer, Integer, String>> orWordResults = new CustomizedList<Triple<Integer, Integer, String>>();
		if (orWords != null) {
			for (String orWord : orWords) {
				orWordResults.addAll(this.searchWordInSection(orWord, startLine, endLine));
			}
			if (orWordResults.size() == 0) {
				return null;
			}
		}
		results.addAll(orWordResults);
		return results;
	}
	
	/**
	 * Search all occurrences of a single word in a specified section.
	 * 
	 * Time complexity: O(N) where N is the word length
	 * 
	 * @param word Word to search
	 * @param startLine Line number of the start of the section
	 * @param endLine Line number of the end of the section
	 * @return A list of triples, each of which contains line number, column number and the corresponding word.
	 * 			Return an empty list if none found in this section.
	 */
	private CustomizedList<Triple<Integer, Integer, String>> searchWordInSection(String word, int startLine, int endLine) {
		CustomizedList<Triple<Integer, Integer, Integer>> total = searchWord(word);
		CustomizedList<Triple<Integer, Integer, String>> results = new CustomizedList<Triple<Integer, Integer, String>>();
		for (Triple<Integer, Integer, Integer> wordInstance : total) {
			if (((wordInstance.getLeftValue() <= endLine && endLine != -1) || endLine == -1)
					&& wordInstance.getLeftValue() >= startLine) {
				results.add(new Triple<Integer, Integer, String>(wordInstance.getLeftValue(), wordInstance.getCentreValue(), word));
			}
		}
		return results;
	}
	
	/**
	 * Under a given node, recursively collect any words that contain a prefix.
	 * 
	 * Time complexity: O(M) where M is the total string size of the trie
	 * 
	 * @param tn Trie node to start searching from
	 * @param prefix Prefix to search
	 * @return A list of triples consist of line numbers, column numbers and word indices.
	 * 			Return an empty list if none found.
	 */
	private static CustomizedList<Triple<Integer, Integer, Integer>> retrieveWordsWithPrefix(TrieNode tn, String prefix) {
		CustomizedList<Triple<Integer, Integer, Integer>> results = new CustomizedList<Triple<Integer, Integer, Integer>>();
		if (tn.isEndOfWord()) {
			results.addAll(tn.getWordOccurrences());
		}
		for (int i = 0; i < CHAR_NUM; ++i) {
			TrieNode child = tn.getChild(i);
			if (child != null) {
				results.addAll(retrieveWordsWithPrefix(child, prefix + child.getCharacter()));
			}
		}
		return results;
	}
	
	/**
	 * Convert a 3D-like list to 2D, typically for discarding the word indices.
	 * 
	 * Time complexity: O(N) where N is the list length.
	 * 
	 * @param in Input list containing triples of integers
	 * @return A list containing pairs of integers that are corresponding to the left and center values of 
	 * 			the original triples respectively.
	 */
	private static CustomizedList<Pair<Integer, Integer>> listLocationPairsOnly(CustomizedList<Triple<Integer, Integer, Integer>> in) {
		CustomizedList<Pair<Integer, Integer>> out = new CustomizedList<Pair<Integer, Integer>>();
		for (Triple<Integer, Integer, Integer> old : in) {
			out.add(new Pair<Integer, Integer>(old.getLeftValue(), old.getCentreValue()));
		}
		return out;
	}
	
	/**
	 * Convert a 3D-like list to 1D, typically for discarding the column numbers and word indices.
	 * 
	 * Time complexity: O(N) where N is the list length.
	 * 
	 * @param in Input list containing triples of integers
	 * @return A list containing integers that are corresponding to the left values of the original triples.
	 */
	private static CustomizedList<Integer> listLineNumbersOnly(CustomizedList<Triple<Integer, Integer, Integer>> in) {
		CustomizedList<Integer> out = new CustomizedList<Integer>();
		for (Triple<Integer, Integer, Integer> old : in) {
			out.add(old.getLeftValue());
		}
		return out;
	}
	
	/**
	 * Replace the data in the word index dimension with the actual word.
	 * 
	 * Time complexity: O(N) where N is the list length.
	 * 
	 * @param in Input list containing triples of integers
	 * @param word Word to put back
	 * @return A list of triples, each of which contains line number, column number and the corresponding word.
	 */
	public static CustomizedList<Triple<Integer, Integer, String>> listIncludeWord(CustomizedList<Triple<Integer, Integer, Integer>> in, 
			String word) {
		CustomizedList<Triple<Integer, Integer, String>> out = new CustomizedList<Triple<Integer, Integer, String>>();
		for (Triple<Integer, Integer, Integer> old : in) {
			out.add(new Triple<Integer, Integer, String>(old.getLeftValue(), old.getCentreValue(), word));
		}
		return out;
	}
	
	/**
	 * Calculate the array index of a trie node's children.
	 * 
	 * Time complexity: O(1)
	 * 
	 * @param c Character to store in a node child, lower case letters 
	 * 			or apostrophe expected by default
	 * @return Index of the default 27 length children array
	 */
	private static int getCharacterIndex(char c) {
		if (c == '\'') {
			return 26;
		} 
		return c - 'a';
	}
	
	/**
	 * Get the intersection of two lists.
	 * 
	 * Time complexity: O(N^2) where N is the average list length,
	 * 					because the contain method of list is O(N)
	 * 
	 * @param l1 First list
	 * @param l2 Second list
	 * @return A new list containing elements in both lists
	 */
	private static <T> CustomizedList<T> listIntersection(CustomizedList<T> l1, CustomizedList<T> l2) {
		CustomizedList<T> result = new CustomizedSet<T>();
		for (T t1 : l1) {
			if (l2.contains(t1)) {
				result.add(t1);
			}
		}
		return result;
	}
	
	/**
	 * Get the union of two lists.
	 * 
	 * Time complexity: O(N^2) where N is the average list length,
	 * 					because the add method of set is O(N)
	 * 
	 * @param l1 First list
	 * @param l2 Second list
	 * @return A new list containing elements in either list
	 */
	private static <T> CustomizedList<T> listUnion(CustomizedList<T> l1, CustomizedList<T> l2) {
		CustomizedList<T> result = new CustomizedSet<T>();
		result.addAll(l1);
		result.addAll(l2);
		return result;
	}
	
	/**
	 * Get the difference of two lists.
	 * 
	 * Time complexity: O(N^2) where N is the average list length,
	 * 					because the add method of set is O(N)
	 * 
	 * @param l1 First list
	 * @param l2 Second list
	 * @return A new list containing the difference of union and intersection
	 */
	private static <T> CustomizedList<T> listDifference(CustomizedList<T> l1, CustomizedList<T> l2) {
		CustomizedList<T> intersection = listIntersection(l1, l2);
		CustomizedList<T> result = new CustomizedSet<T>();
		for (T t1 : l1) {
			if (!intersection.contains(t1)) {
				result.add(t1);
			}
		}
		return result;
	}
}
