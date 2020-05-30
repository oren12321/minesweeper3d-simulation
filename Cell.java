package minesweeper3d.logic;

public class Cell {
	
	public final CellType type;
	public final int number;
	public boolean covered;
	public boolean flagged;

	public Cell(CellType type, int number, boolean covered) {
		if(number < 0) {
			throw new IllegalArgumentException();
		}
		this.type = type;
		this.number = number;
		this.covered = covered;
		this.flagged = false;
	}

	public Cell(CellType type) {
		this(type, 0, true);
	}
	
	public void switchStatus() {
		covered = !covered;
	}

	public void switchHint() {
		flagged = !flagged;
	}
	
	@Override
	public String toString() {
		String returnedString = null;
		if(covered) {
			if(flagged) {
				returnedString = "Cf";
			}
			else {
				returnedString = "C";
			}
		}
		else {
			switch(type) {
			case Mine:
				returnedString = "+";
				break;
			case Number:
				returnedString = Integer.toString(number);
				break;
			case Empty:
				returnedString = " ";
				break;
			}
		}
			
		return returnedString;
	}
}
