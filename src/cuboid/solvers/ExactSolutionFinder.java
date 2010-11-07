package cuboid.solvers;

import java.util.*;
import cuboid.base.*;


public class ExactSolutionFinder 
{
		private class ListElement
		{
			private final int n;
			private int oidx; //current orientation index
			private boolean oidxReset;
			private Block[] orientedBlock;
			private Orientation[] orientation;
			private Vector3D fit;
			private boolean generateFirstFit;
			
			public ListElement(int narg,Block block)
			{
				oidx=0;
				oidxReset=false;
				generateFirstFit=true;
				n=narg;
			}
			
			public int getN()
			{
				return n;
			}
			
			public Orientation getOrientation()
			{
				return orientation[oidx];
			}
			
			public Vector3D getFit()
			{
				return fit;
			}
			
			public void setFit(Vector3D v)
			{
				generateFirstFit=false;
				fit=v;
			}
			
			public void resetFit()
			{
				generateFirstFit=true;
			}
			
			public void incOidx()
			{
				oidxReset=false;
				oidx++;
				if(oidx>=orientedBlock.length)
				{
					oidx=0;
					oidxReset=true;
				}
			}
			
			public void setOidx(int v)
			{
				oidxReset=false;
				oidx=v;
				if(oidx>=orientedBlock.length)
				{
					oidx=0;
					oidxReset=true;
				}
			}
			
			public int getOidx()
			{
				return oidx;
			}
			
			public boolean isOidxReset()
			{
				return oidxReset;
			}
		}
	
		private Solution bestSolution;
		private List<ListElement> blocks;
		private boolean permGenerated; //udalo sie wygenerowac kolejna premutacje
	
		public ExactSolutionFinder(List<Block> argblocks)
		{
				int counter=0;
				permGenerated=false;
				blocks=new LinkedList<ListElement>();
				for(Iterator<Block> it=argblocks.iterator();it.hasNext();)
					blocks.add(new ListElement(++counter,it.next()));
		}
		
		private void nextPerm()
		{
			int i=0,j=0,k=0;
			permGenerated=false;
			if(blocks.size()<2)
				return;
			i=blocks.size()-1;
			while(i>0 && blocks.get(i-1).getN()>=blocks.get(i).getN())
				i--;
			if(i>0)
			{
				j=blocks.size();
				while(j>0 && blocks.get(j-1).getN()<=blocks.get(i-1).getN())
					j--;
			}
			if(i>0 && j>0)
			{
				Collections.swap(blocks, i-1, j-1);
				permGenerated=true;
			}
			for(i++, j=blocks.size(); i<j; i++, j--)
				Collections.swap(blocks, i-1, j-1);
		}

		private void checkPerm()
		{
			ListElement currentElem;
			ListIterator<ListElement> q,p;
			q=blocks.listIterator();
			p=blocks.listIterator();
			
			while(true)
			{
				currentElem=q.next();
				if(!nextFit(q.previousIndex(),currentElem))
				{
					currentElem.incOidx();
					if(currentElem.isOidxReset())
					{
						q.previous();
						if(q.hasPrevious())
							q.previous();
						else
							break;
						continue;
					}
					currentElem.resetFit();
				}
				if(checkSequence(q.previousIndex()))
				{
					q.previous();
					q.previous();
					continue;
				}
				if(q.hasNext())
					continue;
				else
				{
					while(!currentElem.isOidxReset())
					{
						while(nextFit(q.previousIndex(),currentElem))
							checkSequence(q.previousIndex());
						currentElem.resetFit();
						currentElem.incOidx();
					}
					q.previous();
					q.previous();
					continue;
				}		
			}
		}
		
		/**
		 *	Jeżeli sprawdzana sekwencja daje prawidlowe rozwiazanie
		 *  to funkcja sprawdza czy jest ono lepsze od bestSolution
		 *  i ewentualnie je zapamietuje.
		 *  
		 *  @return Funkcja zwraca true jeżeli przekroczono ograniczenie
		 *  wymiaru rozwiazania. 
		 */
		private boolean checkSequence(int idx)
		{
			return false;
		}
		
		/**
		 *  Zwraca czy udalo sie wygenerowac kolejne dopasowanie
		 */
		private boolean nextFit(int idx, ListElement elem)
		{
			return false;
		}
		
		public Solution getSolution()
		{
			do
			{
				checkPerm();
				nextPerm();
			}while(permGenerated);
			return bestSolution;
		}
		
		public void print()
		{
			ListIterator<ListElement> it=blocks.listIterator();
			System.out.println();
			while(it.hasNext())
				System.out.print(it.next().getN()+" ");
			System.out.println();
		}
		
		public static void main(String[] args)
		{
			ExactSolutionFinder esf=new ExactSolutionFinder(null);
			int i=0;
			do
			{
				++i;
				esf.print();
				esf.nextPerm();
			} while(esf.permGenerated);
			System.out.println("I: "+i);
		}
}