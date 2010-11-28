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

		public void clone(int numberOfClones,int lengthLimit){
			// kopiowanie sekwencji ruchów
			// dzięki czemu mamy kopie takich samych solution
			// potem je dostawiamy do istniejącego
			// tym samym powiększamy rozmiar naszego prostopadłościanu
			//
			// opisane w dokumentacji, tutaj do zrobienia na 3. etap
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
