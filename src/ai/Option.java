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
import iansLibrary.data.structures.tree.Node;
import iansLibrary.data.structures.tree.Tree;
import iansLibrary.data.structures.tree.TreePath;
import miniMax.Play;

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
						this.children.add(choice);
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
	
	public int getLastIndexInChildren(){
		return this.children.size() - 1;
	}
	
	public Integer getWinningIndexInChildren(){
		for(int i = 0; i < this.children.size(); i ++){
			if(((Option) this.children.get(i)).score > 10){
				return i;
			}
		}
		return null;
	}
	
	public ArrayList<Integer> getIndexWillLose(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < this.children.size(); i ++){
			Option compChoice = ((Option) this.children.get(i));
			if(compChoice.willLoseIfMovedHere()){
				list.add(i);
			}
		}
		return list;
	}
	
	public boolean walkingIntoTrap(){
		for(int i = 0; i < this.children.size(); i ++){
			Option oponentChoice = (Option) this.getChild(i);
			int count = 0;
			for(int j = 0; j < oponentChoice.children.size(); j ++){
				Option thisChoices2 = (Option) oponentChoice.getChild(j);
				ArrayList<Integer> list = new ArrayList<Integer>();
				for(int k = 0; k < thisChoices2.children.size(); k ++){
					Option oponentChoice2 = ((Option) this.children.get(k));
					if(oponentChoice2.score < -10){
						list.add(k);
					}
				}
				if(list.size() == thisChoices2.children.size()){
					count ++;
				}
			}
			if(count == oponentChoice.children.size()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Decides if this move allows oponent to win next turn
	 * @return
	 */
	public boolean willLoseIfMovedHere(){
		for(int j = 0; j < this.children.size(); j ++){
			Option oponentChoice = ((Option) this.children.get(j));
			if(oponentChoice.score < -10){
				return true;
			}
		}
		return false;
	}
	
	public Integer getTyingIndexInChildren(){
		for(int i = 0; i < this.children.size(); i ++){
			if(((Option) this.children.get(i)).score > 0 && ((Option) this.children.get(i)).score < 10){
				return i;
			}
		}
		return null;
	}
	
	public boolean canWin(){
		Integer winningMove = this.getWinningIndexInChildren();
		if(winningMove == null){
			return false;
		}
		else{
			return true;
		}
	}
	
	public boolean canTie(){
		Integer tyingMove = this.getTyingIndexInChildren();
		if(tyingMove == null){
			return false;
		}
		else{
			return true;
		}
	}
	
	public TreePath getQuickestWin(){
		
		return null;
	}
	
	/**
	 * Decides if there is a move that can be made from here that will result in a loss. similar to {@link Option.willLoseIfMovedHere} but is evaluated one full turn of this computer before
	 * @return
	 */
	public boolean canLose(){
		ArrayList<Integer> losingMove = this.getIndexWillLose();
		if(losingMove.size() == 0){
			return false;
		}
		else {
			return true;
		}
	}
	
	public ArrayList<Integer> getAvailableIndexes(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < this.children.size(); i ++){
			list.add(i);
		}
		return list;
	}
	
	public String toString(){
		return this.team + ": " + this.square + " for " + this.score + " points ----------- Total: " + this.hostTree.getSize();
	}
	
	public Game getGame(){
		return this.value;
	}
	
	@Override
	public int compareTo(Option o){
		return this.score - o.score;
	}

}
