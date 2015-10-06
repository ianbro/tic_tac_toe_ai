/**
 * 
 */
package ai;

import java.util.ArrayList;
import java.util.HashMap;

import game.Game;
import game.Player;
import iansLibrary.data.structures.tree.Node;
import iansLibrary.data.structures.tree.Tree;

/**
 * @author Ian
 * @date Oct 2, 2015
 * @project tic_tac_toe_ai
 * @todo TODO
 */
public class Option extends Node<Game>{
	
	private char team;
	public String square;
	public int score;
	public int turn;

	private HashMap<Character, Integer> scoresMap = new HashMap<Character, Integer>();

	/**
	 * @param parent
	 * @param limit
	 * @param hostTree
	 * @param value
	 */
	public Option(Game last_state, int rowNum, int colNum, char team, Tree<Game> hostTree, Option parentNode){
		super(parentNode, 9, hostTree, last_state);
		this.team = team;
		this.square = rowNum + "" + colNum;
		
		this.setScoresMap();
		this.placePeice();
		this.setScore();
	}
	
	public void generateNodes(){
		if(this.score == 0){
			for(int rowNum = 0; rowNum < this.value.board.squares.length; rowNum ++){
				for(int colNum = 0; colNum < this.value.board.squares.length; colNum ++){
					if(this.value.board.squares[rowNum][colNum] == ' '){
						Option choice = new Option(this.value, rowNum, colNum, this.team, this.hostTree, this);
						choice.generateNodes();
						this.children.add(choice);
					}
				}
			}
		}
	}
	
	public int indexInChildrenOf(int rowNum, int colNum){
		for(int i = 0; i < this.children.size(); i ++){
			if(((Option) this.children.get(i)).square.equals(String.join(String.valueOf(rowNum), String.valueOf(colNum)))){
				return i;
			}
		}
		return -1;
	}
	
	public int[] getSquareSplit(){
		int[] square = {Integer.valueOf(this.square.substring(0, 1), Integer.valueOf(this.square.substring(1, 2)))};
		return square;
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
		if(this.value.turn%2 == 0){
			playerToMove = this.value.player1;
		}
		else {
			playerToMove = this.value.player2;
		}
		playerToMove.move(square, this.value);
		this.value.turn ++;
	}
	
	public void setScore(){
		int scoreTemp = scoresMap.get(this.value.checkWin());
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
	
	public int totalBranchFromHere(){
		int total = this.score;
		for(Node<Game> child : this.children){
			total += ((Option) child).score;
			if(child.children.size() > 0){
				total += ((Option) child).totalBranchFromHere();
			}
		}
		return total;
	}
	
	public String toString(){
		return this.team + ": " + this.square + " for " + this.score + " points ----------- Total: " + this.hostTree.getSize();
	}
	
	public Game getGame(){
		return this.value;
	}

}
