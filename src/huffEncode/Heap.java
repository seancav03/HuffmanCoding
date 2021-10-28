package huffEncode;

import java.util.ArrayList;

/**
 * A Heap data structure, also known as a priority queue, used in huffman compression
 * 
 * @author Sean Cavalieri
 *
 * @param <T> The kind of node the heap stores
 */
public class Heap<T extends Comparable<T>> {

	private ArrayList<T> nodes;

	/**
	 * Creates a Heap with a starting size
	 * 
	 * @param startSize the initial size of the heap
	 */
	public Heap(int startSize) {
		nodes = new ArrayList<T>(startSize);
	}

	/**
	 * Creates a Heap with default starting size
	 */
	public Heap() {
		nodes = new ArrayList<T>();
	}


	//peek looks at root. Returns null if Heap is empty
	/**
	 * Looks at the root of the heap
	 * 
	 * @return the root node of the heap
	 */
	public T peek() {
		if(nodes.size() >= 1) {
			return nodes.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Removes and returns the root of the heap, then heapifies down to fix tree
	 * 
	 * @return the node removed from the heap
	 */
	public T remove() {
		if(nodes.size() == 0) { return null; }
		T result = nodes.get(0);
		this.swap(0, nodes.size()-1);
		nodes.remove(nodes.size()-1);
		int ind = 0;
		//heapify down
		while(this.hasLC(ind)) {
			int smaller = this.getLCInd(ind);
			if(this.hasRC(ind) && this.getRC(ind).compareTo(this.getLC(ind)) < 0) {
				smaller = this.getRCInd(ind);
			}
			//TODO: LINE BELOW, is the <= or just <? What is convention?
			if(nodes.get(ind).compareTo(nodes.get(smaller)) <= 0){
				break;
			}
			this.swap(ind, smaller);
			ind = smaller;
		}
		return result;
	}

	//adds node and fixes tree
	/**
	 * Adds node to the heap, then heapifies up to fix tree
	 * 
	 * @param n node to be added
	 */
	public void add(T n) {
		nodes.add(n);
		int ind = nodes.size()-1;
		//heapify up
		while(this.hasPar(ind)) {
			//TODO: < or <=?
			if(nodes.get(ind).compareTo(this.getPar(ind)) < 0) {
				this.swap(ind, this.getParInd(ind));
				ind = this.getParInd(ind);
			} else {
				break;
			}
		}
	}

	//checks if empty
	public boolean isEmpty() {
		return nodes.size() == 0;
	}

	//helper function swaps two nodes at indexes
	private void swap(int a, int b) {
		T temp = nodes.get(a);
		nodes.set(a, nodes.get(b));
		nodes.set(b, temp);
	}

	//helper functions take care of conversions to array indexes
	private int getLCInd(int parInd) { return (parInd * 2) + 1; }
	private int getRCInd(int parInd) { return (parInd * 2) + 2; }
	private int getParInd(int CInd) { return (CInd - 1) / 2; }
	//get items
	private T getLC(int parInd) { return nodes.get(this.getLCInd(parInd)); }
	private T getRC(int parInd) { return nodes.get(this.getRCInd(parInd)); }
	private T getPar(int CInd) { return nodes.get(this.getParInd(CInd)); }
	//check tree
	private boolean hasLC(int parInd) { return this.getLCInd(parInd) < nodes.size(); }
	private boolean hasRC(int parInd) { return this.getRCInd(parInd) < nodes.size(); }
	private boolean hasPar(int CInd) { return CInd != 0; }

}
