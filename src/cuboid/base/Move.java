package cuboid.base;

public class Move {
			private int id;
		private Block b;
		private Vector3D fit;
		private Orientation orientation;
		int orId;
		
		public Move()
		{
			setId(-1);
			b=null;
			fit=null;
			orientation=null;
		}
		
		public Move(int id,Block block, Vector3D fit, Orientation orientation,int orId)
		{
			this.setId(id);
			this.b=block;
			this.fit=fit;
			this.orientation=orientation;
			this.orId = orId;
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
		/**
		 * Gets the orId for this instance.
		 *
		 * @return The orId.
		 */
		public int getOrId()
		{
			return this.orId;
		}

		/**
		 * Sets the orId for this instance.
		 *
		 * @param orId The orId.
		 */
		public void setOrId(int orId)
		{
			this.orId = orId;
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
