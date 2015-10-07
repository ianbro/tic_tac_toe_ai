/**
 * 
 */
package ai;

import java.util.ArrayList;
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
					this.anchor.children.add(choice);
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
			
			if(current.depth%2 == this.teamNum -1){
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
		ArrayList<Integer> loses = new ArrayList<Integer>();
		ArrayList<Integer> okMoves = ((Option) this.anchor).getAvailableIndexes();
		if(((Option) this.anchor).canWin()){
			return ((Option) this.anchor).getWinningIndexInChildren();
		}
		else if(((Option) this.anchor).canTie()){
			return ((Option) this.anchor).getTyingIndexInChildren();
		}
		else if(((Option) this.anchor).canLose()){
			loses = ((Option) this.anchor).getIndexWillLose();
			okMoves.removeAll(loses);
		}
		
		ArrayList<Option> allStore = this.getAllStorageAsList();
		allStore.sort(null);
		
		for(Option option : allStore){
			if(this.testPathForWin(option.pathToThis)){
				return option.pathToThis.getDifferenceThisLonger(this.anchor.pathToThis).peek();
			}
		}
		
		if(okMoves.size() > 0){
			return okMoves.get(0);
		}
		else{
			return loses.get(0);
		}
	}
	
	public void findTraps(){
		for(Option option : this.getAllStorageAsList()){
			if(option.walkingIntoTrap()){
				option.score -= 50;
			}
		}
	}
}
