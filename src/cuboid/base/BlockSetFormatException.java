package cuboid.base;

public class BlockSetFormatException extends Exception{
	static final long serialVersionUID = 1L;

	String text;

	/**
	 * Constructs a new instance.
	 */
	public BlockSetFormatException()
	{
	}

	/**
	 * Constructs a new instance.
	 *
	 * @param text The text for this instance.
	 */
	public BlockSetFormatException(String text)
	{
		super(text);
		this.text = text;
	}

}
