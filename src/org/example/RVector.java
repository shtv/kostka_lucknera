package org.example;

public class RVector{
	long x,y,z;

	/**
	 * Constructs a new instance.
	 *
	 * @param x The x for this instance.
	 * @param y The y for this instance.
	 * @param z The z for this instance.
	 */
	public RVector(long x, long y, long z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
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
	 * Sets the x for this instance.
	 *
	 * @param x The x.
	 */
	public void setX(long x)
	{
		this.x = x;
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
	 * Sets the y for this instance.
	 *
	 * @param y The y.
	 */
	public void setY(long y)
	{
		this.y = y;
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

	/**
	 * Sets the z for this instance.
	 *
	 * @param z The z.
	 */
	public void setZ(long z)
	{
		this.z = z;
	}
}
