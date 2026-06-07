package main;

public class IngestionQueue {
	
	private Node head = null;
	private Node tail = null;
	private int size = 0;
	
	public void enqueue(String rawLog) {
		Node newNode = new Node(rawLog);
	
		if (size == 0) {
			head = newNode;
			tail = newNode;
		} else {
			tail.setNext(newNode);
			newNode.setPrev(tail);
			tail = newNode;
		}
		
		size++;
	}
	
	public String dequeue() {
		if (size == 0) {
			return null;
		}
		
		String frontLog = head.getData();
		if (size == 1) {
			head = null;
			tail = null;
		} else {
			head = head.getNext();
			head.setPrev(null);
		}
		size--;
		
		return frontLog;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public boolean isEmpty() {
		return this.size == 0;
	}
}
