package main;

public class StackNode {
	private char value;
	private StackNode next;
	
	public StackNode(char value) {
		this.setValue(value);
	}

	public char getValue() {
		return value;
	}

	public void setValue(char value) {
		this.value = value;
	}

	public StackNode getNext() {
		return next;
	}

	public void setNext(StackNode next) {
		this.next = next;
	}
}
