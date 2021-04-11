package nl.krebos.poc.wordcount;

public class RequestMostFrequentNWords {
	private String text;
	private int n;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
}
