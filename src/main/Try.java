package main;

public class Try {

	public static void main(String[] args) throws Exception {
		// define Board object
		Board board;
		// define Bucket object
		Bucket bucket = new Bucket();
		
		// read quest file and put into Board + Bucket
		IStream in = new IStream("quest1.txt", bucket);
		board = in.oBoard;
		
		// define Brain object
		Brain brain = new Brain(board, bucket);
		brain.think();
		
		for (int i=0; i < 46; i++) {
			for (int j=0; j < 46; j++) {
				System.out.print(board.matrix[i][j]);
			}
			System.out.println();
		}
	}

}
