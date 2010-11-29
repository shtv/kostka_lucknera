package cuboid.base;

public class Vector3D {
	private int x;
	private int y;
	private int z;
	
	public Vector3D()
	{
		x=y=z=0;
	}
	
	public Vector3D(int x,int y,int z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getZ()
	{
		return z;
	}
	
	public void setX(int x)
	{
		this.x=x;
	}
	
	public void setY(int y)
	{
		this.y=y;
	}
	
	public void setZ(int z)
	{
		this.z=z;
	}
	
	public Vector3D clone()
	{
		return new Vector3D(x,y,z);
	}
	
	public String toString()
	{
		return "["+x+","+y+","+z+"]";
	}
}