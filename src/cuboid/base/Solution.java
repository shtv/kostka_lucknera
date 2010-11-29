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
			/*
			 * a, b i c to dopuszczalne liczby klonów wzdłuż kolejnych osi
			 */
			Block firstBlock = sequence.get(0).getB();
			firstBlock.orient(sequence.get(0).getOrientation());
			int maxX = firstBlock.getMaxX();
			int minX = firstBlock.getMinX();
			int maxY = firstBlock.getMaxY();
			int minY = firstBlock.getMinY();
			int maxZ = firstBlock.getMaxZ();
			int minZ = firstBlock.getMinZ();
			for(Move move:sequence){
				Block block = move.getB();
				block.orient(move.getOrientation());
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
			int a = (maxX-minX)/lengthLimit; // maks. dopuszczalnych klonów wzdłuż osi X
			int b = (maxY-minY)/lengthLimit;
			int c = (maxZ-minZ)/lengthLimit;
			int l = sequence.size();
			int dx = maxX-minX;
			int dy = maxY-minY;
			int dz = maxZ-minZ;
			if(a*b*c>numberOfClones){ // gdy niepełny
				int m = Math.max(a,Math.max(b,c));
				if(m==a){
					for(int i=1;i<m;++i){
						for(int j=0;j<l;++j){
							Move move = (Move)sequence.get(j);
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
			}
		}
}
