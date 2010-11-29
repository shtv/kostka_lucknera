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
			int a = 1;
			int b = 1;
			int c = 1;
			if(a*b*c>numberOfClones){ // gdy niepełny
				;
			}
		}
}
