package cuboid.base;

public class Orientation {
		private double angleX;
		private double angleY;
		private double angleZ;
		
		public Orientation(double angleX,double angleY,double angleZ)
		{
			this.angleX=angleX;
			this.angleY=angleY;
			this.angleZ=angleZ;
		}
		
		public double getAngleX()
		{
			return angleX;
		}
		
		public double getAngleY()
		{
			return angleY;
		}
		
		public double getAngleZ()
		{
			return angleZ;
		}
		
		public Orientation clone()
		{
			return new Orientation(this.angleX, this.angleY, this.angleZ);
		}
		
		public String toString()
		{
			return "("+angleX+","+angleY+","+angleZ+")";
		}
}
