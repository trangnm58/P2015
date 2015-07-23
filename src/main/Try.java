package main;

public class Try {

	public static void main(String[] args) throws Exception {
		// define Board object
		Board board;
		// define Bucket object
		Bucket bucket = new Bucket();
		
		// read quest file and put into Board + Bucket
		IStream in = new IStream("quest3.txt", bucket);
		board = IStream.oBoard;
		
		// define Brain object
		LinearBrain brain = new LinearBrain(board, bucket);
		brain.think();
		
		for (int i=0; i < 39; i++) {
			for (int j=0; j < 39; j++) {
				if (board.matrix[j][i] < 10)
				System.out.print(board.matrix[j][i] + " ");
				else System.out.print(board.matrix[j][i]);
			}
			System.out.println();
		}
	}
}
