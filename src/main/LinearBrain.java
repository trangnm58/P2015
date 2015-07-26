package main;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class LinearBrain extends Brain {
	Queue<GoodPos> goodPos = new LinkedList<GoodPos>();
	
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
					
					// add new pos
					addNewPos(firstPiece.topLeft.add(ref), firstPiece.botRight.add(ref));
					
					break;
				}
			}
			if (flag) break;
		}
		
		
		while (bucket.pieces.size() > 0) {
			Piece nextPiece = bucket.get(id++);
			// vector of all status of nextPiece
			Vector<Piece> pieces = nextPiece.parsePiece();

			// loop through all status
			for (int i=0; i < pieces.size(); i++) {
				boolean flag = false;
				GoodPos pos = findNextPos();
				if (pos != null) {
					GoodPos temp = pos;
					while (flag == false) {
						flag = move(pieces.elementAt(i), temp);
						if (flag == false) {
							temp = findNextPos();
							if (temp.equals(pos)) {
								goodPos.add(temp);
								break;
							}
						} else {
							break;
						}
					}
					if (flag == true) break;
				}
				// there is no good position
				else {
					break;
				}
			}
		}
		
		// write to file
		OStream.writeToFile(answer, board.empty);
	}

	// move all the way up from pos until valid or reach limit
	// and the move to the left until invalid
	private boolean move(Piece p, GoodPos pos) {
		Point ref = new Point(pos.x - p.topLeft.x, pos.y - p.topLeft.y);
		if (board.tPut(p, ref) == Board.OK) {
			foundPosition(p, ref);

			return true;
		} else if (board.tPut(p, ref) == Board.OVERLAP) {
			return moveOutOverlap(p, ref, pos);
		} else {
			return moveToTouch(p, ref, pos);
		}
	}
	
	private void addNewPos(Point topLeft, Point botRight) {
		for (int y=topLeft.y; y <= botRight.y; y++) {
			if (!board.isOccupied(botRight.x + 1, y)) {
				goodPos.add(new GoodPos(botRight.x + 1, y, "right"));
			}
			if (!board.isOccupied(topLeft.x - 1, y)) {
				goodPos.add(new GoodPos(topLeft.x - 1, y, "left"));
			}
		}
		for (int x=topLeft.x; x <= botRight.x; x++) {
			if (!board.isOccupied(x, botRight.y + 1)) {
				goodPos.add(new GoodPos(x, botRight.y + 1, "bottom"));
			}
			if (!board.isOccupied(x, topLeft.y - 1)) {
				goodPos.add(new GoodPos(x, topLeft.y - 1, "top"));
			}
		}
	}
	
	private GoodPos findNextPos() {
		if (goodPos.size() > 0) {
			GoodPos next = goodPos.remove();
			while (board.isOccupied(next.x, next.y) && goodPos.size() > 0) {
				next = goodPos.remove();
			}
			if (board.isOccupied(next.x, next.y)) {
				return null;
			}
			return next;
		} else return null;
	}
	
	private void foundPosition (Piece p, Point ref) {
		board.put(p, ref);
		
		// generate the answer
		answer.push(ref, p);
		
		// add new pos
		addNewPos(p.topLeft.add(ref), p.botRight.add(ref));
	}

	private boolean moveToTouch(Piece p, Point ref, GoodPos pos) {
		if (pos.type == "top") {
			if (moveDownLeft(p, ref, pos) == false) {
				goodPos.add(pos);
				return false;
			} else return true;
		}
		if (pos.type == "right") {
			if (moveLeftUp(p, ref,pos) == false) {
				goodPos.add(pos);
				return false;
			} else return true;
		}
		if (pos.type == "bottom") {
			if (moveUpLeft(p, ref, pos) == false) {
				goodPos.add(pos);
				return false;
			} else return true;
		}
		if (pos.type == "left") {
			if (moveRightUp(p, ref, pos) == false) {
				goodPos.add(pos);
				return false;
			} else return true;
		}
		return false;
	}
	
	private boolean moveOutOverlap(Piece p, Point ref, GoodPos pos) {
		boolean flag = false;
		// go to the right
		while (p.botRight.x + ref.x > pos.x) {
			ref.x--;
		}
		if (board.tPut(p, ref) != Board.OVERLAP && flag == false) {
			flag = moveRightUp(p, ref, pos);
			if (flag == true) return flag;
		}
		// go to the left
		while (p.botRight.x + ref.x > pos.x) {
			ref.x++;
		}
		if (board.tPut(p, ref) != Board.OVERLAP && flag == false) {
			flag = moveLeftUp(p, ref, pos);
			if (flag = true) return flag;
		}
		// go down
		while (p.botRight.y + ref.y > pos.y) {
			ref.y--;
		}
		if (board.tPut(p, ref) != Board.OVERLAP && flag == false) {
			flag = moveDownLeft(p, ref, pos);
			if (flag = true) return flag;
		}
		// go up
		while (p.botRight.y + ref.y > pos.y) {
			ref.y++;
		}
		if (board.tPut(p, ref) != Board.OVERLAP && flag == false) {
			flag = moveUpLeft(p, ref, pos);
			if (flag = true) return flag;
		}
		goodPos.add(pos);
		return false;
	}

	private boolean moveLeftUp(Piece p, Point ref, GoodPos pos) {
		while (board.tPut(p, ref) == Board.NO_TOUCH) {
			ref.x--;
		}
		if (board.tPut(p, ref) == Board.OK) {
			while (board.tPut(p, ref) == Board.OK) {
				ref.y--;
			}
			ref.y++;
			
			foundPosition(p, ref);
			
			return true;
		} else {
			ref.x++;
			while (board.tPut(p, ref) == Board.NO_TOUCH) {
				ref.y--;
			}
			if (board.tPut(p, ref) == Board.OK) {
				foundPosition(p, ref);
				
				return true;
			} else {
				return false;
			}
		}
	}
	
	private boolean moveRightUp(Piece p, Point ref, GoodPos pos) {
		while (board.tPut(p, ref) == Board.NO_TOUCH) {
			ref.x++;
		}
		if (board.tPut(p, ref) == Board.OK) {
			while (board.tPut(p, ref) == Board.OK) {
				ref.y--;
			}
			ref.y++;
			
			foundPosition(p, ref);
			
			return true;
		} else {
			ref.x--;
			while (board.tPut(p, ref) == Board.NO_TOUCH) {
				ref.y--;
			}
			if (board.tPut(p, ref) == Board.OK) {
				foundPosition(p, ref);
				
				return true;
			} else {
				return false;
			}
		}
	}
	
	private boolean moveDownLeft(Piece p, Point ref, GoodPos pos) {
		while (board.tPut(p, ref) == Board.NO_TOUCH) {
			ref.y++;
		}
		if (board.tPut(p, ref) == Board.OK) {
			while (board.tPut(p, ref) == Board.OK) {
				ref.x--;
			}
			ref.x++;
			
			foundPosition(p, ref);
			
			return true;
		} else {
			ref.y--;
			while (board.tPut(p, ref) == Board.NO_TOUCH) {
				ref.x--;
			}
			if (board.tPut(p, ref) == Board.OK) {
				foundPosition(p, ref);
				
				return true;
			} else {
				return false;
			}
		}
	}
	
	private boolean moveUpLeft(Piece p, Point ref, GoodPos pos) {
		while (board.tPut(p, ref) == Board.NO_TOUCH) {
			ref.y--;
		}
		if (board.tPut(p, ref) == Board.OK) {
			while (board.tPut(p, ref) == Board.OK) {
				ref.x--;
			}
			ref.x++;
			
			foundPosition(p, ref);
			
			return true;
		} else {
			ref.y++;
			while (board.tPut(p, ref) == Board.NO_TOUCH) {
				ref.x--;
			}
			if (board.tPut(p, ref) == Board.OK) {
				foundPosition(p, ref);
				
				return true;
			} else {
				return false;
			}
		}
	}
}

class GoodPos extends Point {
	public String type;
	
	GoodPos(int x, int y, String type) {
		super(x, y);
		this.type = type;
	}
	
	GoodPos(Point p, String type) {
		super(p);
		this.type = type;
	}
}