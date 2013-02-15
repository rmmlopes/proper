package ca.ruiandjenn.proper.exceptions;

public class DuplicateKeyException extends Exception {
	String key;
	
	public DuplicateKeyException(String key) {
		this.key = key;
	}
}
