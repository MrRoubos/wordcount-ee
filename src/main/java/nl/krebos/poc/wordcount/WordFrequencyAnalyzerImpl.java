package nl.krebos.poc.wordcount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("calculate")
public class WordFrequencyAnalyzerImpl implements WordFrequencyAnalyzer{
	Map <String, WordFrequencyImpl> wordMap = new HashMap<>();
	private String text;

	
	@GET
	public Response getAlive() {
		return Response.ok("alive").build();
	}
	
	/**
	 * CalculateHighestFrequency should return the highest frequency in the text (several
	 * words might have this frequency)
	 */
	@POST
	@Path("highestfrequency")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response calculateHighestFrequency(String text) {
		int freq = 0;
		if (text != null) {		
			convertTextToWords(text);

			// Find word with highest frequency
			if (! wordMap.isEmpty() ) {
				int highest = 0;
				for (WordFrequencyImpl wordFrq : wordMap.values()) {
					if (wordFrq.getFrequency() > highest) {
						highest = wordFrq.getFrequency();
					}
				}
				freq = highest;
			}
		}
		
        JsonObject result = Json.createObjectBuilder()
	          .add("frequency", Integer.valueOf(freq)).build();
		return Response.ok(result).build();
	}

	/**
	 * CalculateFrequencyForWord should return the frequency of the specified word
	 */
	@POST
	@Path("frequencyforword")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response calculateFrequencyForWord(RequestFrequencyForWord request) {
		int freq = 0;
		if (request.getText() != null && request.getWord() != null) {		
			convertTextToWords(request.getText());
			
			// Find word with highest frequency
			if (! wordMap.isEmpty() ) {
				WordFrequencyImpl wordFreq = wordMap.get(request.getWord());
				if (wordFreq !=  null) {
					freq = wordFreq.getFrequency();
				}
			}			
		}
        JsonObject result = Json.createObjectBuilder()
  	          .add("frequency", Integer.valueOf(freq)).build();		
		return Response.ok(result).build();			
	}

	/**
	 * CalculateMostFrequentNWords should return a list of the most frequent „n‟ words inthe input text, 
	 * all the words returned in lower case. If several words have the samefrequency, this method should 
	 * return them in ascendant alphabetical order (for inputtext “The sun shines over the lake” and n = 3, 
	 * it should return the list {(“the”, 2),(“lake”, 1), (“over”, 1) }
	 */
	@POST
	@Path("mostfrequentnwords")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response calculateMostFrequentNWords(RequestMostFrequentNWords request) {
		List<WordFrequency> wordList= null;
		JsonArrayBuilder outerArray = Json.createArrayBuilder();		
		if (request.getText() != null &&  request.getN() > 0) {		
			convertTextToWords(request.getText());
			
			if (! wordMap.isEmpty() ) {
				wordList = new ArrayList<>(); 
				for (WordFrequencyImpl wordFrq : wordMap.values()) {
					wordList.add(wordFrq);
				}
				wordList.sort(WordFrequencyImpl.WordFreqComparator);
				//Collections.sort(wordList, WordFrequencyImpl.WordFreqComparator);
				if (wordList.size() > request.getN()) {
					List<WordFrequency> shortWordList= new ArrayList<>();
					for (int i = 0 ; i < request.getN(); i++) {
						shortWordList.add(wordList.get(i));
					}
					wordList = shortWordList;
				}

				for (WordFrequency wordFreq : wordList) {			
					JsonArrayBuilder innerArray = Json.createArrayBuilder();
					innerArray.add(wordFreq.getWord());			
					innerArray.add(wordFreq.getFrequency());
					outerArray.add(innerArray);
				}						
			}
		}
        JsonObject result = Json.createObjectBuilder()                
    	          .add("wordlist", outerArray).build();
       
  		return Response.ok(result).build();			

	}	
	/**
	 * Process the incoming text and construct a List/Map with 
	 * the words and frequencies. REST so no state/cache
	 * @param text2
	 */
	private void convertTextToWords(String text2) {	
		text = text2.trim().toLowerCase();

		int startIndex = 0;
		boolean wordFound = false;
		if (text.length() > 0) {
			WordFrequencyImpl wordFreq = null;			
			for (int i=0; i < text.length(); i++) {
				if (validChar(text.charAt(i))) {
					if (! wordFound) {
						wordFound = true;
						startIndex = i;
						wordFreq = new WordFrequencyImpl();						
					}
				} else {
					// We found an invalid word character, so the word is complete
					if (wordFound) {
						wordFound = false;
						String word = text.substring(startIndex, i);
						storeWord(word);
						startIndex = 0;
					}
				}
			}
			
			// Ensure the last word is also stored
			if (wordFound) {
				String word = text.substring(startIndex);
				storeWord(word);
			}
		}				
	}		

	/**
	 * Only characters between a and z are valid.  
	 * @param c
	 * @return
	 */
	private boolean validChar(char c) {
		if (c >= 'a' && c <= 'z') {
			return true;
		} else {
			return false;
		}
	}

	
	/**
	 * Check if word already present, if not add word.
	 * @param word
	 */
	private void storeWord(String word) {
		WordFrequencyImpl wordFreq = wordMap.get(word);
		if (wordFreq == null) {
			wordFreq = new WordFrequencyImpl();
			wordFreq.setWord(word);
			wordMap.put(word, wordFreq);
		} else {
			wordFreq.increaseFrequency();
		}						
	}	
}
