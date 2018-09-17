/**
 * 
 */
package ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import game.Game;
import game.Play;

import com.ianmann.utils.data.structures.tree.Node;
import com.ianmann.utils.data.structures.tree.Tree;
import com.ianmann.utils.data.structures.tree.TreePath;
import com.ianmann.utils.data.structures.tree.exceptions.MultiIndexesForValueException;

/**
 * @author Ian
 * @date Oct 2, 2015
 * @project tic_tac_toe_ai
 * @todo TODO
 */
public class Mind extends Tree<Game> {

	public char team;
	
	public Mind(char team){
		super();
		this.team = team;
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
	}
	
	public void updateLastMove(String place){
		int rowNum = Integer.valueOf(String.valueOf(place.charAt(0)));
		int colNum = Integer.valueOf(String.valueOf(place.charAt(1)));
		this.pullUpChild(((Option) this.anchor).indexInChildrenOf(rowNum, colNum));
	}
	
	public int getBestChoice(){
		int bestChoice = 0;
		int bestScore = -20;
		for (int i = 0; i < this.anchor.children.size(); i ++) {
			int optionScore = this.getScoreOfOption((Option) this.anchor.children.get(i));
			System.out.println("Current Turn: " + this.anchor.value.turn);
			if (optionScore > bestScore) {
				bestChoice = i;
				bestScore = optionScore;
			}
		}
		return bestChoice;
	}
	
	public int getScoreOfOption(Option option) {
		int adjustedScore = 0;
		if (option.children.isEmpty()) {
			adjustedScore = option.score;
		}
		else {
			adjustedScore = 0;
			for (int i = 0; i < option.children.size(); i ++) {
				Option node = (Option) option.children.get(i);
				int rawScore = this.getScoreOfOption(node);
				if (rawScore < 0) rawScore ++;
				else if (rawScore > 0) rawScore --;
				if (node.getGame().turn == 3) {
					System.out.println(node.getGame().board);
					System.out.println("Raw Score for node: " + rawScore);
					System.out.println(option.getGame().getCurrentPlayerToMove());
					System.out.println("Team:" + this.team);
				}
				if (option.getGame().getCurrentPlayerToMove().team == this.team) {
					if (rawScore > adjustedScore)
						adjustedScore = rawScore;
				} else {
					if (rawScore < adjustedScore)
						adjustedScore = rawScore;
				}
			}
		}
		return adjustedScore;
	}
}
