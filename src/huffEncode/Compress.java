package huffEncode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Makes a huffman dictionary from an input text file and writes it to a file at a given file name
 * 
 * @author Sean Cavalieri
 *
 */
public class Compress {

	public static void main(String[] args) {

		//Main class object
		Compress comp = new Compress();
		//start
		comp.mainCompressDecompress();
	}

	/**
	 * This does everything for this lab
	 */
	public void mainCompressDecompress() {

		boolean nonvalidEntries = true;
		//set up scanner
		Scanner s = new Scanner(System.in);
		while(nonvalidEntries) {
			//ask for functionality
			System.out.println("Would you like to compress or decompress a file? Type \"compress\" or \"decompress\"");
			String compDecomp = s.nextLine();
			if(compDecomp.replaceAll(" ", "").toLowerCase().equals("cancel")) {
				System.out.println("Haulting");
				s.close();
				return;
			} else if(compDecomp.replaceAll(" ", "").toLowerCase().equals("compress")) {
				//COMPRESSION (Getting text)

				System.out.println("What file would you like to compress?");
				String fi = s.nextLine();
				String theFi = fi;
				//check for " CaNCel  "
				String checker1 = fi.replaceAll(" ", "");
				if(checker1.toLowerCase().equals("cancel")) {
					System.out.println("Haulting");
					s.close();
					return;
				}
				System.out.println("What file would you like to compress to?");
				String compFi = s.nextLine();
				String theCompFi = compFi;
				//check for " CaNCel  "
				String checker2 = compFi.replaceAll(" ", "");
				if(checker2.toLowerCase().equals("cancel")) {
					System.out.println("Haulting");
					s.close();
					return;
				}
				File file;
				file = new File(theFi);
				//set up scanner to get data from file
				Scanner input;
				try {
					//continue set up to get data from file
					input = new Scanner(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					//				e.printStackTrace();
					System.out.println("File not Found");
					//start again
					continue;
				}
				//entries were valid, we can allow function to be done
				nonvalidEntries = false;
				s.close();
				//build string of message
				StringBuilder strB = new StringBuilder();
				//put in first line
				if(input.hasNextLine()) { strB.append(input.nextLine()); }
				while(input.hasNextLine()) {
					strB.append('\n');
					strB.append(input.nextLine());
				}

				//append end character
				strB.append((char)7);

				input.close();
				String str = strB.toString();
				//compress to binary file
				compressToFile(str, theCompFi);
				System.out.println("Done");

			} else if (compDecomp.replaceAll(" ", "").toLowerCase().equals("decompress")) {
				//DECOMPRESSION STUFF (Getting byte array)

				System.out.println("What file would you like to decompress?");
				String file = s.nextLine();
				String checker3 = file.replaceAll(" ", "");
				//check for cancellation
				if(checker3.toLowerCase().equals("cancel")) {
					System.out.println("Haulting");
					s.close();
					return;
				}
				System.out.println("What file would you like to decompress to?");
				String compFi = s.nextLine();
				String theCompFi = compFi;
				//check for " CaNCel  "
				String checker4 = compFi.replaceAll(" ", "");
				if(checker4.toLowerCase().equals("cancel")) {
					System.out.println("Haulting");
					s.close();
					return;
				}
				File fi = new File(file);
				//set up scanner to get data from file
				Scanner input;
				try {
					//continue set up to get data from file
					input = new Scanner(fi);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("File not Found");
					//start again
					continue;
				}
				//entries were valid, we can allow function to be done
				nonvalidEntries = false;
				//get message into bytes
				byte[] messageInBytes = readContentIntoByteArray(fi);
				StringBuilder message1 = new StringBuilder();
				for(Byte b : messageInBytes) {
					StringBuilder s2 = new StringBuilder(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
					message1.append(s2.reverse());
				}
				String message = message1.toString();

				//make HuffmanTree and decompress binary String to original text
				HuffmanTree HTree = new HuffmanTree();
				String finalMessage = HTree.decompress(message);

				writeToFile(finalMessage, theCompFi);
				System.out.println("Done");

				s.close();
				input.close();


			} else {
				System.out.println("Invalid Entry. Try again");
				//start again
				continue;
			}
		}

	}


	/**
	 * reads file and returns byte array of the contents. Method from StackOverflow
	 * 
	 * @param file the file to read from
	 * @return byte array of all the bytes in the file
	 */
	public byte[] readContentIntoByteArray(File file){
		FileInputStream fileInputStream = null;
		byte[] bFile = new byte[(int) file.length()];
		try
		{
			//convert file into array of bytes
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		}
		catch (Exception e)
		{
			//			e.printStackTrace();
		}
		return bFile;
	}



	/**
	 * Takes a string and compresses it to a file
	 * 
	 * @param str The string to compress
	 * @param theCompFi The path for the file to write to
	 */
	public void compressToFile(String str, String theCompFi) {

		HuffmanTree HTree = new HuffmanTree();
		//compress in HuffmanTree class
		byte[] bitRep = HTree.compress(str);
		//write to file
		try (FileOutputStream stream = new FileOutputStream(theCompFi)) {
			stream.write(bitRep);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
		}

	}

	/**
	 * Writes a String to file
	 * 
	 * @param str	The String to be written in
	 * @param theCompFi	The path of the file to be written to
	 */
	public void writeToFile(String str, String theCompFi) {
		try {
			PrintWriter pw = new PrintWriter(theCompFi);
			pw.println(str);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
		}
	}

	///**
	// * Takes a message and compresses it
	// * 
	// * @param str the message to compress
	// */
	//	public void compressForDict(String str, String theCompFi) {
	//		
	//		//map for frequencies
	//		HashMap<Character, Integer> freq = new HashMap<Character, Integer>();
	//		
	//		
	//		//make frequency table
	//		char[] line = str.toCharArray();
	//		for(char c : line) {
	//			if(freq.containsKey(c)) {
	//				int f = freq.get(c);
	//				freq.put(c, f+1);
	//			} else {
	//				freq.put(c, 1);
	//			}
	//		}
	//		//make Heap
	//		Heap<TNode> h = makeHeap(freq);
	//		//make Huffman Tree
	//		HuffmanTree HTree = makeHuffmanTree(h);
	//		//make file
	//		try {
	//			PrintWriter pw = new PrintWriter(theCompFi);
	//			pw.println("Dictionary");
	//			pw.println("------------");
	//			Object[] arr2 = HTree.makeDictionary().entrySet().toArray();
	//			for(Object o2 : arr2) {
	//				pw.println(o2.toString().substring(0, 1) + "     " + o2.toString().substring(2));
	//			}
	//			pw.close();
	//		} catch (FileNotFoundException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//	}

}
//target size: 6488666 -> 3683212
