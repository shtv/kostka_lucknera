package org.example;

public class Cube{
	long x,y,z;

	/**
	 * Constructs a new instance.
	 *
	 * @param x The x for this instance.
	 * @param y The y for this instance.
	 * @param z The z for this instance.
	 */
	public Cube(long x, long y, long z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public boolean equals(Object obj){
		if(!(obj instanceof Cube)) return false;

		Cube other = (Cube)obj;
		return other.getX()==x && other.getY()==y && other.getZ()==z;
	}

	/**
	 * Gets the x for this instance.
	 *
	 * @return The x.
	 */
	public long getX()
	{
		return this.x;
	}

	/**
	 * Gets the y for this instance.
	 *
	 * @return The y.
	 */
	public long getY()
	{
		return this.y;
	}

	/**
	 * Gets the z for this instance.
	 *
	 * @return The z.
	 */
	public long getZ()
	{
		return this.z;
	}
}