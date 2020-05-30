package minesweeper3d.logic;

import java.util.Random;

public  class MinesThrower {

	public static Random rand = new Random();
	
	static {
		rand.setSeed(System.nanoTime());
	}
	
	public static Dimension3D throwMine(Dimension3D dimensions) {
		return new Dimension3D(
				rand.nextInt(dimensions.f) + 1,
				rand.nextInt(dimensions.s) + 1,
				rand.nextInt(dimensions.t) + 1);
	}

}
