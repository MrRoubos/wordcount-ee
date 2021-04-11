package nl.krebos.poc.wordcount;

public class RequestFrequencyForWord {
	private String text; 
	private String word;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
}
