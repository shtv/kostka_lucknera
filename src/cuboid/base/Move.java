package cuboid.base;

public class Move {
	int id;
	int x;
	int y;
	int z;
	/**
	 * Constructs a new instance.
	 *
	 * @param id The id for this instance.
	 * @param x The x for this instance.
	 * @param y The y for this instance.
	 * @param z The z for this instance.
	 */
	public Move(int id, int x, int y, int z)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	/**
	 * Constructs a new instance.
	 */
	public Move()
	{
	}
	/**
	 * Gets the id for this instance.
	 *
	 * @return The id.
	 */
	public int getId()
	{
		return this.id;
	}
	/**
	 * Sets the id for this instance.
	 *
	 * @param id The id.
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	/**
	 * Gets the x for this instance.
	 *
	 * @return The x.
	 */
	public int getX()
	{
		return this.x;
	}
	/**
	 * Sets the x for this instance.
	 *
	 * @param x The x.
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	/**
	 * Gets the y for this instance.
	 *
	 * @return The y.
	 */
	public int getY()
	{
		return this.y;
	}
	/**
	 * Sets the y for this instance.
	 *
	 * @param y The y.
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	/**
	 * Gets the z for this instance.
	 *
	 * @return The z.
	 */
	public int getZ()
	{
		return this.z;
	}
	/**
	 * Sets the z for this instance.
	 *
	 * @param z The z.
	 */
	public void setZ(int z)
	{
		this.z = z;
	}

}
