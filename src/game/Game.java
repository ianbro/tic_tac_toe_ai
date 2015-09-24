package game;

import javax.swing.JOptionPane;


public final class Game {

	public Board board = new Board();
	public Player player1;
	public Player player2;
	public int turn = 0;
	public String lastMove;
	
	public Game(){
	}
	
	public Game copy(){
		Game game = new Game();
		game.board = board.copy();
		game.player1 = this.player1.copy();
		game.player2 = this.player2.copy();
		game.turn = this.turn;
		return game;
	}
	
	public void nextTurn(){
		
		Player p;
		if(this.turn%2 == 0){
			p = player1;
		}
		else {
			p = player2;
		}
		
		String move = p.thinkForMove();
		if(move != null){
			p.move(move, this);
			p.mind.pullNodeUp(Integer.valueOf(String.valueOf(move.charAt(0))), Integer.valueOf(String.valueOf(move.charAt(1))));
		}
		else{
			p.move(JOptionPane.showInputDialog("Square:"), this);
		}
		this.turn ++;
	}
	
	public boolean check(char team, int row, int col){
		if(this.board.squares[row][col] == team){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean checkRow(char team, int row){
		if(check(team, row, 0) && check(team, row, 1) && check(team, row, 2)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean checkColumn(char team, int column){
		if(check(team, 0, column) && check(team, 1, column) && check(team, 2, column)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean checkDiag(char team){
		if(check(team, 0, 0) && check(team, 1, 1) && check(team, 2, 2)){
			return true;
		}
		else if(check(team, 0, 2) && check(team, 1, 1) && check(team, 2, 0)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean checkTeam(char team){
		if(checkRow(team, 0)){
			return true;
		}
		else if(checkRow(team, 1)){
			return true;
		}
		else if(checkRow(team, 2)){
			return true;
		}
		else if(checkColumn(team, 0)){
			return true;
		}
		else if(checkColumn(team, 1)){
			return true;
		}
		else if(checkColumn(team, 2)){
			return true;
		}
		else if(checkDiag(team)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean checkTie(){
		for(char[] row: this.board.squares){
			for(char square: row){
				if(square == ' '){
					return false;
				}
			}
		}
		return true;
	}
	
	public char checkWin(){
		
		if(checkTeam('X')){
			return 'X';
		}
		else if (checkTeam('O')){
			return 'O';
		}
		else if (checkTie()){
			return 'C';
		}
		else{
			return 'N';
		}
	}
	
	public void setPlayer(String name, char team){
		if(team != '-'){
			team = JOptionPane.showInputDialog("Sorry, the team " + team + " is not a valid team.").charAt(0);
			team = Character.toUpperCase(team);
		}
		else{
			name = JOptionPane.showInputDialog("Player's Name:");
			team = JOptionPane.showInputDialog("Player's Team:").charAt(0);
			team = Character.toUpperCase(team);
		}
		if(team != 'X' && team != 'O'){
			setPlayer(name, team);
		}
		else{
			if(team == 'X'){
				this.player1 = new Player(team, name);
				if(name.equals("CPU")){
					player1.setAI();
				}
			}
			else{
				this.player2 = new Player(team, name);
				if(name.equals("CPU")){
					player2.setAI();
				}
			}
		}
	}
}
