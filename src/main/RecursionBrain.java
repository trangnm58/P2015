// RecursionBrain.java

package main;

import java.util.Vector;
import java.util.Random;

public class RecursionBrain extends Brain {
	private int minPoint;
	private int minPiece;

	public RecursionBrain(Board board, Bucket bucket) {
		super(board, bucket);

		this.minPoint = 1024;
		this.minPiece = 1024;
	}

	public void think() {
		Board cBoard = (Board) this.board.clone();
		this.think(cBoard, this.bucket);
	}

	public void think(Board board, Bucket bucket) {
		if (bucket.pieces.size() == 0 || board.empty == 0
				|| board.empty < bucket.minSize) {
			if (board.empty < this.minPoint || board.empty == this.minPoint
					&& board.putted < this.minPiece) {
				this.minPoint = board.empty;
				this.minPiece = board.putted;

				this.answer = new Answer(board);
				// write to file
				OStream.writeToFile(this.answer, this.minPoint);
			}
			// else do nothing
		} else {
			Piece newPiece = bucket.pop();
			// get all state of this piece
			Vector<Piece> allState = newPiece.parsePiece();
			// create a random generator
			Random randomGenerator = new Random();

			int randomUse = randomGenerator.nextInt(200);
			if (randomUse >= 20) {
				// in case of use the piece, ask the board for all
				// available point to put this state
				while (!allState.isEmpty()) {
					int randomState = randomGenerator.nextInt(allState.size());
					Piece aState = allState.remove(randomState);
					Vector<Point> allPosition = board.where(aState);

					while (!allPosition.isEmpty()) {
						// put random in state and continue to think
						int randomInt = randomGenerator.nextInt(allPosition
								.size());
						Point temp = allPosition.remove(randomInt);

						Board cBoard = (Board) board.clone();
						cBoard.put(aState, temp);
						this.think(cBoard, bucket);
					}
				}
				// in case of not use this piece, continue to think
				Board cBoard = (Board) board.clone();
				this.think(cBoard, bucket);
			} else {
				// in case of not use this piece, continue to think
				Board cBoard = (Board) board.clone();
				this.think(cBoard, bucket);
				// in case of use the piece, ask the board for all
				// available point to put this state
				while (!allState.isEmpty()) {
					int randomState = randomGenerator.nextInt(allState.size());
					Piece aState = allState.remove(randomState);
					Vector<Point> allPosition = board.where(aState);

					while (!allPosition.isEmpty()) {
						// put random in state and continue to think
						int randomInt = randomGenerator.nextInt(allPosition
								.size());
						Point temp = allPosition.remove(randomInt);

						cBoard = (Board) board.clone();
						cBoard.put(aState, temp);
						this.think(cBoard, bucket);
					}
				}
			}
			bucket.push(newPiece);
		}
	}
}
