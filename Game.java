package minesweeper3d.logic;

public class Game {

	public final Cube cube;
	
	public Game(int rows, int columns, int depth, int mines) {
		cube = new Cube(rows, columns, depth, mines);
	}

	public GameStatus press(int x, int y, int z, boolean around) {
		if(around) {
			cube.revealCellsAround(x, y, z);
		}
		else {
			cube.revealCell(x, y, z);
		}
		
		if(cube.getStatus() == CubeStatus.Fail) {
			return GameStatus.Losing;
		}
		else if(cube.getStatus() == CubeStatus.Pass) {
			return GameStatus.Winning;
		}
		return GameStatus.Playing;
	}
	
	public void mark(int x, int y, int z) {
		cube.flagCell(x, y, z);
	}
	
	public void pressMines() {
		cube.revealAllMines();
	}
	
	@Override
	public String toString() {
		return cube.toString();
	}
}
