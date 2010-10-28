package org.example;

public class MVector{
	long x1,x2;

	/**
	 * Constructs a new instance.
	 *
	 * @param x1 The x1 for this instance.
	 * @param x2 The x2 for this instance.
	 */
	public MVector(long x1, long x2)
	{
		this.x1 = x1;
		this.x2 = x2;
	}

	/**
	 * Gets the x1 for this instance.
	 *
	 * @return The x1.
	 */
	public long getX1()
	{
		return this.x1;
	}

	/**
	 * Sets the x1 for this instance.
	 *
	 * @param x1 The x1.
	 */
	public void setX1(long x1)
	{
		this.x1 = x1;
	}

	/**
	 * Gets the x2 for this instance.
	 *
	 * @return The x2.
	 */
	public long getX2()
	{
		return this.x2;
	}

	/**
	 * Sets the x2 for this instance.
	 *
	 * @param x2 The x2.
	 */
	public void setX2(long x2)
	{
		this.x2 = x2;
	}
}
