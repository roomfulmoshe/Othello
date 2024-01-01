import java.util.LinkedList;

import javax.swing.JOptionPane;

public class Othello2 {
	static char[][] board = new char[8][8];
	static int wins = 0;
	static int losses = 0;
	static int ties = 0;
	static float percentage;
	static boolean playAgain = false;
	static int usersScore = 2;
	static int computersScore = 2;
	static int gamesPlayed;
	static int scoreAfterMove = 0;
	static LinkedList<Character> copyOfBoard = new LinkedList<Character>();
	
	public static void main(String[] args) {
		createBoard();
		printBoard();
		int row = 0;
		int col = 0;
		String rowString, colString;
		while(!GameOver(1) || playAgain) {
			//get the players next move
			try {
				rowString = JOptionPane.showInputDialog("Enter the row number of your next move: (0-7)");
				colString = JOptionPane.showInputDialog("Enter the coloumn number of your next move: (0-7)");
				row = Integer.parseInt(rowString);
				col = Integer.parseInt(colString);
			}
			catch(NumberFormatException wrongInput) {
				System.out.println("Invalid input. Program crashed. Please try again and enter number between 0 - 7");
				System.exit(0);
			}
			
			//keep asking user for next move until legal move is entered
			while(!legalMove(row,col, false, 1)) {
				try {
					rowString = JOptionPane.showInputDialog("Illegal Move. Try again! Enter the row number of your next move: (0-7)");
					colString = JOptionPane.showInputDialog("Enter the coloumn number of your next move: (0-7)");
					row = Integer.parseInt(rowString);
					col = Integer.parseInt(colString);
				}
				catch(NumberFormatException wrongInput) {
					System.out.println("Invalid input. Program crashed. Please try again and enter number between 0 - 7");
					System.exit(0);
				}
			}
			//perform users move
			legalMove(row, col, true, 1);
			System.out.println("users move: ");
			printBoard();
			
			if( GameOver(0) == true && playAgain == false) {
				System.exit(0);  
			}
			else if(GameOver(0) == true && playAgain == true)
				continue;
			//find the next best move and do it
			bestNextMove();
			printBoard();
		}
	}

