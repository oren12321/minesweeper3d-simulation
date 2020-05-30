package minesweeper3d.logic;

import java.util.ArrayList;
import java.util.List;

import static minesweeper3d.logic.MinesThrower.throwMine;

public class Cube {

	public final Cell[][][] cells;
	public final Dimension3D dimensions;
	private int mines;

	private List<Dimension3D> permutations;

	private int coveredCellsCount;
	private CubeStatus status;

	public static final int PADDING = 1;

	public Cube(int rows, int columns, int depth, int mines) {
		if(rows <= 0 || columns <= 0 || depth <= 0 || mines <= 0 || mines > rows * columns * depth) {
			throw new IllegalArgumentException();
		}

		cells = new Cell[rows + 2 * PADDING][columns + 2 * PADDING][depth + 2 * PADDING];
		dimensions = new Dimension3D(rows, columns, depth);
		this.mines = mines;
		coveredCellsCount = rows * columns * depth;
		status = CubeStatus.None;

		throwMines();
		initPossibleSurroundingMinesPermutations();
		initNumbers();
		completeNulls();
	}

	public CubeStatus getStatus() {
		return status;
	}

	private void initNumbers() {
		for(int d = PADDING; d <= dimensions.t; d++) {
			for(int r = PADDING; r <= dimensions.f; r++) {
				for(int c = PADDING; c <= dimensions.s; c++) {
					if(cells[r][c][d] == null) {
						int minesArroundCount = getMinesCountAroundCell(r, c, d).size();
						if(minesArroundCount > 0) {
							cells[r][c][d] = new Cell(CellType.Number, minesArroundCount, true);
						}
					}
				}
			}
		}
	}

	private void completeNulls() {
		for(int d = PADDING; d <= dimensions.t; d++) {
			for(int r = PADDING; r <= dimensions.f; r++) {
				for(int c = PADDING; c <= dimensions.s; c++) {
					if(cells[r][c][d] == null) {
						cells[r][c][d] = new Cell(CellType.Empty);
					}
				}
			}
		}
	}

	private void throwMines() {
		for(int i = 0; i < mines; i++) {
			Dimension3D location = throwMine(dimensions);
			if(cells[location.f][location.s][location.t] != null) {
				if(cells[location.f][location.s][location.t].type == CellType.Mine) {
					i--;
				}
			}
			else {
				cells[location.f][location.s][location.t] = new Cell(CellType.Mine);
			}
		}
	}

	private void initPossibleSurroundingMinesPermutations() {
		permutations = new ArrayList<>();

		final int NEXT_CELL = 1;
		final int PREVIOUS_CELL = -1;

		permutations.add(new Dimension3D(NEXT_CELL		, 0				, 0));
		permutations.add(new Dimension3D(PREVIOUS_CELL	, 0				, 0));
		permutations.add(new Dimension3D(0				, NEXT_CELL		, 0));
		permutations.add(new Dimension3D(0				, PREVIOUS_CELL	, 0));
		permutations.add(new Dimension3D(NEXT_CELL		, NEXT_CELL		, 0));
		permutations.add(new Dimension3D(NEXT_CELL		, PREVIOUS_CELL	, 0));
		permutations.add(new Dimension3D(PREVIOUS_CELL	, PREVIOUS_CELL	, 0));
		permutations.add(new Dimension3D(PREVIOUS_CELL	, NEXT_CELL		, 0));

		permutations.add(new Dimension3D(NEXT_CELL		, 0				, NEXT_CELL));
		permutations.add(new Dimension3D(PREVIOUS_CELL	, 0				, NEXT_CELL));
		permutations.add(new Dimension3D(0				, NEXT_CELL		, NEXT_CELL));
		permutations.add(new Dimension3D(0				, PREVIOUS_CELL	, NEXT_CELL));
		permutations.add(new Dimension3D(NEXT_CELL		, NEXT_CELL		, NEXT_CELL));
		permutations.add(new Dimension3D(NEXT_CELL		, PREVIOUS_CELL	, NEXT_CELL));
		permutations.add(new Dimension3D(PREVIOUS_CELL	, PREVIOUS_CELL	, NEXT_CELL));
		permutations.add(new Dimension3D(PREVIOUS_CELL	, NEXT_CELL		, NEXT_CELL));
		permutations.add(new Dimension3D(0				, 0				, NEXT_CELL));

		permutations.add(new Dimension3D(NEXT_CELL		, 0				, PREVIOUS_CELL));
		permutations.add(new Dimension3D(PREVIOUS_CELL	, 0				, PREVIOUS_CELL));
		permutations.add(new Dimension3D(0				, NEXT_CELL		, PREVIOUS_CELL));
		permutations.add(new Dimension3D(0				, PREVIOUS_CELL	, PREVIOUS_CELL));
		permutations.add(new Dimension3D(NEXT_CELL		, NEXT_CELL		, PREVIOUS_CELL));
		permutations.add(new Dimension3D(NEXT_CELL		, PREVIOUS_CELL	, PREVIOUS_CELL));
		permutations.add(new Dimension3D(PREVIOUS_CELL	, PREVIOUS_CELL	, PREVIOUS_CELL));
		permutations.add(new Dimension3D(PREVIOUS_CELL	, NEXT_CELL		, PREVIOUS_CELL));
		permutations.add(new Dimension3D(0				, 0				, PREVIOUS_CELL));
	}

