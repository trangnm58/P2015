// OStream.java

package main;

import java.util.Formatter;

/**
 * OStream is used to print the answer to txt file
 * @author Page
 */

public class OStream {

	public static Formatter formatter;

	public static void writeToFile(Answer ans) {
		try {
			formatter = new Formatter("answer.txt");
		} catch (Exception e) {
			System.out.println("Can't create file");
		}
		
		// write to file
		for (int i=2; i < IStream.numOfPieces + 2; i++) {
			Line line = ans.lines.get(i);
			if (line != null) {
				formatter.format("%s %s %s %s%n", line.ref.x, line.ref.y, line.side, line.angle);
			} else {
				formatter.format("%n");
			}
		}
		
		// close file
		formatter.close();
	}
}
