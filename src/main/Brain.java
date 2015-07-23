package main;

public abstract class Brain {
	public Board board;
	public Bucket bucket;
	public Answer answer = new Answer();
	
	public Brain(Board board, Bucket bucket) {
		this.board = board;
		this.bucket = bucket;
	}
	
	public abstract void think();
}
