/**
 * 
 */
package ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import game.Game;
import iansLibrary.data.structures.tree.Node;
import iansLibrary.data.structures.tree.Tree;
import iansLibrary.data.structures.tree.TreePath;
import iansLibrary.data.structures.tree.exceptions.MultiIndexesForValueException;
import miniMax.Play;

/**
 * @author Ian
 * @date Oct 2, 2015
 * @project tic_tac_toe_ai
 * @todo TODO
 */
public class Mind extends Tree<Game> {

	private char team;
	public int teamNum;
	
	public Mind(char team){
		super();
		super.setAnchor(new Option(9, this, Play.game.copy()));
	}
	
	public void generateTree(){
		for(int rowNum = 0; rowNum < this.anchor.value.board.squares.length; rowNum ++){
			for(int colNum = 0; colNum < this.anchor.value.board.squares.length; colNum ++){
				if(this.anchor.value.board.squares[rowNum][colNum] == ' '){
					Option choice = new Option(this.anchor.value.copy(), rowNum, colNum, this.team, this, (Option) this.anchor);
					choice.generateNodes();
					this.anchor.addChild(choice);
				}
			}
		}
		this.findTraps();
	}
	
	public ArrayList<Option> availableOutcomesFromHere(){
		ArrayList<Option> outcomes = new ArrayList<Option>();
		
		Option current = (Option) this.anchor;
		
		for(Node<Game> outcome_temp : this.getAllStorageAsList()){
			Option outcome = (Option) outcome_temp;
			if(outcome.pathToThis.size() > current.pathToThis.size() && outcome.pathToThis.getDifferenceThisLonger(current.pathToThis) != null){
				outcomes.add(outcome);
			}
		}
		
		return outcomes;
	}
	
	public void updateLastMove(String place){
		int rowNum = Integer.valueOf(String.valueOf(place.charAt(0)));
		int colNum = Integer.valueOf(String.valueOf(place.charAt(1)));
		this.pullUpChild(((Option) this.anchor).indexInChildrenOf(rowNum, colNum));
	}
	
	public ArrayList<Option> getAllStorageAsList(){
		ArrayList<Option> allStore = new ArrayList<Option>();
		Collection<ArrayList<Node<Game>>> depths = this.allStorage.values();
		for(ArrayList<Node<Game>> group : depths){
			for(Node<Game> option : group){
				Option opt = ((Option) option);
				allStore.add(opt);
			}
		}
		return allStore;
	}
	
	public boolean testPathForWin(TreePath path){
		TreePath tempPath = new TreePath();
		Option current = (Option) this.anchor;
		try {
			tempPath.push(current.parent.indexInChildrenOf(current));
		} catch (MultiIndexesForValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){
			tempPath.push(-1);
		}
		while(tempPath.size() < path.size()) {
			ArrayList<Node<Game>> tempChildren = (ArrayList<Node<Game>>) current.children.clone();
			tempChildren.sort(null);
			
			if(current.getDepth()%2 == this.teamNum -1 && current.children.size() > 0){
				tempPath.push(0);
				current = (Option) tempChildren.get(0);
			}
			else if (current.children.size() > 0){
				tempPath.push(current.children.size()-1);
				current = (Option) tempChildren.get(current.children.size()-1);
			}
		}
		//now see if the two paths match
		if(path.equals(tempPath)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public int getBestChoice(){
		System.out.println("Thinking ========================================================================================= Thinking");
		ArrayList<Integer> loses = new ArrayList<Integer>();
		ArrayList<Integer> okMoves = ((Option) this.anchor).getAvailableIndexes();
		
		System.out.println("Can I win?   " + ((Option) this.anchor).canWin());
		System.out.println("Can I tie?   " + ((Option) this.anchor).canTie());
		System.out.println("Can I lose?  " + ((Option) this.anchor).canLose());
		
		if(((Option) this.anchor).canWin()){
			int index = ((Option) this.anchor).getWinningIndexInChildren();
			System.out.println("Winning Index:  " + index);
			System.out.println("Done ================================================================================================= Done");
			return index;
		}
		else if(((Option) this.anchor).canTie()){
			int index = ((Option) this.anchor).getTyingIndexInChildren();
			System.out.println("Tying Index:    " + index);
			System.out.println("Done ================================================================================================= Done");
			return index;
		}
		else if(((Option) this.anchor).canLose()){
			loses = ((Option) this.anchor).getIndexWillLose();
			System.out.println("Losing Indexes:\n" + Arrays.toString(loses.toArray()));
			System.out.println("OK moves before removing losses:\n" + Arrays.toString(okMoves.toArray()));
			okMoves.removeAll(loses);
			System.out.println("OK moves after removing losses:\n" + Arrays.toString(okMoves.toArray()));
		}
		
		ArrayList<Option> allStore = this.getAllStorageAsList();
		allStore.sort(null);
		
		for(Option option : availableOutcomesFromHere()){
			if(this.testPathForWin(option.pathToThis)){
				TreePath best = option.pathToThis.getDifferenceThisLonger(this.anchor.pathToThis);
				System.out.println("Done ================================================================================================= Done");
				return best.peek();
			}
		}
		
		if(okMoves.size() > 0){
			int index = okMoves.get(0);
			System.out.println("Move to go with after all evaluation: " + index);
			System.out.println("Done ================================================================================================= Done");
			return okMoves.get(0);
		}
		else{
			int index = loses.get(0);
			System.out.println("Loss to go with after all evaluation: " + index);
			System.out.println("Done ================================================================================================= Done");
			return index;
		}
	}
	
	public void findTraps(){
		for(Option option : this.getAllStorageAsList()){
			if(option.getDepth() < 2){
				System.out.println(option.walkingIntoTrap());
			}
			if(option.walkingIntoTrap()){
				option.score -= 50;
			}
		}
	}
}
