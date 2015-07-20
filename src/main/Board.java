// Board.java

package main;

public class Board {
	public int[][] matrix;
	public Piece[] pieceStack;
	public Point[] posStack;
	public Point bottomRight;

	public int putted;
	public int empty;

	public static final int MAX_UNDO = 1024;
	public static final int STANDARD_WIDTH = 32;
	public static final int STANDARD_HEIGHT = 32;

	public Board() {
		this.matrix = new int[STANDARD_WIDTH + 14][STANDARD_HEIGHT + 14]; // construct with 4 edges of 7
		this.pieceStack = new Piece[MAX_UNDO];
		this.posStack = new Point[MAX_UNDO];

		for (int i = 0; i < STANDARD_WIDTH; i++) {
			for (int j = 0; j < STANDARD_HEIGHT; j++) {
				if (i < 7 ||
				j < 7 ||
				i > STANDARD_WIDTH + 6 ||
				j > STANDARD_HEIGHT + 6) {
					this.matrix[i][j] = 1;
				} else {
					this.matrix[i][j] = 0;
				}
			}
		}

		this.bottomRight = new Point(STANDARD_WIDTH + 7, STANDARD_WIDTH + 7);
		this.putted = 0;
		this.empty = STANDARD_WIDTH * STANDARD_HEIGHT;
	}

	public Board(Point[] input) {
		this();

		for (Point pos: input) {
			this.matrix[pos.x][pos.y] = 1;
			this.empty--;
		}

		// find bottom right
		boolean found = false;
		for (int i = STANDARD_WIDTH + 7; i >= 7; i--) {
			for (int j = STANDARD_WIDTH + 7; i >= 7; j--) {
				if (this.matrix[i][j] == 0) {
					this.bottomRight = new Point(i, j);
					found = true;
					break;
				}
			}

			if (found) {
				break;
			}
		}
	}

	public void put(Piece p, Point pos) {
		for (Block part: p.body) {
			this.matrix[7 + pos.x + part.x][7 + pos.y + part.y] = p.id;
			this.empty--;
		}

		this.pieceStack[this.putted] = new Piece(p);
		this.posStack[this.putted] = new Point(pos);
		this.putted++;
	}

	public boolean tPut(Piece p, Point pos) {
		boolean found = false;
		for (Block part: p.body) {
			// check put on existed point
			if (this.matrix[7 + pos.x + part.x][7 + pos.y + part.y] != 0) {
				return false;
			}

			// look for contact piece
			int top = this.matrix[7 + pos.x + part.x][6 + pos.y + part.y];
			int bottom = this.matrix[7 + pos.x + part.x][8 + pos.y + part.y];
			int left = this.matrix[6 + pos.x + part.x][7 + pos.y + part.y];
			int right = this.matrix[8 + pos.x + part.x][7 + pos.y + part.y];
			if ((top > 1	&& top != p.id) ||
				(bottom > 1 && bottom != p.id) ||
				(left > 1 	&& left != p.id) ||
				(right > 1 	&& right != p.id)) {
				found = true;
			}
		}

		return found;
	}

	public boolean unPut(int pos) {
		if (pos < putted && pos >= 0) {
			this.putted--;
			for (Block part: this.pieceStack[pos].body) {
				this.matrix[7 + this.posStack[pos].x + part.x][7 + this.posStack[pos].y + part.y] = 0;
				this.empty++;
			}

			for (int i = pos; i < putted; i++) {
				this.pieceStack[i] = this.pieceStack[i + 1];
				this.posStack[i] = this.posStack[i + 1];		
			}

			this.pieceStack[this.putted] = null;
			this.posStack[this.putted] = null;
			return true;
		} else {
			return false;
		}
	}

	public boolean unPut(Piece p) {
		for (int i = 0; i < putted; i++) {
			if (p.equals(this.pieceStack[i])) {
				return unPut(i);
			}
		}
		return false;
	}

	public boolean undo() {
		return unPut(putted - 1);
	}
}
