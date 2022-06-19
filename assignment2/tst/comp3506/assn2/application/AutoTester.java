package comp3506.assn2.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;
import comp3506.assn2.utils.Trie;
import comp3506.assn2.utils.CustomizedList;
import comp3506.assn2.utils.CustomizedMap;
import comp3506.assn2.utils.CustomizedSet;


/**
 * Hook class used by automated testing tool.
 * The testing tool will instantiate an object of this class to test the functionality of your assignment.
 * You must implement the constructor stub below and override the methods from the Search interface
 * so that they call the necessary code in your application.
 * 
 * @author 
 */
public class AutoTester implements Search {
	private Trie docTrie;	// for main document searching
	private CustomizedMap<String, Pair<Integer, Integer>> indexTable;	// for index document searching
	private CustomizedList<String> stopWords;

	/**
	 * Create an object that performs search operations on a document.
	 * If indexFileName or stopWordsFileName are null or an empty string the document should be loaded
	 * and all searches will be across the entire document with no stop words.
	 * All files are expected to be in the files sub-directory and 
	 * file names are to include the relative path to the files (e.g. "files\\shakespeare.txt").
	 * 
	 * @param documentFileName  Name of the file containing the text of the document to be searched.
	 * @param indexFileName     Name of the file containing the index of sections in the document.
	 * @param stopWordsFileName Name of the file containing the stop words ignored by most searches.
	 * @throws FileNotFoundException if any of the files cannot be loaded. 
	 *                               The name of the file(s) that could not be loaded should be passed 
	 *                               to the FileNotFoundException's constructor.
	 * @throws IllegalArgumentException if documentFileName is null or an empty string.
	 */
	public AutoTester(String documentFileName, String indexFileName, String stopWordsFileName) 
			throws FileNotFoundException, IllegalArgumentException {
		if (!isStringValid(documentFileName)) {
			throw new IllegalArgumentException();
		}
		this.loadDocTrie(documentFileName);
		if (isStringValid(indexFileName)) {
			this.loadIndexTable(indexFileName);
		}
		if (isStringValid(stopWordsFileName)) {
			this.loadStopWords(stopWordsFileName);
		}
	}
	
	/**
	 * Determines the number of times the word appears in the document.
	 * 
	 * @param word The word to be counted in the document.
	 * @return The number of occurrences of the word in the document.
	 * @throws IllegalArgumentException if word is null or an empty String.
	 */
	@Override
	public int wordCount(String word) throws IllegalArgumentException {
		if (!isStringValid(word)) {
			throw new IllegalArgumentException();
		}
		return docTrie.searchWord(word.toLowerCase()).size();
	}

