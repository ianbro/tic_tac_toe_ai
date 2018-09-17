/**
 * 
 */
package ai;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LongSummaryStatistics;

import game.Game;
import game.Player;
import com.ianmann.utils.data.structures.tree.Node;
import com.ianmann.utils.data.structures.tree.Tree;
import com.ianmann.utils.data.structures.tree.TreePath;

/**
 * @author Ian
 * @date Oct 2, 2015
 * @project tic_tac_toe_ai
 * @todo TODO
 */
public class Option extends Node<Game> implements Comparable<Option>{
	
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
	public Option(Game last_state, int rowNum, int colNum, char team, Mind hostTree, Option parentNode){
		super(parentNode, 9, hostTree, last_state);
		this.team = team;
		this.square = rowNum + "" + colNum;
		
		this.setScoresMap();
		this.placePeice();
		this.setScore();
	}
	
	public Option(Integer limit, Mind hostTree, Game currentState){
		super(9, hostTree, currentState);
		
		this.setScoresMap();
		this.setScore();
	}
	
	public void generateNodes(){
		if(this.score == 0){
			for(int rowNum = 0; rowNum < this.value.board.squares.length; rowNum ++){
				for(int colNum = 0; colNum < this.value.board.squares.length; colNum ++){
					if(this.value.board.squares[rowNum][colNum] == ' '){
						Option choice = new Option(this.value.copy(), rowNum, colNum, this.team, (Mind) this.hostTree, this);
						choice.generateNodes();
						this.addChild(choice);
					}
				}
			}
		}
	}
	
	public int indexInChildrenOf(int rowNum, int colNum){
		for(int i = 0; i < this.children.size(); i ++){
			if(((Option) this.children.get(i)).square.equals(String.valueOf(rowNum) + String.valueOf(colNum))){
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
		this.score = scoresMap.get(this.value.checkWin());
	}
	
	public int getLastIndexInChildren(){
		return this.children.size() - 1;
	}
	
	public String toString(){
		return "(" + this.team + ": " + this.square + " for " + this.score + " points)";
	}
	
	public Game getGame(){
		return this.value;
	}
	
	@Override
	public int compareTo(Option o){
		return this.score - o.score;
	}

}
