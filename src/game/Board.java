package game;

import java.util.Arrays;

public final class Board {

	public char[][] squares = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};
	
	public Board(){
		
	}
	
	public Board copy(){
		Board board = new Board();
		for(int rowNum = 0; rowNum < this.squares.length; rowNum ++){
			for(int colNum = 0; colNum < this.squares.length; colNum ++){
				board.squares[rowNum][colNum] = String.valueOf(this.squares[rowNum][colNum]).charAt(0);
			}
		}
		return board;
	}
	
	public String toString(){
		String board = "+-------+-------+-------+\n";
		for(char[] row: this.squares){
			board = board.concat("|       |       |       |\n|");
			for(char square: row){
				board = board.concat("   " + square + "   |");
			}
			board = board.concat("\n|       |       |       |\n+-------+-------+-------+\n");
		}
		return board;
	}
}
