package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ai.Option;
import ai.Mind;

public class Player {

	public char team;
	public String name;
	public Mind mind;
	public boolean isAI = false;
	
	public Player(char team, String name){
		team = Character.toUpperCase(team);
		this.team = team;
		this.name = name;
	}
	
	public void setAI(){
		if(this.name.equalsIgnoreCase("CPU")){
			this.mind = new Mind(this.team);
			this.isAI = true;
		}
	}
	
	public Player(){
	}
	
	public Player copy(){
		Player player = new Player();
		player.team = this.team;
		player.name = this.name.toString();
		return player;
	}
	
	public void move(String place, Game game){
		
		if(place.length() != 2){
			System.out.println("Please enter square in format '00'. Note: it is 0 indexed.");
			game.turn -= 1;
		}
		else{
			try{
				int rowNum = Integer.valueOf(String.valueOf(place.charAt(0)));
				int columnNum = Integer.valueOf(String.valueOf(place.charAt(1)));
				char square = game.board.squares[rowNum][columnNum];
				if(square != ' '){
					System.out.println("That square is already taken.");
					game.turn -= 1;
				}
				else{
					game.board.squares[rowNum][columnNum] = this.team;
					game.lastMove = place;
				}
			} catch (IndexOutOfBoundsException e){
				e.printStackTrace();
				System.out.println("Please enter square in format '00'. Note: it is 0 indexed.\nThe first digit must range from 0 to 2 inclusive and so must the second digit");
				game.turn -= 1;
			}
		}
	}
	
	public String thinkForMove(){
		if(this.mind == null){
			return null;
		}
		else{
			this.mind.updateLastMove(Play.game.lastMove);
		    int choiceIndex = this.mind.getBestChoice();
			return ((Option) this.mind.anchor.children.get(choiceIndex)).square;
		}
	}
	
	public String toString(){
		return this.name + ", Team: " + this.team;
	}
}
