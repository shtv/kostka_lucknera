package cuboid.solvers;

import java.util.*;
import cuboid.base.*;


public class ExactSolutionFinder 
{
		private class ListElement
		{
			private final int n;
			private int oidx; //current orientation index
			private Block[] orientedBlock;
			private Orientation[] orientation;
			private Vector3D fit;
			
			public ListElement(int narg,Block block)
			{
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
			
		}
		
		private void checkSequence()
		{
		}
		
		private void nextFit()
		{
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