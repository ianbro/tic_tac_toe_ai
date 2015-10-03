package ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import game.Board;
import game.Game;
import game.Player;
import miniMax.Play;

public class TreeTemp {
	
	private char team;
	public Game currentState;
	public ArrayList<NodeTemp> choices = new ArrayList<NodeTemp>();
	public int count = 0;
	public int maxDepth = 0;
	public int depth;
	public int teamNum;
	
	public TreeTemp(char team){
		this.team = team;
		this.currentState = Play.game.copy();
		this.depth = 0;
	}
	
	public void generateTree(Game newState){
		this.currentState = newState;
		for(int rowNum = 0; rowNum < this.currentState.board.squares.length; rowNum ++){
			for(int colNum = 0; colNum < this.currentState.board.squares.length; colNum ++){
				if(this.currentState.board.squares[rowNum][colNum] == ' '){
					NodeTemp choice = new NodeTemp(this.depth, this.currentState, rowNum, colNum, this.team, this, null);
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
			NodeTemp child = this.choices.get(i);
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
		for(NodeTemp child : this.choices){
//			str = str + "" + Arrays.deepToString(child.state.board.squares) + "\n";
			str = str + child.toString() + "\n";
		}
		str = str + "================================================================\n";
		str = str + "================================================================";
		return str;
	}
	
	public boolean checkIfWillLose(int i){
		for(int j = 0; j < this.choices.get(i).children.size(); j ++){
			NodeTemp oponentMove = this.choices.get(i).children.get(j);
			if(oponentMove.score < 0){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkIfWillWin(int i){
		if(this.choices.get(i).score > 10){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean checkIfTrick(int i){
		for(int j = 0; j < this.choices.get(i).children.size(); j ++){
			NodeTemp oponent1 = this.choices.get(i).children.get(j);
			int losses = 0;
			for(int k = 0; k < oponent1.children.size(); k ++){
				NodeTemp move2 = oponent1.children.get(k);
				if(checkIfWillLose(k)){
					losses ++;
				}
			}
			if(losses == oponent1.children.size()){
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<NodeTemp> tempBestChoices = new ArrayList<NodeTemp>();
	public ArrayList<NodeTemp> tempLosses = new ArrayList<NodeTemp>();
	public int tempBestChoice;
	public int tempBestLoss;
	public int getBestChoice(){
		
		this.choices.sort(null);
		int best = -5000;
		int bestChoice = -30;
		for(int i = 0; i < this.choices.size(); i ++){
			NodeTemp choice = this.choices.get(i);
			int temp = choice.getBest();
			if(temp > best){
				best = temp;
				bestChoice = i;
			}
		}
		
		if(!this.checkIfWillWin(bestChoice) && !this.choices.get(bestChoice).square.equals("11") && this.choices.get(bestChoice).square.contains("1")){
			this.tempBestChoices.add(this.choices.get(bestChoice));
			this.choices.remove(bestChoice);
			try{
				bestChoice = this.getBestChoice();
			}
			catch(ArrayIndexOutOfBoundsException e){
				bestChoice = this.tempBestChoice;
			}
			for(NodeTemp choice : tempBestChoices){
				this.choices.add(choice);
			}
			tempBestChoices = new ArrayList<NodeTemp>();
		}
		
		if(!this.checkIfWillWin(bestChoice) && checkIfWillLose(bestChoice)){
			this.tempLosses.add(this.choices.get(bestChoice));
			this.choices.remove(bestChoice);
			try{
				bestChoice = this.getBestChoice();
			}
			catch(ArrayIndexOutOfBoundsException e){
				bestChoice = this.tempBestLoss;
			}
			for(NodeTemp choice : tempLosses){
				this.choices.add(choice);
			}
			this.tempLosses = new ArrayList<NodeTemp>();
		}
		
		if(!this.checkIfWillWin(bestChoice) && checkIfTrick(bestChoice)){
			this.tempLosses.add(this.choices.get(bestChoice));
			this.choices.remove(bestChoice);
			try{
				bestChoice = this.getBestChoice();
			}
			catch(ArrayIndexOutOfBoundsException e){
				bestChoice = this.tempBestLoss;
			}
			for(NodeTemp choice : tempLosses){
				this.choices.add(choice);
			}
			this.tempLosses = new ArrayList<NodeTemp>();
		}
		
		return bestChoice;
	}
	
	public void pullNodeUp(int rowNum, int colNum){
		int index = this.indexInChoicesOf(rowNum, colNum);
		NodeTemp currentOption = this.choices.get(index);
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
			NodeTemp child = this.choices.get(i);
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