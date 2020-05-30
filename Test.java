package minesweeper3d;

import java.util.Scanner;

import minesweeper3d.logic.Game;
import minesweeper3d.logic.GameStatus;

public class Test {

	public static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		simulateMinesweeper3D(3, 3, 5, 15);
		
	}

	public static void simulateMinesweeper3D(int rows, int columns, int depth, int mines) {
		Game game = new Game(rows, columns, depth, mines);
		
		System.out.println("Game bounds : 1 - " + rows + " ; 1 - " + columns + " ; 1 - " + depth);
		System.out.println(game);
		
		int x = sc.nextInt();
		int y = sc.nextInt();
		int z = sc.nextInt();
		
		GameStatus status = game.press(x, y, z, false);
		System.out.println(game);
		checkWinLose(status);
		
		while(status == GameStatus.Playing) {
			
			x = sc.nextInt();
			y = sc.nextInt();
			z = sc.nextInt();
			
			status = game.press(x, y, z, false);
			System.out.println(game);
			checkWinLose(status);
		}
	}
	
	public static void checkWinLose(GameStatus status) {
		if(status == GameStatus.Winning) {
			System.out.println("You win the game");
		}
		else if(status == GameStatus.Losing) {
			System.out.println("You lose the game");
		}
	}
}