	private List<Dimension3D> getMinesCountAroundCell(int x, int y, int z) {
		List<Dimension3D> nearMinesLocations = new ArrayList<>(); 

		for(Dimension3D permutation : permutations) {
			Dimension3D location = new Dimension3D(x + permutation.f, y + permutation.s, z + permutation.t);
			Cell nearCell = cells[location.f][location.s][location.t];
			if(nearCell != null) {
				if(nearCell.type == CellType.Mine) {
					nearMinesLocations.add(location);
				}
			}
		}

		return nearMinesLocations;
	}

	public void revealAllMines() {
		for(int d = PADDING; d <= dimensions.t; d++) {
			for(int r = PADDING; r <= dimensions.f; r++) {
				for(int c = PADDING; c <= dimensions.s; c++) {
					if(cells[r][c][d] != null) {
						if(cells[r][c][d].type == CellType.Mine) {
							revealCell(r, c, d);
						}
					}
				}
			}
		}
	}
	
	public void flagCell(int x, int y, int z) {
		Cell cell = cells[x][y][z];
		if(cell != null) {
			if(cell.covered) {
				cell.switchHint();
			}
		}
	}

	public void revealCellsAround(int x, int y, int z) {
		Cell cell = cells[x][y][z];
		if(cell != null) {
			if(!cell.covered) {
				if(cell.type == CellType.Number) {
					int minesAroundCount = cell.number;
					int flaggedCellsArountCount = 0;
					List<Dimension3D> unflaggedCoveredAroundLocations = new ArrayList<>();
					for(Dimension3D permutation : permutations) {
						Cell nearCell = cells[x + permutation.f][y + permutation.s][z + permutation.t];
						if(nearCell != null) {
							if(nearCell.flagged) {
								flaggedCellsArountCount++;
							}
							else if(nearCell.covered) {
								unflaggedCoveredAroundLocations.add(new Dimension3D(x + permutation.f, y + permutation.s, z + permutation.t));
							}
						}
					}
					if(minesAroundCount == flaggedCellsArountCount) {
						boolean passed = true;
						for(Dimension3D unflaggedCoveredAroundLocation : unflaggedCoveredAroundLocations) {
							revealCell(unflaggedCoveredAroundLocation.f, unflaggedCoveredAroundLocation.s, unflaggedCoveredAroundLocation.t);
							if(status == CubeStatus.Fail) {
								passed = false;
							}
						}
						if(!passed) {
							status = CubeStatus.Fail;
						}
					}
				}
			}
		}
	}
	
	public void revealCell(int x, int y, int z) {
		Cell cell = cells[x][y][z];
		if(cell != null) {
			if(cell.covered) {
				if(cell.type == CellType.Number) {
					cell.switchStatus();
					coveredCellsCount--;
					if(coveredCellsCount == mines) {
						status = CubeStatus.Pass;
					}
				}
				else if(cell.type == CellType.Mine) {
					cell.switchStatus();
					coveredCellsCount--;

					for(Dimension3D permutation : permutations) {
						Cell nearCell = cells[x + permutation.f][y + permutation.s][z + permutation.t];
						if(nearCell != null) {
							if(nearCell.type == CellType.Empty) {
								revealCell(x + permutation.f, y + permutation.s, z + permutation.t);
							}
						}
					}

					status = CubeStatus.Fail;
				}
				else if(cell.type == CellType.Empty) {
					cell.switchStatus();
					coveredCellsCount--;
					revealCellRecursive(x, y, z);
					if(coveredCellsCount == mines) {
						status = CubeStatus.Pass;
					}
				}
			}
		}
	}

	private void revealCellRecursive(int x, int y, int z) {
		List<Dimension3D> recursiveStack = new ArrayList<>();
		for(Dimension3D permutation : permutations) {
			Cell cell = cells[x + permutation.f][y + permutation.s][z + permutation.t];
			if(cell != null) {
				if(cell.covered) {
					if(cell.type == CellType.Number) {
						cell.switchStatus();
						coveredCellsCount--;
					}
					else if(cell.type == CellType.Empty) {
						cell.switchStatus();
						coveredCellsCount--;
						recursiveStack.add(new Dimension3D(x + permutation.f, y + permutation.s, z + permutation.t));
					}
				}
			}
		}
		for(Dimension3D element : recursiveStack) {
			revealCellRecursive(element.f, element.s, element.t);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for(int i = PADDING; i <= dimensions.s; i++) { sb.append("X\t"); }
		sb.append("\n");

		for(int d = PADDING; d <= dimensions.t; d++) {

			sb.append("Page No. " + d + " :\n");

			for(int r = PADDING; r <= dimensions.f; r++) {
				for(int c = PADDING; c <= dimensions.s; c++) {
					sb.append(cells[r][c][d] + "\t");
				}
				sb.append("\n");
			}
			sb.append("\n");
		}

		for(int i = PADDING; i <= dimensions.s; i++) { sb.append("X\t"); }
		sb.append("\n");

		return sb.toString();
	}
}
