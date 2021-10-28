package huffEncode;

import java.util.BitSet;
import java.util.HashMap;

/**
 * Stores a Huffman Tree and is able to traverse it to create Huffman dictionary used for compression
 * 
 * @author Sean Cavalieri
 *
 */
public class HuffmanTree {

	private TNode root;

	public byte[] compress(String str) {

		//make frequency table
		HashMap<Character, Integer> freq = makeFreqTable(str);

		//make Heap
		Heap<TNode> h = makeHeap(freq);

		//build up huffman tree
		while(!h.isEmpty()) {
			TNode node1 = h.remove();
			if(!h.isEmpty()) {
				TNode node2 = h.remove();
				int newFreq = node1.getFreq() + node2.getFreq();
				TNode parent = new TNode('*', newFreq);
				parent.setLeft(node1);
				parent.setRight(node2);
				h.add(parent);
			} else {
				this.root = node1;
			}
		}

		//get dictionary
		HashMap<Character, String> compDict = makeDictionary();
		//Make compressed tree
		StringBuilder makeCompString = new StringBuilder();
		char[] mess = str.toCharArray();
		for(char c : mess) {
			makeCompString.append(compDict.get(c));
		}

		//make binary message
		String binaryString = makeCompString.toString();
		//get binary String for tree
		String compHuffTree = makeBinaryTree();
		//bitString to be made
		BitSet compMessage = new BitSet(binaryString.length());
		//combine and make bitset
		String whole = compHuffTree + binaryString;
		for(int i = 0; i < whole.length(); i++) {
			if(whole.charAt(i) == '1') {
				compMessage.set(i);
			}
		}
		//return finished byte array of compressed message and dictionary
		return compMessage.toByteArray();
	}
	private HashMap<Character, Integer> makeFreqTable(String str){

		HashMap<Character, Integer> freq = new HashMap<Character, Integer>();
		//make frequency table
		char[] line = str.toCharArray();
		for(char c : line) {
			if(freq.containsKey(c)) {
				int f = freq.get(c);
				freq.put(c, f+1);
			} else {
				freq.put(c, 1);
			}
		}
		return freq;	
	}
	//Makes a heap with TNode objects holding a char and a frequency
	private Heap<TNode> makeHeap(HashMap<Character, Integer> freq) {
		Heap<TNode> h = new Heap<TNode>();
		Object[] arr = freq.entrySet().toArray();
		for(Object o : arr) {
			String str = o.toString();
			TNode temp = new TNode(str.charAt(0), Integer.parseInt(str.substring(2)));
			h.add(temp);
		}
		return h;
	}

	/**
	 * decompress binary string containing dictionary and message into readable text
	 * 
	 * @param binaryString	The binary string holding the dictionary and message
	 * @return	A String of the original message
	 */
	public String decompress(String binaryString) {

		//build up tree and find message v. dictionary
		int ind = makeTreeFromBits(binaryString);
		//use ind to get text of the message
		String textInBinary = binaryString.substring(ind);
		//make text form dictionary and binary Message and return it
		return makeMessage(textInBinary);

	}

	//make dictionary encoding every character with an optimal binary string
	/**
	 * Make dictionary encoding of the huffman tree
	 * 
	 * @return HashMap<Character, String> dictionary of the chars with their binary encoding
	 */
	public HashMap<Character, String> makeDictionary(){
		HashMap<Character, String> dic = new HashMap<Character, String>();
		preOrder(dic, root, "");
		return dic;
	}
	//helper function does preorder traversal tracking path to make dictionary
	private void preOrder(HashMap<Character, String> dic, TNode node, String path) {
		if(node.hasRight()) {
		}
		if(!node.hasLeft() && !node.hasRight()) {
			//Base Case. Add to dictionary
			dic.put(node.getChar(), path);
		}
		if(node.hasLeft()) {
			preOrder(dic, node.getLeft(), (path + "0"));
		}
		if(node.hasRight()) {
			preOrder(dic, node.getRight(), (path + "1"));
		}

	}


	/**
	 * Make binary representation of a tree (as a string)
	 * 
	 * @return String of binary representation of tree
	 */
	public String makeBinaryTree() {
		StringBuilder bitString = new StringBuilder();
		//get string of binary for tree
		preOrderBinary(bitString, root);
		//return string
		return bitString.toString();
	}
	//helper method
	private void preOrderBinary(StringBuilder bitString, TNode node) {
		if(!node.hasLeft() && !node.hasRight()) {
			//"1" denotes a leaf, put in reverse 8 bit char representation (reverse because littleEndian)
			bitString.append("1");
			//Base Case. Add char
			char c = node.getChar();
			int theCharVal = (int)c;
			String stringyChar = Integer.toBinaryString(theCharVal);
			StringBuilder strB = new StringBuilder(stringyChar);
			while(strB.length() < 8) {
				strB.insert(0, "0");
			}
			bitString.append(strB.reverse());
		}
		//add 0 to show going down a level
		boolean beenCounted = false;
		if(node.hasLeft()) {
			bitString.append("0");
			beenCounted = true;
			preOrderBinary(bitString, node.getLeft());
		}
		if(node.hasRight()) {
			if(!beenCounted) { bitString.append("0"); }
			preOrderBinary(bitString, node.getRight());
		}

	}

	/**
	 * makes a huffman tree from the binary string representing it
	 * 
	 * @param bin Binary String holding the tree and a message after
	 * @return The index where the message part of the binary string starts
	 */
	public int makeTreeFromBits(String bin) {

		TNode r = new TNode('*', -1);
		int ind = buildPreOrder(0, r, bin);
		//set root;
		root = r;
		//ind is the index of next char after tree in string, return it
		return ind;

	}
	//build tree using preorder traversal. '0' means internal node, '1' means leaf and must store a char
	//returns the last index checked to keep track of where in binary string the next bit is
	private int buildPreOrder(int ind, TNode n, String bin) {

		//TODO: FINISH: Deal with double counting 0's
		if(bin.charAt(ind) == '0') {
			TNode left = new TNode('*', -1);
			n.setLeft(left);
			ind++;
			ind = buildPreOrder(ind, left, bin);
			TNode right = new TNode('*', -1);
			n.setRight(right);
			return buildPreOrder(ind, right, bin);
		} else {
			ind++;
			StringBuilder s2 = new StringBuilder(bin.substring(ind, ind+8));
			s2.reverse();
			String s = s2.toString();
			ind += 8;
			int val = Integer.parseInt(s, 2);
			char c = (char)val;
			n.setChar(c);
			return ind;
		}

	}

	/**
	 * Takes a binary string and decodes into into a message using the huffman tree
	 * 
	 * @param bin The string to decode
	 * @return The string message
	 */
	public String makeMessage(String bin) {

		StringBuilder decoded = new StringBuilder();
		Integer ind = 0;
		while(ind < bin.length()) {
			TNode track = root;
			//if has a left child in Huffman tree, it has a right child as well.
			while(track.hasLeft()) {
				if(bin.charAt(ind) == '0') {
					track = track.getLeft();
				} else {
					track = track.getRight();
				}
				ind++;
			}
			if((int)track.getChar() == 7) {
				//This is the end character, message is over
				break;
			}
			//if not done, add character and move on
			decoded.append(track.getChar());
		}
		return decoded.toString();

	}


	/**
	 * Sets the root of the HuffmanTree tree to a TNode
	 * 
	 * @param root The TNode to be set as the root of the tree
	 */
	public void setRoot(TNode root) { this.root = root; }

}