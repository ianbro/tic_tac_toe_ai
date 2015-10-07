package miniMax;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import game.Game;
import iansLibrary.data.structures.tree.TreePath;

public abstract class Play {

	public static Game game;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		game = new Game();
		game.setPlayer(null, '-');
		game.setPlayer(null, '-');
		System.out.println(game.player1);
		System.out.println(game.player2);
		game.player2.mind.generateTree();
		System.out.println("Start!");

		while(true){
			char win = game.checkWin();
			if(win == 'N'){
				game.nextTurn();
				System.out.println(game.board);
				continue;
			}
			else if(win == 'X'){
				System.out.println(game.player1.name + " Wins!");
			}
			else if(win == 'O'){
				System.out.println(game.player2.name + " Wins!");
			}
			else if(win == 'C'){
				System.out.println("Cat's Game!");
			}
			System.out.println(game.board);
			break;
		}
	}

}
