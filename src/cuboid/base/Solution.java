package cuboid.base;

import java.util.List;

public class Solution {
		private List<Move> sequence;
		private int volume;

		public int compareTo(Object o) {
			Solution other = (Solution) o;
			
			return getVolume().compareTo(other.getVolume());
		}

		public Integer getVolume(){
			return 0;
		}
}
