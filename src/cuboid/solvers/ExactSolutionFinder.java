package cuboid.solvers;

import java.util.*;
import cuboid.base.*;


public class ExactSolutionFinder  implements SolutionFinder
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
			
			public Block getOrientedBlock()
			{
				return orientedBlock[oidx];
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
	
		private final int MAX_LENGTH;
		private Solution bestSolution;
		private List<ListElement> blocks;
		private boolean permGenerated; //udalo sie wygenerowac kolejna premutacje
	
		public ExactSolutionFinder(List<Block> argblocks)
		{
				int counter=0;
				int maxlength=Integer.MIN_VALUE;
				permGenerated=false;
				blocks=new LinkedList<ListElement>();
				for(Iterator<Block> it=argblocks.iterator();it.hasNext();)
				{
					Block cb=it.next();
					if(cb.getMaxX()>maxlength)
						maxlength=cb.getMaxX();
					if(cb.getMaxY()>maxlength)
						maxlength=cb.getMaxY();
					if(cb.getMaxZ()>maxlength)
						maxlength=cb.getMaxZ();
					blocks.add(new ListElement(++counter,cb));
				}
				MAX_LENGTH=maxlength;
		}
		
		/**
		 * Constructs a new instance.
		 */
		public ExactSolutionFinder()
		{
			MAX_LENGTH = 1000; // TODO, do poprawy
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
				boolean properBlock=false;
				int minz=Integer.MAX_VALUE, maxz=Integer.MIN_VALUE;
				int miny=Integer.MAX_VALUE, maxy=Integer.MIN_VALUE;
				int minx=Integer.MAX_VALUE, maxx=Integer.MIN_VALUE;
				int sum=0;
				int volume=0;
				ListIterator<ListElement> q=blocks.listIterator();
				while(q.previousIndex()<idx)
				{
					ListElement le=q.next();
					if(minz>le.getFit().getZ())
						minz=le.getFit().getZ();
					if(miny>le.getFit().getY())
						miny=le.getFit().getY();
					if(minx>le.getFit().getX())
						minx=le.getFit().getX();
					if(maxz<le.getFit().getZ()+le.getOrientedBlock().getMaxZ())
						maxz=le.getFit().getZ()+le.getOrientedBlock().getMaxZ();
					if(maxy<le.getFit().getY()+le.getOrientedBlock().getMaxY())
						maxy=le.getFit().getY()+le.getOrientedBlock().getMaxY();
					if(maxx<le.getFit().getX()+le.getOrientedBlock().getMaxX())
						maxx=le.getFit().getX()+le.getOrientedBlock().getMaxX();
					sum+=le.getOrientedBlock().getCuboidsCount();
				}
				if((maxx-minx)<MAX_LENGTH && (maxy-miny)<MAX_LENGTH && (maxz-minz)<MAX_LENGTH)
					properBlock=true;
				volume=(maxx-minx)*(maxy-miny)*(maxz-minz);
				if(volume==sum)
					saveSequence(idx);
				return properBlock;
		}
		
		private void saveSequence(int idx)
		{
			
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

		public Solution getSolution(java.util.List<BlockCollection> blockCollections) {
			return null;
		}
}
