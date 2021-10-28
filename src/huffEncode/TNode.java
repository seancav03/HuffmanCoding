package huffEncode;

/**
 * Node of a binary tree storing a character and a frequency associated with that character
 * 
 * @author Sean Cavalieri
 *
 */
public class TNode implements Comparable<TNode> {


	private char character;
	//freq must be private as input can't be negative
	private int freq;

	private TNode left;
	private TNode right;

	/**
	 * creates a TNode object. A TNode stores and char and a freqency for that char, and two child nodes, left and right.
	 * 
	 * @param character The initial char for the TNode
	 * @param freq The initial frequency for that character
	 */
	public TNode(char character, int freq) {
		this.character = character;
		this.freq = freq;
	}

	/**
	 * checks if it is a positive number, and sets the frequency of the char in the node if it is.
	 * 
	 * @param nFreq the new frequency to be inputed
	 * @return returns true if successfully changed, false if not (value was less than 0)
	 */
	public boolean setFreq(int nFreq) {
		if(nFreq >= 0) {
			freq = nFreq;
			return true;
		} else {
			return false;
		}
	}

	//BELOW: Getters and Setters and Checkers
	/**
	 * gets the frequency for the char in node
	 * 
	 * @return integer of frequency of char in node
	 */
	public int getFreq() { return freq; }

	/**
	 * sets character in node
	 * 
	 * @param n character to store in node
	 */
	public void setChar(char n) { character = n; }

	/**
	 * gets character stored in node
	 * 
	 * @return char in node
	 */
	public char getChar() { return character; }

	/**
	 * gets the left child of this node
	 * 
	 * @return the left child, or null if there is no left child
	 */
	public TNode getLeft() { return left; }

	/**
	 * gets the right child of this node
	 * 
	 * @return the right child, or null if there is no right child
	 */
	public TNode getRight() { return right; }

	/**
	 * sets the left child of this node
	 * 
	 * @param n the node to be set as left child
	 */
	public void setLeft(TNode n) { left = n; }

	/**
	 * sets the right child of this node
	 * 	
	 * @param n the node to be set as right child
	 */
	public void setRight(TNode n) { right = n; }

	/**
	 * checks if the node has a left child
	 * 
	 * @return returns true if left child exists, false if there is no left child
	 */
	public boolean hasLeft() { return left != null; }

	/**
	 * checks if the node has a right child
	 * 
	 * @return returns true if right child exists, false if there is no right child
	 */
	public boolean hasRight() { return right != null; }

	@Override
	public int compareTo(TNode o) {
		return this.getFreq() - o.getFreq();
	}

}
