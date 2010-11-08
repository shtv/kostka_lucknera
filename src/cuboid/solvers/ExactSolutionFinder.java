package cuboid.solvers;

import java.util.*;
import cuboid.base.*;


public class ExactSolutionFinder implements SolutionFinder
{
		protected class ListElement
		{
			private final static int orientationCount=24;
			private final int n;
			private int oidx; //current orientation index
			private boolean oidxReset;
			private Block[] orientedBlock;
			private Orientation[] orientation={new Orientation(0,0,0),
											   new Orientation(0,90,0),
											   new Orientation(0,180,0),
											   new Orientation(0,270,0),
											   new Orientation(90,0,0),
											   new Orientation(90,0,90),
											   new Orientation(90,0,180),
											   new Orientation(90,0,270),
											   new Orientation(180,0,0),
											   new Orientation(180,90,0),
											   new Orientation(180,180,0),
											   new Orientation(180,270,0),
											   new Orientation(270,0,0),
											   new Orientation(270,0,90),
											   new Orientation(270,0,180),
											   new Orientation(270,0,270),
											   new Orientation(0,0,90),
											   new Orientation(0,90,90),
											   new Orientation(0,180,90),
											   new Orientation(0,270,90),
											   new Orientation(0,0,270),
											   new Orientation(0,90,270),
											   new Orientation(0,180,270),
											   new Orientation(0,270,270)};
			private Vector3D fit;
			private boolean generateFirstFit;
			
			public ListElement(int narg,Block block)
			{
				oidx=0;
				oidxReset=false;
				generateFirstFit=true;
				n=narg;
				orientedBlock=new Block[orientation.length];
				for(int i=0;i<orientation.length;i++)
				{
					orientedBlock[i]=new Block(block);
					orientedBlock[i].orient(orientation[i]);
				}
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
	
		protected int MAX_LENGTH;
		protected Solution bestSolution;
		protected List<ListElement> blocks;
		protected boolean permGenerated; //udalo sie wygenerowac kolejna premutacje
	
		public ExactSolutionFinder()
		{
		}
		
		protected void nextPerm()
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

		protected void checkPerm()
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
					nextFit(q.previousIndex(), currentElem);
				}
				if(checkSequence(q.previousIndex()))
				{
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
		protected boolean checkSequence(int idx)
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
					if(minz>le.getFit().getZ()+le.getOrientedBlock().getMinZ())
						minz=le.getFit().getZ();
					if(miny>le.getFit().getY()+le.getOrientedBlock().getMinY())
						miny=le.getFit().getY();
					if(minx>le.getFit().getX()+le.getOrientedBlock().getMinX())
						minx=le.getFit().getX();
					if(maxz<le.getFit().getZ()+le.getOrientedBlock().getMaxZ())
						maxz=le.getFit().getZ()+le.getOrientedBlock().getMaxZ();
					if(maxy<le.getFit().getY()+le.getOrientedBlock().getMaxY())
						maxy=le.getFit().getY()+le.getOrientedBlock().getMaxY();
					if(maxx<le.getFit().getX()+le.getOrientedBlock().getMaxX())
						maxx=le.getFit().getX()+le.getOrientedBlock().getMaxX();
					sum+=le.getOrientedBlock().getCubesCount();
				}
				if((maxx-minx)<MAX_LENGTH && (maxy-miny)<MAX_LENGTH && (maxz-minz)<MAX_LENGTH)
					properBlock=true;
				volume=(maxx-minx)*(maxy-miny)*(maxz-minz);
				if(volume==sum)
					saveSequence(idx);
				return !properBlock;
		}
		
		protected void saveSequence(int idx)
		{
			
		}
		
		/**
		 *  Zwraca czy udalo sie wygenerowac kolejne dopasowanie
		 */
		protected boolean nextFit(int idx, ListElement elem)
		{
			return false;
		}
		
		public Solution getSolution(List<BlockCollection> lbc)
		{
			int counter=0;
			int maxlength=Integer.MIN_VALUE;
			permGenerated=false;
			blocks=new LinkedList<ListElement>();
			for(Iterator<BlockCollection> it=lbc.iterator();it.hasNext();)
			{
				BlockCollection bc=it.next();
				for(int i=0;i<bc.getAmount();i++)
				{
					Block cb=bc.getBlock();
					if(cb.getMaxX()>maxlength)
						maxlength=cb.getMaxX();
					if(cb.getMaxY()>maxlength)
						maxlength=cb.getMaxY();
					if(cb.getMaxZ()>maxlength)
						maxlength=cb.getMaxZ();
					blocks.add(new ListElement(counter,cb));
				}
				counter++;
			}
			MAX_LENGTH=maxlength;
			do
			{
				checkPerm();
				nextPerm();
			}while(permGenerated);
			return bestSolution;
		}
		
		public static void main(String[] args)
		{
		}
}
