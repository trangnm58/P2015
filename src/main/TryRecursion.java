package main;

public class TryRecursion {

	public static void main(String[] args) throws Exception {
		// define Board object
		Board board;
		// define Bucket object
		Bucket bucket = new Bucket();
		
		// read quest file and put into Board + Bucket
		IStream in = new IStream("Medium.txt", bucket);
		board = IStream.oBoard;
		
		// define Brain object
		RecursionBrain brain = new RecursionBrain(board, bucket);
		System.out.println("Thinking");
		brain.think();
		
		System.out.println("Done ");
	}
}
