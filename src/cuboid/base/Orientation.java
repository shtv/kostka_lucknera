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
}
