package ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import game.Board;
import game.Game;
import game.Player;

public class Node implements Comparable<Node>{
	
	private char team;
	public String square;
	public Game state = new Game();
	public int score;
	public int turn;
	public Tree hostTree;
	public Node parentNode;
	public int depth;
	public ArrayList<Node> children = new ArrayList<Node>();
	
	private HashMap<Character, Integer> scoresMap = new HashMap<Character, Integer>();
	
	public Node(int depth, Game last_state, int rowNum, int colNum, char team, Tree hostTree, Node parentNode){
		this.state = last_state.copy();
		this.team = team;
		this.square = rowNum + "" + colNum;
		this.hostTree = hostTree;
		this.depth = depth + 1;
		if(this.depth > this.hostTree.maxDepth){
			this.hostTree.maxDepth = this.depth;
		}
		
		this.setScoresMap();
		this.placePeice();
		this.setScore();
		
		this.hostTree.count ++;
	}
	
	public void generateNodes(){
		if(this.score == 0){
			for(int rowNum = 0; rowNum < this.state.board.squares.length; rowNum ++){
				for(int colNum = 0; colNum < this.state.board.squares.length; colNum ++){
					if(this.state.board.squares[rowNum][colNum] == ' '){
						Node choice = new Node(this.depth, this.state, rowNum, colNum, this.team, this.hostTree, this);
						choice.generateNodes();
						this.children.add(choice);
					}
				}
			}
		}
	}
	
	public int[] getSquareSplit(){
		int[] square = {Integer.valueOf(this.square.substring(0, 1), Integer.valueOf(this.square.substring(1, 2)))};
		return square;
	}
	
	public int getSoonestWin(){
		if(this.score > 10){
			return this.depth;
		}
		else{
			int best = 9;
			if(this.parentNode == null){
				best = 9;
			}
			else {
				best = 9;
			}
			for(int i = 0; i < this.children.size(); i ++){
				if(this.children.get(i).score > 10 && this.children.get(i).depth > best){
					int temp = 9;
					if((this.children.get(i).depth%2)+1 == 3-this.hostTree.teamNum){
						temp = this.getSoonestLoss();
					}
					else{
						temp = this.children.get(i).getSoonestWin();
					}
					if(temp < best){
						best = temp;
					}
				}
			}
			return best;
		}
	}
	
	public int getSoonestLoss(){
		if(this.score < -10){
			return this.depth;
		}
		else{
			int best = 9;
			if(this.parentNode == null){
				best = 9;
			}
			else {
				best = 9;
			}
			for(int i = 0; i < this.children.size(); i ++){
				if(this.children.get(i).score < -10 && this.children.get(i).depth < best){
					int temp = 9;
					if((this.children.get(i).depth%2)+1 == 3-this.hostTree.teamNum){
						temp = this.getSoonestLoss();
					}
					else{
						temp = this.children.get(i).getSoonestWin();
					}
					if(temp < best){
						best = temp;
					}
				}
			}
			return best;
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
			this.scoresMap.put('X', 20);
			this.scoresMap.put('O', -20);
			this.scoresMap.put('C', 10);
			this.scoresMap.put('N', 0);
		}
		else {
			this.scoresMap.put('O', 20);
			this.scoresMap.put('X', -20);
			this.scoresMap.put('C', 10);
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
		int scoreTemp = scoresMap.get(this.state.checkWin());
		if(scoreTemp < 0){
			this.score = scoreTemp + this.depth;
		}
		else if(scoreTemp == 0){
			this.score = scoreTemp;
		}
		else{
			this.score = scoreTemp - this.depth;
		}
	}
	
	public String toString(){
		return this.team + ": " + this.square + " for " + this.score + " points ----------- Total: " + this.hostTree.count;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub
		return this.score - o.score;
	}
}
