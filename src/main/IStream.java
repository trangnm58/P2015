// IStream.java

package main;

import java.io.File;
import java.util.Scanner;
import java.util.Stack;
import java.lang.String;

public class IStream {
	public Board oBoard;
	public Bucket oBucket;

	public IStream(String name) throws Exception {
		File input = new File(name);
		Scanner sc = new Scanner(input);
		Stack temp;
		String read;

		// read board
		temp = new Stack();
		for (int i = 0; i < 32; i++) {
			read = sc.nextLine();
			for (int j = 0; j < 32; j++) {
				if (read.charAt(j) == '1') {
					temp.push(new Block(i, j));
				}
			}
		}
		this.oBoard = new Board(temp);

		// read piece
		int num = sc.nextInt();
		for (int i = 0; i < num; i++) {
			// read 1 piece
			temp = new Stack();
			read = sc.nextLine(); // skip 1 empty line in template
			for (int j = 0; j < 8; j++) {
				read = sc.nextLine();
				for (int k = 0; k < 8; k++) {
					if (read.charAt(k) == '1') {
						temp.push(new Block(j, k));
					}
				}
			}
			this.oBucket.push(new Piece(temp));
		}

		if (sc != null) sc.close();	
	}
}