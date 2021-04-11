package nl.krebos.poc.wordcount;

import javax.ws.rs.core.Response;

public interface WordFrequencyAnalyzer {
	Response calculateHighestFrequency(String text);
	Response calculateFrequencyForWord (RequestFrequencyForWord request);
	Response calculateMostFrequentNWords (RequestMostFrequentNWords request);
}
