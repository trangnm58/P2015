// Block.java

package main;

public class Block extends Point {
	public Block() {
		super();
	}
	
	public Block(int x, int y) {
		super(x, y);
	}
	
	public Block(Block b) {
		this(b.x, b.y);
	}
}