	/**
	 * Finds all occurrences of the phrase in the document.
	 * A phrase may be a single word or a sequence of words.
	 * 
	 * @param phrase The phrase to be found in the document.
	 * @return List of pairs, where each pair indicates the line and column number of each occurrence of the phrase.
	 *         Returns an empty list if the phrase is not found in the document.
	 * @throws IllegalArgumentException if phrase is null or an empty String.
	 */
	@Override
	public List<Pair<Integer,Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
		if (!isStringValid(phrase)) {
			throw new IllegalArgumentException();
		}
		return convertToStandardList(docTrie.searchPhrase(processPhrase(phrase)));
	}

	/**
	 * Finds all occurrences of the prefix in the document.
	 * A prefix is the start of a word. It can also be the complete word.
	 * For example, "obscure" would be a prefix for "obscure", "obscured", "obscures" and "obscurely".
	 * 
	 * @param prefix The prefix of a word that is to be found in the document.
	 * @return List of pairs, where each pair indicates the line and column number of each occurrence of the prefix.
	 *         Returns an empty list if the prefix is not found in the document.
	 * @throws IllegalArgumentException if prefix is null or an empty String.
	 */
	@Override
	public List<Pair<Integer,Integer>> prefixOccurrence(String prefix) throws IllegalArgumentException {
		if (!isStringValid(prefix)) throw new IllegalArgumentException();
		return convertToStandardList(docTrie.searchPrefix(prefix.toLowerCase()));
	}

	/**
	 * Searches the document for lines that contain all the words in the 'words' parameter.
	 * Implements simple "and" logic when searching for the words.
	 * The words do not need to be contiguous on the line.
	 * 
	 * @param words Array of words to find on a single line in the document.
	 * @return List of line numbers on which all the words appear in the document.
	 *         Returns an empty list if the words do not appear in any line in the document.
	 * @throws IllegalArgumentException if words is null or an empty array 
	 *                                  or any of the Strings in the array are null or empty.
	 */
	@Override
	public List<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {
		if (!isStringArrayValid(words)) throw new IllegalArgumentException();
		return convertToStandardList(docTrie.searchAllWordsOnLine(processWords(words)));
	}

	/**
	 * Searches the document for lines that contain any of the words in the 'words' parameter.
	 * Implements simple "or" logic when searching for the words.
	 * The words do not need to be contiguous on the line.
	 * 
	 * @param words Array of words to find on a single line in the document.
	 * @return List of line numbers on which any of the words appear in the document.
	 *         Returns an empty list if none of the words appear in any line in the document.
	 * @throws IllegalArgumentException if words is null or an empty array 
	 *                                  or any of the Strings in the array are null or empty.
	 */
	@Override
	public List<Integer> someWordsOnLine(String[] words) throws IllegalArgumentException {
		if (!isStringArrayValid(words)) throw new IllegalArgumentException();
		return convertToStandardList(docTrie.searchAnyWordsOnLine(processWords(words)));
	}

	/**
	 * Searches the document for lines that contain all the words in the 'wordsRequired' parameter
	 * and none of the words in the 'wordsExcluded' parameter.
	 * Implements simple "not" logic when searching for the words.
	 * The words do not need to be contiguous on the line.
	 * 
	 * @param wordsRequired Array of words to find on a single line in the document.
	 * @param wordsExcluded Array of words that must not be on the same line as 'wordsRequired'.
	 * @return List of line numbers on which all the wordsRequired appear 
	 *         and none of the wordsExcluded appear in the document.
	 *         Returns an empty list if no lines meet the search criteria.
	 * @throws IllegalArgumentException if either of wordsRequired or wordsExcluded are null or an empty array 
	 *                                  or any of the Strings in either of the arrays are null or empty.
	 */
	@Override
	public List<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded) 
			throws IllegalArgumentException {
		if (!isStringArrayValid(wordsRequired) || !isStringArrayValid(wordsExcluded)) throw new IllegalArgumentException();
		return convertToStandardList(docTrie.searchAllWordsOnLineWithExclusions(processWords(wordsRequired), 
				processWords(wordsExcluded)));
	}

	/**
	 * Searches the document for sections that contain all the words in the 'words' parameter.
	 * Implements simple "and" logic when searching for the words.
	 * The words do not need to be on the same lines.
	 * 
	 * @param titles Array of titles of the sections to search within, 
	 *               the entire document is searched if titles is null or an empty array.
	 * @param words Array of words to find within a defined section in the document.
	 * @return List of triples, where each triple indicates the line and column number and word found,
	 *         for each occurrence of one of the words.
	 *         Returns an empty list if the words are not found in the indicated sections of the document, 
	 *         or all the indicated sections are not part of the document.
	 * @throws IllegalArgumentException if words is null or an empty array 
	 *                                  or any of the Strings in either of the arrays are null or empty.
	 */
	@Override
	public List<Triple<Integer,Integer,String>> simpleAndSearch(String[] titles, String[] words)
			throws IllegalArgumentException {
		if (!isStringArrayValid(titles, true) || !isStringArrayValid(words)) throw new IllegalArgumentException();
		if (titles == null || titles.length == 0) {
			return convertToStandardList(docTrie.searchWords(processWords(words)));
		}
		List<Triple<Integer,Integer,String>> finalResults = new ArrayList<>();
		for (String title : titles) {
			Pair<Integer, Integer> lineNumbers = indexTable.get(processTitle(title));
			if (lineNumbers != null) {
				int startLine = lineNumbers.getLeftValue();
				int endLine = lineNumbers.getRightValue();
				finalResults.addAll(convertToStandardList(docTrie.searchWordsInSection(processWords(words), startLine, endLine)));
			}
		}
		return finalResults;
	}

	/**
	 * Searches the document for sections that contain any of the words in the 'words' parameter.
	 * Implements simple "or" logic when searching for the words.
	 * The words do not need to be on the same lines.
	 * 
	 * @param titles Array of titles of the sections to search within, 
	 *               the entire document is searched if titles is null or an empty array.
	 * @param words Array of words to find within a defined section in the document.
	 * @return List of triples, where each triple indicates the line and column number and word found,
	 *         for each occurrence of one of the words.
	 *         Returns an empty list if the words are not found in the indicated sections of the document, 
	 *         or all the indicated sections are not part of the document.
	 * @throws IllegalArgumentException if words is null or an empty array 
	 *                                  or any of the Strings in either of the arrays are null or empty.
	 */
	@Override
	public List<Triple<Integer,Integer,String>> simpleOrSearch(String[] titles, String[] words)
			throws IllegalArgumentException {
		if (!isStringArrayValid(titles, true) || !isStringArrayValid(words)) throw new IllegalArgumentException();
		List<Triple<Integer,Integer,String>> totalResults = convertToStandardList(docTrie.searchWords(processWords(words)));
		if (titles == null || titles.length == 0) {
			return totalResults;
		}
		List<Triple<Integer,Integer,String>> finalResults = new ArrayList<>();
		for (String title : titles) {
			Pair<Integer, Integer> lineNumbers = indexTable.get(processTitle(title));
			if (lineNumbers != null) {
				int startLine = lineNumbers.getLeftValue();
				int endLine = lineNumbers.getRightValue();
				for (Triple<Integer,Integer,String> wordInstance : totalResults) {
					if (((wordInstance.getLeftValue() <= endLine && endLine != -1) || endLine == -1)
							&& wordInstance.getLeftValue() >= startLine) {
						finalResults.add(wordInstance);
					}
				}
			}
		}
		return finalResults;
	}

	/**
	 * Searches the document for sections that contain all the words in the 'wordsRequired' parameter
	 * and none of the words in the 'wordsExcluded' parameter.
	 * Implements simple "not" logic when searching for the words.
	 * The words do not need to be on the same lines.
	 * 
	 * @param titles Array of titles of the sections to search within, 
	 *               the entire document is searched if titles is null or an empty array.
	 * @param wordsRequired Array of words to find within a defined section in the document.
	 * @param wordsExcluded Array of words that must not be in the same section as 'wordsRequired'.
	 * @return List of triples, where each triple indicates the line and column number and word found,
	 *         for each occurrence of one of the required words.
	 *         Returns an empty list if the words are not found in the indicated sections of the document, 
	 *         or all the indicated sections are not part of the document.
	 * @throws IllegalArgumentException if wordsRequired is null or an empty array 
	 *                                  or any of the Strings in any of the arrays are null or empty.
	 */
	@Override
	public List<Triple<Integer,Integer,String>> simpleNotSearch(String[] titles, String[] wordsRequired, 
			                                                     String[] wordsExcluded)
			throws IllegalArgumentException {
		if (!isStringArrayValid(titles, true) || !isStringArrayValid(wordsRequired) || !isStringArrayValid(wordsExcluded, true)) 
			throw new IllegalArgumentException();
		if (titles == null || titles.length == 0) {
			if (wordsExcluded != null) {
				for (String excludedWord : wordsExcluded) {
					if (docTrie.searchWord(excludedWord.toLowerCase()).size() != 0) {
						return new ArrayList<>();
					}
				}
			}
			return convertToStandardList(docTrie.searchWords(processWords(wordsRequired)));
		}
		List<Triple<Integer,Integer,String>> finalResults = new ArrayList<>();
		for (String title : titles) {
			Pair<Integer, Integer> lineNumbers = indexTable.get(processTitle(title));
			if (lineNumbers != null) {
				int startLine = lineNumbers.getLeftValue();
				int endLine = lineNumbers.getRightValue();
				finalResults.addAll(convertToStandardList(docTrie.searchWordsInSectionWithExclusions(processWords(wordsRequired), 
						processWords(wordsExcluded), startLine, endLine)));
			}
		}
		return finalResults;
	}

	/**
	 * Searches the document for sections that contain all the words in the 'wordsRequired' parameter
	 * and at least one of the words in the 'orWords' parameter.
	 * Implements simple compound "and/or" logic when searching for the words.
	 * The words do not need to be on the same lines.
	 * 
	 * @param titles Array of titles of the sections to search within, 
	 *               the entire document is searched if titles is null or an empty array.
	 * @param wordsRequired Array of words to find within a defined section in the document.
	 * @param orWords Array of words, of which at least one, must be in the same section as 'wordsRequired'.
	 * @return List of triples, where each triple indicates the line and column number and word found,
	 *         for each occurrence of one of the words.
	 *         Returns an empty list if the words are not found in the indicated sections of the document, 
	 *         or all the indicated sections are not part of the document.
	 * @throws IllegalArgumentException if wordsRequired is null or an empty array 
	 *                                  or any of the Strings in any of the arrays are null or empty.
	 */
	@Override
	public List<Triple<Integer,Integer,String>> compoundAndOrSearch(String[] titles, String[] wordsRequired, 
			                                                         String[] orWords)
			throws IllegalArgumentException {
		if (!isStringArrayValid(titles, true) || !isStringArrayValid(wordsRequired) || !isStringArrayValid(orWords, true)) 
			throw new IllegalArgumentException();
		if (titles == null || titles.length == 0) {
			List<Triple<Integer, Integer, String>> results = convertToStandardList(docTrie.searchWords(processWords(wordsRequired)));
			if (results.size() == 0) {
				return new ArrayList<>();
			}
			if (orWords != null) {
				List<Triple<Integer, Integer, String>> orWordResults = new ArrayList<>();
				for (String orWord : orWords) {
					orWordResults.addAll(convertToStandardList(Trie.listIncludeWord(docTrie.searchWord(orWord.toLowerCase()), orWord)));
				}
				if (orWordResults.size() == 0) {
					return new ArrayList<>();
				}
				results.addAll(orWordResults);
			}
			return results;
		}
		List<Triple<Integer,Integer,String>> finalResults = new ArrayList<>();
		for (String title : titles) {
			Pair<Integer, Integer> lineNumbers = indexTable.get(processTitle(title));
			if (lineNumbers != null) {
				int startLine = lineNumbers.getLeftValue();
				int endLine = lineNumbers.getRightValue();
				finalResults.addAll(convertToStandardList(docTrie.searchWordsInSectionWithOrWords(processWords(wordsRequired), 
						processWords(orWords), startLine, endLine)));
			}
		}
		return finalResults;
	}
	
	/**
	 * Helper function to determine whether arguments of search functions are valid.
	 * 
	 * Time Complexity: O(N) where N is number of strings in array
	 * 
	 * @param strings Array of strings to check
	 * @return true if valid
	 */
	private static boolean isStringArrayValid(String[] strings) {
		if (strings == null || strings.length == 0) return false;
		for (String s: strings) {
			if (!isStringValid(s)) return false;
		}
		return true;
	}
	
	/**
	 * Overloaded helper function to determine whether arguments of search functions are valid.
	 * 
	 * Time Complexity: O(N) where N is number of strings in array
	 * 
	 * @param strings Array of strings to check
	 * @return true if valid
	 */
	private static boolean isStringArrayValid(String[] strings, boolean isOptional) {
		if (isOptional) {
			if (strings == null || strings.length == 0)
				return true;
			else {
				for (String s: strings) {
					if (!isStringValid(s)) return false;
				}
				return true;
			}
		}
		return isStringArrayValid(strings);
	}
	
	/**
	 * Time Complexity: O(1)
	 * 
	 * @param s String to validate
	 * @return true if valid
	 */
	private static boolean isStringValid(String s) {
		return s != null && !s.isEmpty();
	}
	
	/**
	 * Convert raw output from Trie class to the required list type.
	 * 
	 * Time Complexity: O(N) where N is the number of elements in list
	 * 
	 * @param old Raw list from Trie output
	 * @return Converted array list, empty if old list is null or empty
	 */
	private static <T> List<T> convertToStandardList(CustomizedList<T> old) {
		List<T> result = new ArrayList<T>();
		if (old != null && old.size() != 0) {
			for (T element : old) {
				result.add(element);
			}
		}
		return result;
	}
	
	/**
	 * Load the main document to search into a Trie data structure.
	 * 
	 * Time Complexity: O(L*C) where L is the number of lines and C is the average number 
	 * 					of columns on each line in the file.
	 * 
	 * @param documentFileName Filename of the documentation including path
	 * @throws FileNotFoundException if the specified file cannot be loaded.
	 */
	private void loadDocTrie(String documentFileName) throws FileNotFoundException {
		BufferedReader docReader = new BufferedReader(new FileReader(documentFileName));
		this.docTrie = new Trie();
		String line = "";
		int lineCounter = 1;	// starts from 1
		int wordIdx = 0;	// keep track of word order in full text
		try {
			while((line = docReader.readLine()) != null) {
				if (line.length() != 0) {
					for (Triple<Integer,Integer,String> item : parseDocLine(line, lineCounter)) {
						docTrie.put(item, wordIdx);
						wordIdx++;
					}
				}
				lineCounter++;	
			}
			docReader.close();
		} catch (IOException ioe) {
			// Invalid data in file will make the entire file invalid
			throw new FileNotFoundException("Not a correct document file: " + documentFileName);
		}
	}
	
	/**
	 * Load the index information into a map, including end page number of each title.
	 * 
	 * Time Complexity: O(L^2) where L is the number of lines in the file,
	 * 					because the put operation of map takes O(L).
	 * 
	 * @param indexFileName Filename of the corresponding document index.
	 * @throws FileNotFoundException if the specified file cannot be loaded.
	 */
	private void loadIndexTable(String indexFileName) throws FileNotFoundException {
		BufferedReader indexReader = new BufferedReader(new FileReader(indexFileName));
		this.indexTable = new CustomizedMap<String, Pair<Integer, Integer>>();
		String line, nextLine = "";
		int endLine = -1;
		try {
			line = indexReader.readLine();
			while (nextLine != null) {
				String[] currentIndexInfo = line.trim().split(",");
				String title = processTitle(currentIndexInfo[0]);
				int startLine = Integer.parseInt(currentIndexInfo[1]);
				nextLine = indexReader.readLine();
				if (nextLine != null && nextLine.length() != 0) {
					currentIndexInfo = nextLine.trim().split(",");
					endLine = Integer.parseInt(currentIndexInfo[1]) - 1;
					line = nextLine;
				} else {
					endLine = -1;
				}
				// Assuming title is unique
				this.indexTable.put(title, new Pair<Integer, Integer>(startLine, endLine));
			}
			indexReader.close();
		} catch (IOException ioe) {
			// Invalid data in file will make the entire file invalid
			throw new FileNotFoundException("Not a correct index file: " + indexFileName);
		}
	}
	
	/**
	 * Load the stop words into a list.
	 * 
	 * Time Complexity: O(L) where L is the number of lines in the file,
	 * 					because the add operation of list takes O(1).
	 * 
	 * @param stopWordsFileName Filename of the stop words.
	 * @throws FileNotFoundException if the specified file cannot be loaded.
	 */
	private void loadStopWords(String stopWordsFileName) throws FileNotFoundException {
		BufferedReader swReader = new BufferedReader(new FileReader(stopWordsFileName));
		String line = "";
		this.stopWords = new CustomizedList<String>();
		try {
			while ((line = swReader.readLine()) != null) {
				if (line.length() != 0) {
					this.stopWords.add(line.toLowerCase().trim());
				}
			}
			swReader.close();
		} catch (IOException ioe) {
			// Invalid data in file will make the entire file invalid
			throw new FileNotFoundException("Not a correct stop word file: " + stopWordsFileName);
		}
	}
	
	/**
	 * Split a line from the main document to a list of words and their locations, omitting characters 
	 * other than letters and and the apostrophe in the middle of a word.
	 * 
	 * Time Complexity: O(c) where c is the number of columns on this line.
	 * 
	 * @param line The line to be processed
	 * @param lineNumber The corresponding line number of the line
	 * @return A list of words and their locations on the line
	 */
	private static CustomizedList<Triple<Integer, Integer, String>> parseDocLine(String line, int lineNumber) {
		CustomizedList<Triple<Integer, Integer, String>> lineWords = 
				new CustomizedList<Triple<Integer, Integer, String>>();
		// It seems without paddings of whitespace will result in infinite while loop below
		String processedLine = " " + line.toLowerCase().replaceAll("[^a-z']", " ") + " ";
		int currentSpaceIdx = 0;
		int nextSpaceIdx = 0;
		int lastSpaceIdx = processedLine.lastIndexOf(" ");
		while (currentSpaceIdx != lastSpaceIdx) {
			int columnNumber = 0;
		    nextSpaceIdx = processedLine.indexOf(" ", currentSpaceIdx + 1);
		    String word = processedLine.substring(currentSpaceIdx, nextSpaceIdx);
		    if (word.length() != 1) {
			    if (word.startsWith(" ")) { 
			    	word = word.trim();	
			    }
			    columnNumber = currentSpaceIdx + 1;
			    // Remove invalid apostrophes
			    if (word.startsWith("'")) {
		    		word = word.substring(1);
		    		columnNumber++;
		    	}
		    	if (word.endsWith("'")) {
		    		word = word.substring(0, word.length() - 1);
		    	}
		    	lineWords.add(new Triple<Integer, Integer, String>(lineNumber, columnNumber, word));
		    }
		    currentSpaceIdx = nextSpaceIdx;
		}
		return lineWords;
	}
	
	/**
	 * Process input array of search words to neglect duplicates, cases and stop words.
	 * 
	 * Time Complexity: O(N*M) where N is the number of words and M is the
	 * 					average number of string length
	 * 
	 * @param arr Array of words for search functions
	 * @return A list of valid words in lower case
	 */
	private CustomizedList<String> processWords(String[] arr) {
		CustomizedList<String> searchWords = new CustomizedSet<String>();
		if (arr != null) {
			for (int i = 0; i < arr.length; ++i) {
				String temp = arr[i].toLowerCase();
				if (this.stopWords != null && !this.stopWords.contains(temp)) {
					searchWords.add(temp);
				}
			}
		}
		return searchWords;
	}
	
	/**
	 * Convert letters to lower case and remove any whitespace for a title string. Keep all
	 * other character types.
	 * 
	 * Time Complexity: O(N) where N is the string length
	 * 
	 * @param s Title string from an index file
	 * @return Title in lower case and without whitespace
	 */
	private static String processTitle(String s) {
		return s.toLowerCase().replaceAll("\\s+", "");
	}
	
	/**
	 * Split a phrase into words ignoring characters other than letters and the apostrophe 
	 * in the middle of a word.
	 * 
	 * Time complexity: O(N) where N is string length
	 * 
	 * @param phrase The phrase to split and to search
	 * @return An array containing words of the phrase
	 */
	private static String[] processPhrase(String phrase) {
		CustomizedList<Triple<Integer, Integer, String>> phraseWordInfo = parseDocLine(phrase, -1);
		if (phraseWordInfo.size() == 0) {
			return null;
		}
		String[] phraseWords = new String[phraseWordInfo.size()];
		int idx = 0;
		for (Triple<Integer, Integer, String> item : phraseWordInfo) {
			phraseWords[idx] = item.getRightValue();
			idx++;
		}
		return phraseWords;
	}

}
