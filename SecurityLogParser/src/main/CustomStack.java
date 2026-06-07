package main;

public class CustomStack {
	private StackNode top = null;
	private int size = 0;
	
	public void push(char val) {
		StackNode newNode = new StackNode(val);
		newNode.setNext(top);
		top = newNode;
		size++;
	}
	
	public char pop() {
		if (isEmpty()) {
			throw new IllegalStateException("Stack Underflow: Cannot pop from an empty stack.");
		}
		
		char poppedValue = top.getValue();
		top = top.getNext();
		size--;
		return poppedValue;
	}
	
	public boolean isEmpty() {
		return top == null;
	}
	
	public int getSize() {
		return size;
	}
}
