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
		tempPath.push(0);
		while(tempPath.size() <= path.size()) {
			current.children.sort(null);
			
			if(current.getDepth()%2 == this.teamNum -1){
				tempPath.push(0);
				current = (Option) current.getChild(0);
			}
			else{
				tempPath.push(current.children.size()-1);
				current = (Option) current.getChild(current.children.size()-1);
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
		System.out.println(this.anchor.childrenToString());
		System.out.println(this.anchor.getChild(0).getChild(0).getDepth());
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
		
		for(Option option : allStore){
			if(this.testPathForWin(option.pathToThis)){
				TreePath best = option.pathToThis.getDifferenceThisLonger(this.anchor.pathToThis);
				System.out.println("Path to best Choice: " + this.anchor.pathToThis);
				System.out.println("Path without anchor" + best);
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
//			System.out.println(option);
//			System.out.println(option.pathToThis);
			if(option.walkingIntoTrap()){
				option.score -= 50;
			}
//			System.out.println(option);
		}
	}
}
