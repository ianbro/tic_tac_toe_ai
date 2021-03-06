package game;


import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import com.ianmann.utils.data.structures.tree.Tree;
import com.ianmann.utils.data.structures.tree.TreePath;

public abstract class Play {

	public static Game game;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		game = new Game();
		game.setPlayer(null, '-');
		game.setPlayer(null, '-');
		if (game.player1.name.equals("CPU")) {
			game.player1.setAI();
			System.out.println("Generating mind for player 1...");
			game.player1.mind.generateTree();
		}
		if (game.player2.name.equals("CPU")) {
			game.player2.setAI();
			System.out.println("Generating mind for player 2...");
			game.player2.mind.generateTree();
		}
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
