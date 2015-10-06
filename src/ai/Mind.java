/**
 * 
 */
package ai;

import game.Game;
import iansLibrary.data.structures.tree.Node;
import iansLibrary.data.structures.tree.Tree;
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
		super.setAnchor(new Node<Game>(9, this, Play.game.copy()));
	}
	
	public void generateTree(){
		for(int rowNum = 0; rowNum < this.anchor.value.board.squares.length; rowNum ++){
			for(int colNum = 0; colNum < this.anchor.value.board.squares.length; colNum ++){
				if(this.anchor.value.board.squares[rowNum][colNum] == ' '){
					Option choice = new Option(this.anchor.value, rowNum, colNum, this.team, this, (Option) this.anchor);
					choice.generateNodes();
					this.anchor.children.add(choice);
				}
			}
		}
	}
	
	public void updateLastMove(String place){
		int rowNum = Integer.valueOf(String.valueOf(place.charAt(0)));
		int colNum = Integer.valueOf(String.valueOf(place.charAt(1)));
		this.pullUpChild(((Option) this.anchor).indexInChildrenOf(rowNum, colNum));
	}
	
	public int getBestChoice(){
		return 0;
	}
}
