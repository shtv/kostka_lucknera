package cuboid.base;

public class Cube {
	private int x;
	private int y;
	private int z;
	
	public Cube(int x,int y,int z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Cube(Cube c)
	{
		this.x=c.getX();
		this.y=c.getY();
		this.z=c.getZ();
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getZ() {
		return z;
	}
	
	public void translate(int tx,int ty,int tz)
	{
		x+=tx;
		y+=ty;
		z+=tz;
	}
}
