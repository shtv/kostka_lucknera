package cuboid.base;

public class Move {
	    private int id;
		private Block b;
		private Vector3D fit;
		private Orientation orientation;
		
		public Move()
		{
			setId(-1);
			b=null;
			fit=null;
			orientation=null;
		}
		
		public Move(int id,Block block, Vector3D fit, Orientation orientation)
		{
			this.setId(id);
			this.b=block;
			this.fit=fit;
			this.orientation=orientation;
		}
		
		public void setFit(Vector3D fit) {
			this.fit = fit;
		}
		public Vector3D getFit() {
			return fit;
		}
		public void setB(Block b) {
			this.b = b;
		}
		public Block getB() {
			return b;
		}
		public void setOrientation(Orientation orientation) {
			this.orientation = orientation;
		}
		public Orientation getOrientation() {
			return orientation;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
		
		public int getX()
		{
			return fit.getX();
		}
		
		public int getY()
		{
			return fit.getY();
		}
		
		public int getZ()
		{
			return fit.getZ();
		}
}
