package ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import game.Board;
import game.Game;
import game.Player;

public class Node {
	
	private char team;
	public String square;
	public Game state = new Game();
	public int score;
	public int turn;
	public Tree hostTree;
	public Node parentNode;
	public ArrayList<Node> children = new ArrayList<Node>();
	
	private HashMap<Character, Integer> scoresMap = new HashMap<Character, Integer>();
	
	public Node(Game last_state, int rowNum, int colNum, char team, Tree hostTree, Node parentNode){
		this.state = last_state.copy();
		this.team = team;
		this.square = rowNum + "" + colNum;
		this.hostTree = hostTree;
		
		this.setScoresMap();
		this.placePeice();
		this.setScore();
		
		this.hostTree.count ++;
	}
	
	public Node(Game currentState){
		this.state = currentState;
		this.team = team;
		this.hostTree = hostTree;
		this.score = 0;
	}
	
	public void generateNodes(){
		if(this.score == 0){
			for(int rowNum = 0; rowNum < this.state.board.squares.length; rowNum ++){
				for(int colNum = 0; colNum < this.state.board.squares.length; colNum ++){
					if(this.state.board.squares[rowNum][colNum] == ' '){
						Node choice = new Node(this.state, rowNum, colNum, this.team, this.hostTree, this);
						choice.generateNodes();
						this.children.add(choice);
					}
				}
			}
		}
	}
	
	public int indexInChildrenOf(int rowNum, int colNum){
		//finds the child that made the move of the given row and column
		for(int i = 0; i < this.children.size(); i++){
			Node child = this.children.get(i);
			if(child.state.board.squares[rowNum][colNum] != ' '){
				for(int j = 0; j < this.children.size(); j ++){
					if(this.children.get(j).state.board.squares[rowNum][colNum] == child.state.board.squares[rowNum][colNum] && j != i){
						return -1;
					}
				}
				return i;
			}
		}
		return -2;
	}
	
	public int totalBranchFromHere(){
		int total = this.score;
		for(Node child : this.children){
			total += child.score;
			if(child.children.size() > 0){
				total += child.totalBranchFromHere();
			}
		}
		return total;
	}
	
	public void setScoresMap(){
		if(this.team == 'X'){
			this.scoresMap.put('X', 10);
			this.scoresMap.put('O', -10);
			this.scoresMap.put('C', 5);
			this.scoresMap.put('N', 0);
		}
		else {
			this.scoresMap.put('O', 10);
			this.scoresMap.put('X', -10);
			this.scoresMap.put('C', 5);
			this.scoresMap.put('N', 0);
		}
	}
	
	public void placePeice(){
		Player playerToMove;
		if(this.state.turn%2 == 0){
			playerToMove = this.state.player1;
		}
		else {
			playerToMove = this.state.player2;
		}
		playerToMove.move(square, this.state);
		this.state.turn ++;
	}
	
	public String childrenToString(){
		String str = "";
		str = str + "================================================================\n";
		for(Node child : this.children){
			str = str + "" + Arrays.deepToString(child.state.board.squares) + "\n";
		}
		str = str + "================================================================\n";
		str = str + "================================================================\n";
		return str;
	}
	
	public void setScore(){
		score = scoresMap.get(this.state.checkWin());
	}
	
	public String toString(){
		return this.team + ": " + this.square + " for " + this.score + " points ----------- Total: " + this.hostTree.count;
	}
}
