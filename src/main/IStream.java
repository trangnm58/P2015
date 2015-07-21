// IStream.java

package main;

import java.io.File;
import java.util.Scanner;
import java.util.Stack;
import java.lang.String;

public class IStream {
	public static Board oBoard;
	
	public IStream(String name, Bucket oBucket) throws Exception {
		File input = new File(name);
		Scanner sc = new Scanner(input);
		Stack<Block> temp;
		String read;

		// read board
		temp = new Stack<Block>();
		for (int i = 0; i < 32; i++) {
			read = sc.nextLine();
			for (int j = 0; j < 32; j++) {
				if (read.charAt(j) == '1') {
					temp.push(new Block(i, j));
				}
			}
		}
		
		// creat a board
		oBoard = new Board(temp);

		// read piece
		int num = sc.nextInt();
		int id = 2; // start id is 2
		for (int i = 0; i < num; i++) {
			// read 1 piece
			temp = new Stack<Block>();
			read = sc.nextLine(); // skip 1 empty line in template
			for (int j = 0; j < 8; j++) {
				read = sc.nextLine();
				for (int k = 0; k < 8; k++) {
					if (read.charAt(k) == '1') {
						temp.push(new Block(j, k));
					}
				}
			}
			// convert temp to array of Block
			Block[] body = new Block[temp.size()];
			for (int k=0; k < body.length; k++) {
				body[k] = temp.pop();
			}
			oBucket.push(new Piece(body, id++));
		}

		if (sc != null) sc.close();	
	}
}