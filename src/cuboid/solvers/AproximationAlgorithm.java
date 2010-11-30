package cuboid.solvers;

import java.sql.NClob;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import cuboid.base.Block;
import cuboid.base.BlockCollection;
import cuboid.base.Solution;
import cuboid.base.Vector3D;
import cuboid.solvers.ExactSolutionFinder.ListElement;
import cuboid.solvers.ExactSolutionFinder.NextFitHelpClass;

public class AproximationAlgorithm extends ExactSolutionFinder {

	private final int C;
	
	private class ChoosenFits
	{
		private int[] oidx;
		private Vector3D[] fit;
		private float[] score;
		private NextFitHelpClass[] nfhc;
		
		private int currentFit;
		
		public ChoosenFits()
		{
			this.oidx=new int[C];
			this.fit=new Vector3D[C];
			this.score=new float[C];
			this.nfhc=new NextFitHelpClass[C];
			for(int i=0;i<this.score.length;i++)
				this.score[i]=-1;
			this.currentFit=-1;
		}
		
		public float getScore(int i)
		{
			return score[i];
		}
		
		public void setScore(int i,float v)
		{
			score[i]=v;
		}
		
		public int getOidx(int i)
		{
			return oidx[i];
		}
		
		public Vector3D getFit(int i)
		{
			return fit[i];
		}
		
		public void setOidx(int i,int v)
		{
			oidx[i]=v;
		}
		
		public void setFit(int i,Vector3D v)
		{
			fit[i]=v;
		}
		
		public int getCurrentOidx()
		{
			return oidx[currentFit]; 
		}
		
		public Vector3D getCurrentFit()
		{
			return fit[currentFit];
		}
		
		public NextFitHelpClass getCurrentNfhc()
		{
			if(this.nfhc[currentFit]==null)
				System.out.println("WTF");
			return this.nfhc[currentFit];
		}
		
 		public boolean nextFit()
		{
			currentFit++;
			return currentFit<C && nfhc[currentFit]!=null && fit[currentFit]!=null;
		}
		

		public void setNfhc(int i, NextFitHelpClass nfhc) {
			this.nfhc[i] = nfhc;
		}

		public NextFitHelpClass getNfhc(int i) {
			return nfhc[i];
		}
	}
	
	private List<ChoosenFits> choosenFits;
	
	public AproximationAlgorithm(int C)
	{
		super();
		this.C=C;
		choosenFits=new LinkedList<ChoosenFits>();
	}
	
	private float fitnessScore(int idx)
	{
		float score=0;
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
		volume=(maxx-minx+1)*(maxy-miny+1)*(maxz-minz+1);
		if(properBlock)
			score=(float)sum/(float)volume;
		return score;
	}
	
	private void chooseFits()
	{
		int idx=choosenFits.size();
		ListElement ce=blocks.get(idx);
		ChoosenFits cf=new ChoosenFits();
		choosenFits.add(cf);
		ce.setOidx(0);
		while(!ce.isOidxReset())
		{
			ce.resetFit();
			while(nextFit(idx, ce))
			{
				float score=fitnessScore(idx);
				for(int i=0;i<C;i++)
					if(score>cf.getScore(i))
					{
						cf.setFit(i, ce.getFit().clone());
						cf.setOidx(i, ce.getOidx());
						cf.setScore(i,score);
						cf.setNfhc(i,nfhc[idx].clone());
						break;
					}
			}
			ce.incOidx();
		}
	}
	
	private void removeLastChoosenFit()
	{
		choosenFits.remove(choosenFits.size()-1);
	}
	
	private boolean nextChoosenFit()
	{
		int idx=choosenFits.size()-1;
		ChoosenFits cf=choosenFits.get(idx);
		boolean result=cf.nextFit();
		if(result)
		{
			blocks.get(idx).setFit(cf.getCurrentFit());
			blocks.get(idx).setOidx(cf.getCurrentOidx());
			nfhc[idx]=cf.getCurrentNfhc();
		}
		return result;
	}
	
	@Override
	protected void checkPerm() {
		ListIterator<ListElement> q;
		q=blocks.listIterator(); 
		
		while(true)
		{
			q.next();
			if(choosenFits.size()<=q.previousIndex())
				chooseFits();
			if(!nextChoosenFit())
			{
					q.previous();
					if(q.hasPrevious())
					{
						q.previous();
						removeLastChoosenFit();
					}
					else
						break;
					continue;
			}
			if(checkSequence(q.previousIndex()))
			{
				if(maxSolutionFound)
					break;
				q.previous();
				continue;
			}
			if(q.hasNext())
				continue;
			else
			{
				while(nextChoosenFit())
					checkSequence(q.previousIndex());
				q.previous();
				if(q.hasPrevious())
				{
					q.previous();
					removeLastChoosenFit();
				}
				else 
					break;
				continue;
			}		
		}
		choosenFits=new LinkedList<ChoosenFits>();
	}
	
	public String toString()
	{
		return "AproximationAlgorithm";
	}
}
