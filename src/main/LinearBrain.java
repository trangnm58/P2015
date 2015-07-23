package main;

import java.util.LinkedList;
import java.util.Queue;

public class LinearBrain extends Brain {
	Queue<Point> goodPos = new LinkedList<Point>();
	
	public LinearBrain(Board board, Bucket bucket) {
		super(board, bucket);
	}
	
	public void think() {
		int id = 2;
		Piece firstPiece = bucket.get(id++);
		// put the first piece
		for (int i=0; i < Board.STANDARD_HEIGHT; i++) {
			boolean flag = false;
			for (int j=0; j < Board.STANDARD_WIDTH; j++) {
				Point ref = new Point(j - firstPiece.topLeft.x, i - firstPiece.topLeft.y);
				if (board.tPut(firstPiece, ref) == Board.OK) {
					board.put(firstPiece, ref);
					// generate the answer
					answer.push(ref, firstPiece);
					flag = true;
					
					// add 2 new goodPos
					Point p1 = new Point(firstPiece.botRight.x + 1 + ref.x, firstPiece.topLeft.y + ref.y);
					Point p2 = new Point(firstPiece.topLeft.x + ref.x, firstPiece.botRight.y + 1 + ref.y);
					goodPos.add(p1);
					goodPos.add(p2);
					break;
				}
			}
			if (flag) break;
		}
		
		while (bucket.pieces.size() > 0) {
			Piece nextPiece = bucket.get(id++);
			Point pos = goodPos.remove();
			move(nextPiece, pos);
		}
		
		// write to file
		OStream.writeToFile(answer, board.empty);
	}

	// move all the way up from pos until valid or reach limit
	// and the move to the left until invalid
	private void move(Piece p, Point pos) {
		Point ref = new Point(pos.x - p.topLeft.x, pos.y - p.topLeft.y);
		if (board.tPut(p, ref) == Board.OVERLAP) {
			// move this good position to the end of the queue
			goodPos.add(pos);
			move2(p, goodPos.remove(), pos);
		} else {
			while (board.tPut(p, ref) == Board.NO_TOUCH) {
				ref.y--;
			}
			if (board.tPut(p, ref) != Board.OK)
				ref.y++;
			while (board.tPut(p, ref) != Board.OVERLAP) {
				ref.x--;
			}
			ref.x++;
			board.put(p, ref);

			// generate the answer
			answer.push(ref, p);

			// add 2 new goodPos
			Point p1 = new Point(p.botRight.x + 1 + ref.x, p.topLeft.y + ref.y);
			Point p2 = new Point(p.topLeft.x + ref.x, p.botRight.y + 1 + ref.y);
			goodPos.add(p1);
			goodPos.add(p2);
		}
	}
	
	private void move2(Piece p, Point pos, Point pos2) {
		if (!pos.equals(pos2)) {
			Point ref = new Point(pos.x - p.topLeft.x, pos.y - p.topLeft.y);
			if (board.tPut(p, ref) == Board.OVERLAP) {
				// move this good position to the end of the queue
				goodPos.add(pos);
				move2(p, goodPos.remove(), pos2);
			} else {
				while (board.tPut(p, ref) == Board.NO_TOUCH) {
					ref.y--;
				}
				if (board.tPut(p, ref) != Board.OK)
					ref.y++;
				while (board.tPut(p, ref) != Board.OVERLAP) {
					ref.x--;
				}
				ref.x++;
				board.put(p, ref);

				// generate the answer
				answer.push(ref, p);

				// add 2 new goodPos
				Point p1 = new Point(p.botRight.x + 1 + ref.x, p.topLeft.y + ref.y);
				Point p2 = new Point(p.topLeft.x + ref.x, p.botRight.y + 1 + ref.y);
				goodPos.add(p1);
				goodPos.add(p2);
			}
		}
	}
}
