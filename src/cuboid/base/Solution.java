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

		public void clone(int numberOfClones,int lengthLimit){
			// kopiowanie sekwencji ruchów
			// dzięki czemu mamy kopie takich samych solution
			// potem je dostawiamy do istniejącego
			// tym samym powiększamy rozmiar naszego prostopadłościanu
			//
			// opisane w dokumentacji, tutaj do zrobienia na 3. etap
		}
}
