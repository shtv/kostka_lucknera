package cuboid.base;

import java.util.LinkedList;
import java.util.List;

public class Solution {
		private List<Move> sequence;

		private int volume;

		public Solution()
		{
			sequence=new LinkedList<Move>();
			volume=0;
		}
		
		public List<Move> getSequence() {
			return sequence;
		}
		
		public void addNextMove(Move m)
		{
			sequence.add(m);
			volume+=m.getB().getCubesCount();
		}
		
		public int compareTo(Object o) {
			Solution other = (Solution) o;
			
			return getVolume().compareTo(other.getVolume());
		}

		public Integer getVolume(){
			return volume;
		}

		public void clone(List<BlockCollection> blockCollections,int numberOfClones,int lengthLimit){
//			System.out.println("1. clone blockCollections:");
//			for(BlockCollection bc:blockCollections)
//				System.out.println(bc);
//			System.out.println("nofCl = "+numberOfClones);
//			System.out.println("lengthLIm = "+lengthLimit);
//			System.out.println("1. clone blockCollections: end");
			/*
			 * a, b i c to dopuszczalne liczby klonów wzdłuż kolejnych osi
			 */
			Block firstBlock = new Block(sequence.get(0).getB());
			firstBlock.orient(sequence.get(0).getOrientation());
			for(Cube cube:firstBlock.getCubes()){
				cube.setX(cube.getX()+sequence.get(0).getX());
				cube.setY(cube.getY()+sequence.get(0).getY());
				cube.setZ(cube.getZ()+sequence.get(0).getZ());
			}
			int maxX = firstBlock.getMaxX();
			int minX = firstBlock.getMinX();
			int maxY = firstBlock.getMaxY();
			int minY = firstBlock.getMinY();
			int maxZ = firstBlock.getMaxZ();
			int minZ = firstBlock.getMinZ();
//			System.out.println("maxX = "+maxX+", minX = "+minX+", maxY = "+maxY+", minY = "+minY+", maxZ = "+maxZ+", minZ = "+minZ);
			for(Move move:sequence){
				Block block = new Block(move.getB());
//				System.out.println(block);
				block.orient(move.getOrientation());
//				System.out.println("after orient:");
				for(Cube cube:block.getCubes()){
					cube.setX(cube.getX()+move.getX());
					cube.setY(cube.getY()+move.getY());
					cube.setZ(cube.getZ()+move.getZ());
				}
				block.resetMaxMin();
//				System.out.println(block);
				if(block.getMaxX()>maxX)
					maxX = block.getMaxX();
				else if(block.getMinX()<minX)
					minX = block.getMinX();
				if(block.getMaxY()>maxY)
					maxY = block.getMaxY();
				else if(block.getMinY()<minY)
					minY = block.getMinY();
				if(block.getMaxZ()>maxZ)
					maxZ = block.getMaxZ();
				else if(block.getMinZ()<minZ)
					minZ = block.getMinZ();
			}
//			System.out.println("maxX = "+maxX+", minX = "+minX+", maxY = "+maxY+", minY = "+minY+", maxZ = "+maxZ+", minZ = "+minZ);
			int a = lengthLimit/(1+maxX-minX); // maks. dopuszczalnych klonów wzdłuż osi X
			int b = lengthLimit/(1+maxY-minY);
			int c = lengthLimit/(1+maxZ-minZ);
//			System.out.println("a="+a+",b="+b+",c="+c);
			int l = sequence.size();
			int dx = maxX-minX+1;
			int dy = maxY-minY+1;
			int dz = maxZ-minZ+1;
			int a1 = 0;
			int b1 = 0;
			int c1 = 0;
			for(int i=a;i>0;--i)
				for(int j=b;j>0;--j)
					for(int k=c;k>0;--k)
						if(i*j*k<=numberOfClones && i*j*k>a1*b1*c1){
							a1 = i;
							b1 = j;
							c1 = k;
						}
//			System.out.println("ok");
			a = a1;
			b = b1;
			c = c1;
			/*
			if(a*b*c>numberOfClones){ // gdy niepełny
				a = Math.min(a,numberOfClones);
				b = Math.min(b,numberOfClones);
				c = Math.min(c,numberOfClones);
				int m = Math.max(a,Math.max(b,c));
				if(m==a){
//					System.out.println("wybrano a");
					for(int i=1;i<m;++i){
						for(int j=0;j<l;++j){
							Move move = (Move)(sequence.get(j)).clone();
							move.getFit().setX(move.getFit().getX()+i*dx);
							sequence.add(move);
						}
					}
				}else if(m==b){
					for(int i=1;i<m;++i){
						for(int j=0;j<l;++j){
							Move move = (Move)sequence.get(j);
							move.getFit().setY(move.getFit().getY()+i*dy);
							sequence.add(move);
						}
					}
				}else{
					for(int i=1;i<m;++i){
						for(int j=0;j<l;++j){
							Move move = (Move)sequence.get(j);
							move.getFit().setZ(move.getFit().getZ()+i*dz);
							sequence.add(move);
						}
					}
				}
			}else{
//				System.out.println("pełny");
				*/
			for(int i=0;i<a;++i)
				for(int j=0;j<b;++j)
					for(int k=0;k<c;++k){
//						System.out.println("i="+i+",j="+j+",k="+k);
						if(i==0 && j==0 && k==0) continue;
						for(int p=0;p<l;++p){
//							System.out.println("p="+p);
							Move move = (Move)(sequence.get(p)).clone();
							Vector3D fit = move.getFit();
							fit.setX(fit.getX()+i*dx);
							fit.setY(fit.getY()+j*dy);
							fit.setZ(fit.getZ()+k*dz);
							sequence.add(move);
						}
					}
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("solution[volume="+volume+",sequence=");
			for(Move move:sequence)
				sb.append(move+",");
			sb.append("]");
			return sb.toString();
		}
}
