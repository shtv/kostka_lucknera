package cuboid.base;

import java.util.ArrayList;
import java.util.List;

public class Solution {
		private List<Move> sequence;
		private Integer volume;

		/**
		 * Constructs a new instance.
		 */
		public Solution()
		{
			sequence = new ArrayList<Move>();
		}

		public int compareTo(Object o) {
			Solution other = (Solution) o;
			
			return getVolume().compareTo(other.getVolume());
		}

		/**
		 * Gets the sequence for this instance.
		 *
		 * @return The sequence.
		 */
		public List<Move> getSequence()
		{
			return this.sequence;
		}

		/**
		 * Sets the sequence for this instance.
		 *
		 * @param sequence The sequence.
		 */
		public void setSequence(List<Move> sequence)
		{
			this.sequence = sequence;
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

		/**
		 * Sets the volume for this instance.
		 *
		 * @param volume The volume.
		 */
		public void setVolume(int volume)
		{
			this.volume = volume;
		}
}