	static void createBoard() {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				board[i][j] = '*';
			}
		}
		board[3][3] = '0';
		board[3][4] = '1';
		board[4][3] = '1';
		board[4][4] = '0';
	}
	
	static void printBoard() {
		countScore();
		System.out.println("ScoreBoard-  \n User: " + Integer.toString(usersScore) + " \n Computer: " + Integer.toString(computersScore) + "\n" );
		System.out.print("      ");
		for(int i = 0; i < 8; i ++)
			System.out.print(Integer.toString(i) + "   ");
		System.out.println("\n");
		for(int i = 0; i < 8; i++) {
			System.out.print(Integer.toString(i)  + "     ");
			for(int j = 0; j < 8; j++) {
				System.out.print(board[i][j] + "   ");
			}
			System.out.println("\n");
		}
		System.out.println("  -    -    -    -    -    -    -    -  \n");
		
	}
	
	private static void countScore() {
		usersScore = 0;
		computersScore = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j] == '1')
					usersScore++;
				else if(board[i][j] == '0')
					computersScore++;
			}
		}
		
	}

	static boolean GameOver(int player) {
		playAgain = false;
		int playersPeice = 0;
		if(player == 1) {
			playersPeice = 1;
		}
		//check for legal move
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(legalMove(i,j, false, playersPeice) == true) {
					return false;
				}
			}
		}
		//if there is no legal move do following code
		gamesPlayed++;
		countScore();
		String message = "Tie game!";
		if(usersScore > computersScore) {
			wins++;
			message = "Congrats you won! ";
		}
		else if(usersScore < computersScore){
			losses++;
			message = "Sorry you lost :(  ! ";
		}
		else
			ties++;
		
		percentage = ( (float) wins)/( (float) gamesPlayed);
		String answer;
		while(true) {
			answer = JOptionPane.showInputDialog(message + "Do you want to play again: (type 'y' for yes or type 'n' for no) ");
			if(answer.equals("y") || answer.equals("n")) break;
		}
		if(answer.equals("y")) {
			playAgain = true;
			createBoard();
			printBoard();
		}
		else if(answer.equals("n")) {
			JOptionPane.showMessageDialog(null, "Good game! Here are the results: \n won " + Integer.toString(wins) + " many times, \n lost " + Integer.toString(losses) + " many times, \n and tied  " + Integer.toString(ties) +   " many times. \n You won " + Float.toString(percentage*100)+ "% of the time. \n Thanks for playing come back soon :) "  );
			System.exit(0);
		}
		return true;
	}
	
	
	
	
	
	
	static boolean legalMove(int row, int col, boolean update, int player) {
		char playersPeice = '0';
		char opponentsPeice = '1';
		if(player == 1) {
			playersPeice = '1';
			opponentsPeice = '0';
		}
		scoreAfterMove = 0;
		
		//check if row and column is occupied
		try {
			if(board[row][col] == '1' || board[row][col] == '0') 
				return false;
		}
		catch(ArrayIndexOutOfBoundsException ignore) {
			return false;
		}
		boolean legal = false;
		//check upper right diagonal
		try {
			if(board[row - 1][col + 1] == opponentsPeice) {
				for(int i = 2; i + col < 8 && row - i >= 0; i++) {
					if(board[row - i][col + i] == '*' )
						break;
					if(board[row - i][col + i] == playersPeice) {
						legal = true;
						scoreAfterMove++;
						//update the board
						if(update == true) {
							for(int j = 0; j + col < 8 && row - j >= 0; j++) {
								if(board[row - j][col + j] == playersPeice) break;
								else {
									board[row - j][col + j] = playersPeice;
								}
							}
						}
						
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException ignore) {
			
		}
		//check upper left diagonal
		try {
			if(board[row - 1][col - 1] == opponentsPeice) {
				for(int i = 2; col - i >= 0 && row - i >= 0; i++) {
					if(board[row - i][col - i] == '*' )
						break;
					if(board[row - i][col - i] == playersPeice) {
						//update board
						if(update == true) {
							for(int j = 0; col - j >= 0 && row - j >= 0; j++) {
								if(legal == true && j == 0) j = 1;
								if(board[row - j][col - j] == playersPeice) break;
								else {
									board[row - j][col - j] = playersPeice;
								}
							}
						}
						legal = true;
						scoreAfterMove++;
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException ignore) {
			
		}
		//check lower left diagonal
		try {
			if(board[row + 1][col - 1] == opponentsPeice) {
				for(int i = 2; col - i >= 0 && row + i <= 7; i++) {
					if(board[row + i][col - i] == '*' )
						break;
					if(board[row + i][col - i] == playersPeice) {
						//update board
						if(update == true) {
							for(int j = 0; col - j >= 0 && row + j <= 7; j++) {
								if(legal == true && j == 0) j = 1;
								if(board[row + j][col - j] == playersPeice) break;
								else {
									board[row + j][col - j] = playersPeice;
								}
							}
						}
						legal = true;
						scoreAfterMove++;
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException ignore) {
			
		}
		//check lower right diagonal
		try {
			if(board[row + 1][col + 1] == opponentsPeice) {
				for(int i = 2; col + i <= 7 && row + i <= 7; i++) {
					if(board[row + i][col + i] == '*' )
						break;
					if(board[row + i][col + i] == playersPeice) {
						//update the board
						if(update == true) {
							for(int j = 0; col + j <= 7 && row + j <= 7; j++) {
								if(legal == true && j == 0) j = 1;
								if(board[row + j][col + j] == playersPeice) break;
								else {
									board[row + j][col + j] = playersPeice;
								}
									
							}
						}
						legal = true;
						scoreAfterMove++;
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException ignore) {
			
		}
		//check row up
		try {
			if(board[row - 1][col] == opponentsPeice) {
				for(int i = 2; row - i >= 0; i++) {
					if(board[row - i][col] == '*' )
						break;
					if(board[row - i][col] == playersPeice) {
						if(update == true) {
							for(int j = 0; row - j >= 0; j++) {
								if(legal == true && j == 0) j = 1;
								if(board[row - j][col] == playersPeice) break;
								else {
									board[row - j][col] = playersPeice;
								}
							}
						}
						legal = true;
						scoreAfterMove++;
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException ignore) {
			
		}
		//check row down
		try {
			if(board[row + 1][col] == opponentsPeice) {
				for(int i = 2; row + i <= 7; i++) {
					if(board[row + i][col] == '*' )
						break;
					if(board[row + i][col] == playersPeice) {
						//update the board
						if(update == true) {
							for(int j = 0; row + j <= 7; j++) {
								if(legal == true && j == 0) j = 1;
								if(board[row + j][col] == playersPeice) break;
								else {
									board[row + j][col] = playersPeice;
								}
							}
						}
						legal = true;
						scoreAfterMove++;
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException ignore) {
			
		}
		//check columns to left
		try {
			if(board[row][col - 1] == opponentsPeice) {
				for(int i = 2; col - i >= 0; i++) {
					if(board[row][col - i] == '*' )
						break;
					if(board[row][col - i] == playersPeice) {
						//update the board
						if(update == true) {
							for(int j = 0; col - j >= 0; j++) {
								if(legal == true && j == 0) j = 1;
								if(board[row][col - j] == playersPeice) break;
								else {
									board[row][col - j] = playersPeice;
								}
									
							}
						}
						legal = true;
						scoreAfterMove++;
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException ignore) {
			
		}
		//check columns to right
		try {
			if(board[row][col + 1] == opponentsPeice) {
				for(int i = 2; col + i <= 7; i++) {
					if(board[row][col + i] == '*' )
						break;
					if(board[row][col + i] == playersPeice) {
						if(update == true) {
							for(int j = 0; col + j <= 7; j++) {
								if(legal == true && j == 0) j = 1;
								if(board[row][col + j] == playersPeice) break;
								else {
									board[row][col + j] = playersPeice;
								}
							}
						}
						legal = true;
						scoreAfterMove++;
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException ignore) {
			
		} 
		return legal;	
	}
	
	private static void bestNextMove() {
		int[] arr = {-2,-2};
		//check if corners (the most valuable squares) are available if yes then do that move 
		if( legalMove(0,0, false, 0) == true) {
			legalMove(0,0, true, 0);
			return;
		}
		if( legalMove(7,0, false, 0) == true) {
			legalMove(7,0, true, 0);
			return;
		}
		if( legalMove(7,7, false, 0) == true) {
			legalMove(7,7, true, 0);
			return;
		}
		if( legalMove(0,7, false, 0) == true) {
			legalMove(0,7, true, 0);
			return;
		}
		//check to see which move results in highest score
		int highScore = -1;
		copyBoardToLinkList();
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				legalMove(i,j, true, 0);
				countScore();
				if(computersScore > highScore) {
					arr[0] = i;
					arr[1] = j;
					highScore = computersScore;
				}
				linkedListToBoard();
			}
		}
		linkedListToBoard();
		System.out.println("Computer's move - row: " + Integer.toString(arr[0]) + " col:  " + Integer.toString(arr[1]) );
		legalMove(arr[0], arr[1], true, 0);
	}
	
	private static void copyBoardToLinkList() {
		copyOfBoard.clear();
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				copyOfBoard.add(board[i][j]);
			}
		}
	}
	
	private static void linkedListToBoard(){
		int counter = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				board[i][j] = copyOfBoard.get(counter);
				counter++;
			}
		}
		
	}	
}
