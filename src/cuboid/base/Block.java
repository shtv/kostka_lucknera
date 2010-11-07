package cuboid.base;

import java.util.Set;

public class Block {
	private int maxX;
	private int maxY;
	private int maxZ;
	private int cuboidsCount;
	Set<Cube> cubes;
	/**
	 * It is a Luckner's code. For example, a simple chair's code would be
	 * (U)(D)LLU.
	 */
	String id;
	
	public int getMaxX()
	{
		return maxX;
	}


	public int getMaxY() {
		return maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}
	
	public int getCuboidsCount()
	{
		return cuboidsCount;
	}
}
