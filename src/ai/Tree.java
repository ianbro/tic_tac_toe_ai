package ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import game.Board;
import game.Game;
import game.Player;
import miniMax.Play;

public class Tree {
	
	private char team;
	public Game currentState;
	public ArrayList<Node> choices = new ArrayList<Node>();
	public int count = 0;
	public int maxDepth = 0;
	public int depth;
	public int teamNum;
	
	public Tree(char team){
		this.team = team;
		this.currentState = Play.game.copy();
		this.depth = 0;
	}
	
	public void generateTree(Game newState){
		this.currentState = newState;
		for(int rowNum = 0; rowNum < this.currentState.board.squares.length; rowNum ++){
			for(int colNum = 0; colNum < this.currentState.board.squares.length; colNum ++){
				if(this.currentState.board.squares[rowNum][colNum] == ' '){
					Node choice = new Node(this.depth, this.currentState, rowNum, colNum, this.team, this, null);
					choice.generateNodes();
					this.choices.add(choice);
				}
			}
		}
	}
	
	public void updateLastMove(String place){
		int rowNum = Integer.valueOf(String.valueOf(place.charAt(0)));
		int colNum = Integer.valueOf(String.valueOf(place.charAt(1)));
		this.pullNodeUp(rowNum, colNum);
	}
	
	public int indexInChoicesOf(int rowNum, int colNum){
		//finds the child that made the move of the given row and column
		for(int i = 0; i < this.choices.size(); i++){
			Node child = this.choices.get(i);
			if(child.state.board.squares[rowNum][colNum] != ' '){
				for(int j = 0; j < this.choices.size(); j ++){
					if(this.choices.get(j).state.board.squares[rowNum][colNum] == child.state.board.squares[rowNum][colNum] && j != i){
						return -1;
					}
				}
				return i;
			}
		}
		return -2;
	}
	
	public String childrenToString(){
		String str = "";
		str = str + "================================================================\n";
		for(Node child : this.choices){
//			str = str + "" + Arrays.deepToString(child.state.board.squares) + "\n";
			str = str + child.toString() + "\n";
		}
		str = str + "================================================================\n";
		str = str + "================================================================";
		return str;
	}
	
	public void orderChildrenByDepth(){
		
		if(this.choices.size() == 0){
			return;
		}
		else{
			this.choices.sort(null);
			for(int i = 0; i < this.choices.size(); i ++){
				Node child = this.choices.get(i);
				int soonestWin = child.getSoonestWin();
				int bestSoonestWin = this.choices.get(0).getSoonestWin();
				if(soonestWin < bestSoonestWin){
					Node temp = this.choices.get(0);
					this.choices.set(0, child);
					this.choices.set(i, temp);
				}
				int soonestLoss = child.getSoonestLoss();
				int worstSoonestLoss = this.choices.get(this.choices.size()-1).getSoonestLoss();
				if(soonestLoss < worstSoonestLoss){
					Node temp = this.choices.get(this.choices.size()-1);
					this.choices.set(this.choices.size()-1, child);
					this.choices.set(i, temp);
				}
			}
		}
	}
	
	public void pullNodeUp(int rowNum, int colNum){
		int index = this.indexInChoicesOf(rowNum, colNum);
		Node currentOption = this.choices.get(index);
		this.choices = currentOption.children;
		this.currentState = currentOption.state.copy();
	}
	
	public int[] getSquareSplit(){
		int[] square = {Integer.valueOf(Play.game.lastMove.substring(0, 1), Integer.valueOf(Play.game.lastMove.substring(1, 2)))};
		return square;
	}
	
	public int indexInChildrenOf(int rowNum, int colNum){
		//finds the child that made the move of the given row and column
		for(int i = 0; i < this.choices.size(); i++){
			Node child = this.choices.get(i);
			if(child.state.board.squares[rowNum][colNum] != ' '){
				for(int j = 0; j < this.choices.size(); j ++){
					if(this.choices.get(j).state.board.squares[rowNum][colNum] == child.state.board.squares[rowNum][colNum] && j != i){
						return -1;
					}
				}
				return i;
			}
		}
		return -2;
	}
}