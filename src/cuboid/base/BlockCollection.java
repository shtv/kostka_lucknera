package cuboid.base;

public class BlockCollection {
	Block block;
	int amount;

	/**
	 * Constructs a new instance.
	 *
	 * @param block The block for this instance.
	 * @param amount The amount for this instance.
	 */
	public BlockCollection(Block block, int amount)
	{
		this.block = block;
		this.amount = amount;
	}

	/**
	 * Gets the block for this instance.
	 *
	 * @return The block.
	 */
	public Block getBlock()
	{
		return this.block;
	}

	/**
	 * Gets the amount for this instance.
	 *
	 * @return The amount.
	 */
	public int getAmount()
	{
		return this.amount;
	}

	public String toString() {
		return "bc[amount="+amount+",block="+block+"]";
	}

}
