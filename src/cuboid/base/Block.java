package cuboid.base;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class Block {
		private static final double deg2rad=Math.PI/180; 
		private int maxX;
		private int maxY;
		private int maxZ;
		private int minX;
		private int minY;
		private int minZ;
		Set<Cube> cubes;
		String structure;
		
		public Block()
		{
			maxX=maxY=maxZ=Integer.MIN_VALUE;
			minX=minY=minZ=Integer.MAX_VALUE;
			cubes=null;
		}
		
		public Block(Block b)
		{
			this.maxX=b.getMaxX();
			this.maxY=b.getMaxY();
			this.maxZ=b.getMaxZ();
			this.minX=b.getMinX();
			this.minY=b.getMinY();
			this.minZ=b.getMinZ();
			if(b.getStructure()!=null)
				this.structure=new String(b.getStructure());
			cubes=new HashSet<Cube>();
			Iterator<Cube> it=b.cubes.iterator();
			while(it.hasNext())
				cubes.add(new Cube(it.next()));
		}
		
		public Block(String structure) throws BlockSetFormatException{
			this.structure = structure;
			cubes = new HashSet<Cube>();
			Stack<Cube> stack = new Stack<Cube>();
			char[] cArray = structure.toCharArray();
			Cube prev = new Cube(0,0,0);
			add(prev.getX(),prev.getY(),prev.getZ());
			for(char c:cArray){
				switch(c){
					case '(':
						stack.push(prev);
						continue;
					case ')':
						prev = stack.pop();
						if(prev==null)
							throw new BlockSetFormatException("Invalid strucure. In some structure prefix closing brackets more than opening one's.");
						continue;
					case 'W':
						prev = new Cube(prev.getX()-1,prev.getY(),prev.getZ());
						break;
					case 'E':
						prev = new Cube(prev.getX()+1,prev.getY(),prev.getZ());
						break;
					case 'N':
						prev = new Cube(prev.getX(),prev.getY()+1,prev.getZ());
						break;
					case 'S':
						prev = new Cube(prev.getX(),prev.getY()-1,prev.getZ());
						break;
					case 'D':
						prev = new Cube(prev.getX(),prev.getY(),prev.getZ()-1);
						break;
					case 'U':
						prev = new Cube(prev.getX(),prev.getY(),prev.getZ()+1);
						break;
					default:
						throw new BlockSetFormatException("Char "+c+" is not allowed in structure.");
				}
//				System.out.println("dodajemy = "+prev);
				add(prev.getX(),prev.getY(),prev.getZ());
			}
			if(!stack.isEmpty())
				throw new BlockSetFormatException("More opening brackets than closing one's in structure.");
		}

		public int computeMaxLength(){
			for(Cube cube:cubes){
				if(cube.getX()>maxX)
					maxX = cube.getX();
				else if(cube.getX()<minX)
					minX = cube.getX();

				if(cube.getY()>maxY)
					maxY = cube.getY();
				else if(cube.getY()<minY)
					minY = cube.getY();

				if(cube.getZ()>maxZ)
					maxZ = cube.getZ();
				else if(cube.getZ()<minZ)
					minZ = cube.getZ();

			}
			return 1+Math.max(maxX-minX,Math.max(maxY-minY,maxZ-minZ));
		}
		
		/**
		 * Constructs a new instance.
		 *
		 * @param cubes The cubes for this instance.
		 */
		public Block(Set<Cube> cubes)
		{
			this.cubes = cubes;
		}
		
		public int getMinX() {
			return minX;
		}

		public int getMinY() {
			return minY;
		}

		public int getMinZ() {
			return minZ;
		}
		
		public void add(int x,int y,int z)
		{
			if(cubes==null)
				cubes=new HashSet<Cube>();
			cubes.add(new Cube(x,y,z));
			if(x>maxX) maxX=x;
			if(y>maxY) maxY=y;
			if(z>maxZ) maxZ=z;
			if(x<minX) minX=x;
			if(y<minY) minY=y;
			if(z<minZ) minZ=z;
		}

		public void resetMaxMin(){
			Iterator<Cube> it = cubes.iterator();
			Cube cube = it.next();
			maxX = cube.getX();
			maxY = cube.getY();
			maxZ = cube.getZ();
			minX = cube.getX();
			minY = cube.getY();
			minZ = cube.getZ();
			for(Cube cube2:cubes){
				int x = cube2.getX();
				int y = cube2.getY();
				int z = cube2.getZ();
				if(x>maxX) maxX=x;
				if(y>maxY) maxY=y;
				if(z>maxZ) maxZ=z;
				if(x<minX) minX=x;
				if(y<minY) minY=y;
				if(z<minZ) minZ=z;
			}
		}
		
		public void orient(Orientation o)
		{
			int[] minXYZ={Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE};
			int[] maxXYZ={Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE};
			double xAngle=o.getAngleX()*deg2rad;
			double yAngle=o.getAngleY()*deg2rad;
			double zAngle=o.getAngleZ()*deg2rad;
			int[][] xRot={{1,0,0},
					      {0,(int) Math.round(Math.cos(xAngle)),(int) Math.round(-1*Math.sin(xAngle))},
					      {0,(int)Math.round(Math.sin(xAngle)),(int)Math.round(Math.cos(xAngle))}};
			int[][] yRot={{(int)Math.round(Math.cos(yAngle)),0,(int)Math.round(Math.sin(yAngle))},
						  {0,1,0},
						  {(int)Math.round(-1*Math.sin(yAngle)),0,(int)Math.round(Math.cos(yAngle))}};
			int[][] zRot={{(int)Math.round(Math.cos(zAngle)),(int)Math.round(-1*Math.sin(zAngle)),0},
					      {(int)Math.round(Math.sin(zAngle)),(int)Math.round(Math.cos(zAngle)),0},
					      {0,0,1}};
			
			Iterator<Cube> it=cubes.iterator();
			while(it.hasNext())
			{
				Cube cc=it.next();
				int[] in={cc.getX(),cc.getY(),cc.getZ()};
				int[] out={0,0,0};
								
				for(int i=0;i<out.length;i++)
					for(int j=0;j<xRot[i].length;j++)
						out[i]+=xRot[i][j]*in[j];
				
				for(int i=0;i<in.length;i++)
				{
						in[i]=out[i];
						out[i]=0;
				}
				
				for(int i=0;i<out.length;i++)
					for(int j=0;j<yRot[i].length;j++)
						out[i]+=yRot[i][j]*in[j];
				for(int i=0;i<in.length;i++)
				{
						in[i]=out[i];
						out[i]=0;
				}
				
				for(int i=0;i<out.length;i++)
					for(int j=0;j<zRot[i].length;j++)
						out[i]+=zRot[i][j]*in[j];
				
				for(int i=0;i<minXYZ.length;i++)
				{
					if(minXYZ[i]>out[i])
						minXYZ[i]=out[i];
					if(maxXYZ[i]<out[i])
						maxXYZ[i]=out[i];
				}
				
				cc.setX(out[0]);
				cc.setY(out[1]);
				cc.setZ(out[2]);
			}
			maxX=maxXYZ[0];
			maxY=maxXYZ[1];
			maxZ=maxXYZ[2];
			minX=minXYZ[0];
			minY=minXYZ[1];
			minZ=minXYZ[2];
			//translate(-minXYZ[0],-minXYZ[1],-minXYZ[2]);
		}
		
		public void translate(int tx,int ty,int tz)
		{
			Iterator<Cube> it=cubes.iterator();
			while(it.hasNext())
				it.next().translate(tx,ty,tz);
			maxX+=tx;
			maxY+=ty;
			maxZ+=tz;
		}
		
		public void addBlock(Vector3D v, Block b)
		{
			int dx,dy,dz;
			if(v==null)
			{
				dx=dy=dz=0;
			}
			else
			{
				dx=v.getX(); dy=v.getY(); dz=v.getZ();
			}
			Iterator<Cube> it = b.getCubes().iterator();
			while(it.hasNext())
			{
				Cube oc=it.next();
				this.cubes.add(new Cube(oc.getX()+dx, oc.getY()+dy, oc.getZ()+dz));
				if(oc.getX()+dx>maxX) maxX=oc.getX()+dx;
				if(oc.getX()+dx<minX) minX=oc.getX()+dx;
				if(oc.getY()+dy>maxY) maxY=oc.getY()+dy;
				if(oc.getY()+dy<minY) minY=oc.getY()+dy;
				if(oc.getZ()+dz>maxZ) maxZ=oc.getZ()+dz;
				if(oc.getZ()+dz<minZ) minZ=oc.getZ()+dz;
			}
		}
		
		public int getMaxX()
		{
			return maxX;
		}

		public int getMaxY() {
			return maxY;
		}

		public int getMaxZ() {
			return maxZ;
		}
		
		public int getCubesCount()
		{
			return cubes.size();
		}
		
		public Set<Cube> getCubes()
		{
			return cubes;
		}
		
		public int[][][] getMatrix()
		{
			int[][][] result=new int[maxX-minX+1][maxY-minY+1][maxZ-minZ+1];
			Iterator<Cube> it=cubes.iterator();
			while(it.hasNext())
			{
				Cube cc=it.next();
				result[cc.getX()-minX][cc.getY()-minY][cc.getZ()-minZ]=1;
			}
			return result;
		}
		
		public String getStructure()
		{
			return this.structure;
		}

		/**
		 * Sets the structure for this instance.
		 *
		 * @param structure The structure.
		 */
		public void setStructure(String structure)
		{
			this.structure = structure;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("b[");
			for(Cube cube:cubes)
				sb.append(cube);
			sb.append("]");
			return sb.toString();
		}
}
