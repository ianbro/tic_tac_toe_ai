package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ai.Node;
import ai.Tree;
import miniMax.Play;

public class Player {

	public char team;
	public String name;
	public Tree mind;
	public boolean isAI = false;
	
	public Player(char team, String name){
		team = Character.toUpperCase(team);
		this.team = team;
		this.name = name;
	}
	
	public void setAI(){
		if(this.name.equalsIgnoreCase("CPU")){
			this.mind = new Tree(this.team);
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
			System.out.println("Please enter square in format 'a0'. Note: it is 0 indexed.");
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
					Play.game.lastMove = place;
				}
			} catch (IndexOutOfBoundsException e){
				e.printStackTrace();
				System.out.println("Please enter square in format 'a0'. Note: it is 0 indexed.\nThe first digit must range from 0 to 2 inclusive and so must the second digit");
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
			HashMap<Integer, Integer> scoresTotal = new HashMap<Integer, Integer>();
			ArrayList<Integer> losses = new ArrayList<Integer>();
			for(int i = 0; i < this.mind.choices.size(); i ++){
				Node choice = this.mind.choices.get(i);
				if(choice.score == 10){
					return choice.square.toString();
				}
			}
			for(int i = 0; i < this.mind.choices.size(); i ++){
				Node choice = this.mind.choices.get(i);
				boolean loss = false;
				for(int j = 0; j < choice.children.size(); j ++){
					if(choice.children.get(j).score == -10){
						losses.add(i);
						loss = true;
					}
				}
				if(!loss){
					scoresTotal.put(i, choice.totalBranchFromHere());
					System.out.println(choice.totalBranchFromHere());
				}
			}
			for(Integer loss: losses){
				this.mind.choices.remove(loss);
			}
			
			Random rand = new Random();
		    int randomNum = rand.nextInt((scoresTotal.keySet().size()) + 1);
		    
			int max = -1000000000;
		    int choiceIndex = randomNum;
		    
		    System.out.println(scoresTotal.keySet().toString());
			for(int index : scoresTotal.keySet()){
				int score = scoresTotal.get(index);
				if(score > max){
					max = score;
					choiceIndex = index;
				}
				System.out.println(index);
			}
			System.out.println(randomNum + "/" + choiceIndex + ":" + max);
			return this.mind.choices.get(choiceIndex).square;
		}
	}
	
	public String toString(){
		return this.name + ", Team: " + this.team;
	}
}
