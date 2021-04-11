package nl.krebos.poc.wordcount;

import java.util.Comparator;

public class WordFrequencyImpl implements WordFrequency {
	private String word;
	private int frequency;
	
	public void setWord(String word) {
		this.word = word;
		this.frequency = 1;
	}
	
	public String getWord() {
		return word;
	}

	public int getFrequency() {
		return frequency;
	}
	
	public void increaseFrequency() {
		this.frequency++;
	}
	
	/**
	 * used resources: 
	 * https://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-comparator/
	 * https://www.benchresources.net/sorting-list-of-objects-on-multiple-fields-using-comparator-in-java/
	 */
    public static Comparator<WordFrequency> WordFreqComparator = new Comparator<WordFrequency>() {

	public int compare(WordFrequency s1, WordFrequency s2) {
		Integer freq1 = s1.getFrequency();
		Integer freq2 = s2.getFrequency();

		// frequency descending, compareTo not working on primitives
		int compareFreq = Integer.valueOf(s2.getFrequency()).compareTo(Integer.valueOf(s1.getFrequency()));
		// Word ascending
		int compareWord = s1.getWord().compareTo(s2.getWord());
		if (compareFreq == 0) {
			return compareWord;
		} else {
			return compareFreq;
		}
    }};
    
    public String toString() {
    	return "freq: " + frequency + "; word:" + word;
    }	
}
