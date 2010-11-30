package cuboid.solvers;

import java.sql.NClob;
import java.util.*;

import javax.swing.event.ListSelectionEvent;
import javax.swing.text.html.MinimalHTMLWriter;

import cuboid.base.*;


public class ExactSolutionFinder implements SolutionFinder
{	
		protected class NextFitHelpClass 
		{
			public Block buildBlock;
			public Block block;
			public Vector3D fit;
			public Vector3D w;
			public int currentDirection;
			public int minI, maxI;
			public int minJ, maxJ;
			public int[][] bp;
			public int[][] bbp;
			
			public NextFitHelpClass clone()
			{
				NextFitHelpClass result=new NextFitHelpClass();
				
				if(buildBlock==null) result.buildBlock=null;
				else result.buildBlock=new Block(this.buildBlock);
				
				if(this.block==null) result.block=null;
				else result.block = new Block(this.block);
				
				if(this.fit==null) result.fit=null;
				else result.fit=this.fit.clone();
				
				if(this.w==null) result.w=null;
				else result.w = this.w.clone();
				
				result.currentDirection=this.currentDirection;
				result.minI=this.minI;
				result.maxI=this.maxI;
				result.minJ=this.minJ;
				result.maxJ=this.maxJ;
				
				if(this.bp==null) result.bp=null;
				else result.bp=this.bp.clone();
				
				if(this.bbp==null) result.bbp=null;
				else result.bbp=this.bbp.clone();
				return result;
			}
		}
	
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
			
			private boolean compareMatrices(int[][][] m1,int[][][] m2)
			{
				if(m1==null && m2==null) return false;
				
				if(!(m1.length==m2.length && m1[0].length==m2[0].length && m1[0][0].length==m2[0][0].length)) return false;
				
				for(int i=0;i<m1.length;i++)
					for(int j=0;j<m1[i].length;j++)
						for(int k=0;k<m1[i][j].length;k++)
							if(m1[i][j][k]!=m2[i][j][k])
									return false;
				return true;
			}
			
			private boolean compareOrientedBlocks(Block b1, Block b2)
			{
				int[][][] mb1 = b1.getMatrix();
				int[][][] mb2 = b2.getMatrix();
				if(compareMatrices(mb1,mb2))
					return true;
				return false;
			}
			
			
			public ListElement(int narg,Block block)
			{
				oidx=0;
				oidxReset=false;
				setGenerateFirstFit(true);
				n=narg;
				orientedBlock=new Block[orientation.length];
				for(int i=0;i<orientation.length;i++)
				{
					orientedBlock[i]=new Block(block);
					orientedBlock[i].orient(orientation[i]);
					for(int j=i-1;j>=0;j--)
					{
						if(orientedBlock[j]==null) continue;
						if(compareOrientedBlocks(orientedBlock[i],orientedBlock[j]))
						{
							orientedBlock[i]=null;
							break;
						}
					}
				}
			}
			
			public Block getBaseBlock()
			{
				return orientedBlock[0];
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
				setGenerateFirstFit(false);
				fit=v;
			}
			
			public void resetFit()
			{
				setGenerateFirstFit(true);
			}
			
			public void incOidx()
			{
				oidxReset=false;
				do
				{
					oidx++;
				}while(oidx<orientedBlock.length && orientedBlock[oidx]==null);
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

			public void setGenerateFirstFit(boolean generateFirstFit) {
				this.generateFirstFit = generateFirstFit;
			}

			public boolean isGenerateFirstFit() {
				return generateFirstFit;
			}
		}
	 
		protected int MAX_LENGTH;
		protected Solution bestSolution;
		protected List<ListElement> blocks;
		protected NextFitHelpClass[] nfhc;
		protected boolean permGenerated; //udalo sie wygenerowac kolejna premutacje
		protected boolean maxSolutionFound;
	
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
			ListIterator<ListElement> q;
			q=blocks.listIterator();
			
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
					if(maxSolutionFound)
						break;
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
//				System.out.print("SEQUENCE: ");
//				for(int i=0;i<=idx;i++)
//				{
//					ListElement le=blocks.get(i);
//					System.out.print("[V: ("+le.getFit().getX()+","+le.getFit().getY()+","+le.getFit().getZ()+")");
//					System.out.print("O: "+le.getOrientation().getAngleX()+" "+le.getOrientation().getAngleY()+" "+le.getOrientation().getAngleZ()+"]");
//				}
//				System.out.println();
//				
				boolean properBlock=false;
//				int minz=Integer.MAX_VALUE, maxz=Integer.MIN_VALUE;
//				int miny=Integer.MAX_VALUE, maxy=Integer.MIN_VALUE;
//				int minx=Integer.MAX_VALUE, maxx=Integer.MIN_VALUE;
				int sum=0;
				int volume=0;
//				ListIterator<ListElement> q=blocks.listIterator();
//				while(q.previousIndex()<idx)
//				{
//					ListElement le=q.next();
//					if(minz>le.getFit().getZ()+le.getOrientedBlock().getMinZ())
//						minz=le.getFit().getZ()+le.getOrientedBlock().getMinZ();
//					if(miny>le.getFit().getY()+le.getOrientedBlock().getMinY())
//						miny=le.getFit().getY()+le.getOrientedBlock().getMinY();
//					if(minx>le.getFit().getX()+le.getOrientedBlock().getMinX())
//						minx=le.getFit().getX()+le.getOrientedBlock().getMinX();
//					if(maxz<le.getFit().getZ()+le.getOrientedBlock().getMaxZ())
//						maxz=le.getFit().getZ()+le.getOrientedBlock().getMaxZ();
//					if(maxy<le.getFit().getY()+le.getOrientedBlock().getMaxY())
//						maxy=le.getFit().getY()+le.getOrientedBlock().getMaxY();
//					if(maxx<le.getFit().getX()+le.getOrientedBlock().getMaxX())
//						maxx=le.getFit().getX()+le.getOrientedBlock().getMaxX();
//					sum+=le.getOrientedBlock().getCubesCount();
//				}
//				System.out.println("COUNTED (DX,DY,DZ): "+(maxx-minx+1)+","+(maxy-miny+1)+","+(maxz-minz+1)+")");
//				System.out.println("REMEMBERED (DX,DY,DZ): "+(nfhc[idx].buildBlock.getMaxX()-nfhc[idx].buildBlock.getMinX()+1)+"," +
//									(nfhc[idx].buildBlock.getMaxY()-nfhc[idx].buildBlock.getMinY()+1)+"," +
//											(nfhc[idx].buildBlock.getMaxZ()-nfhc[idx].buildBlock.getMinZ()+1)+")");
//				if((maxx-minx+1)<=MAX_LENGTH && (maxy-miny+1)<=MAX_LENGTH && (maxz-minz+1)<=MAX_LENGTH)
//					properBlock=true;
//				volume=(maxx-minx+1)*(maxy-miny+1)*(maxz-minz+1);
//				if(properBlock && volume==sum)
//					saveSequence(idx);
				int dx=nfhc[idx].buildBlock.getMaxX()-nfhc[idx].buildBlock.getMinX()+1;
				int dy=nfhc[idx].buildBlock.getMaxY()-nfhc[idx].buildBlock.getMinY()+1;
				int dz=nfhc[idx].buildBlock.getMaxZ()-nfhc[idx].buildBlock.getMinZ()+1;
				if(dx<=MAX_LENGTH && dy<=MAX_LENGTH && dz<=MAX_LENGTH)
					properBlock=true;
				volume=dx*dy*dz;
				if(properBlock && volume==nfhc[idx].buildBlock.getCubesCount())
					saveSequence(idx);
				return !properBlock;
		}
		
		protected void saveSequence(int idx)
		{
			/*
			 * Sprawdza czy objetosc sekwencji, ktora ma byc zapisana jest wieksza od 
			 * objetosci bestSolution. 
			 * Jesli tak to nastepuje zapisanie.
			 */
//			System.out.println("SAVE SEQUENCE");
			Solution cs=new Solution();
			ListIterator<ListElement> it=blocks.listIterator();
			while(it.hasNext() && (!it.hasPrevious()?true:it.previousIndex()<idx))
			{
				ListElement le=it.next();
				cs.addNextMove(new Move(le.getN(),new Block(le.getBaseBlock()),
						le.getFit().clone(),
						le.getOrientation().clone(),le.oidx));
			}
			if(bestSolution==null || bestSolution.getVolume()<cs.getVolume())
			{
				bestSolution=cs;
				if(bestSolution.getVolume()>=MAX_LENGTH*MAX_LENGTH*MAX_LENGTH || bestSolution.getSequence().size()==blocks.size()) 
					maxSolutionFound=true;
			}
			return;
		}
		
		/**
		 * 
		 * @param d 0-XY 1-XZ 2-YZ
		 * @param p
		 * @param b
		 */
		protected int[][] getBlockProjection(int d, Block b)
		{
			int[][] br=null;
			if(d==0)
			{
				br=new int[b.getMaxY()-b.getMinY()+1][b.getMaxX()-b.getMinX()+1];
				for(int i=0;i<br.length;i++)
					for(int j=0;j<br[i].length;j++)
						br[i][j]=Integer.MIN_VALUE;
				Iterator<Cube> it = b.getCubes().iterator();
				while(it.hasNext())
				{
					Cube c=it.next();
					int hz = b.getMaxZ()-c.getZ()+1;
					int cy=b.getMaxY()-c.getY();
					int cx=c.getX()-b.getMinX();
					if(hz>br[cy][cx])
						br[cy][cx]=hz;
				}
			}
			else if (d==1)
			{
				br=new int[b.getMaxZ()-b.getMinZ()+1][b.getMaxX()-b.getMinX()+1];
				for(int i=0;i<br.length;i++)
					for(int j=0;j<br[i].length;j++)
						br[i][j]=Integer.MIN_VALUE;
				Iterator<Cube> it = b.getCubes().iterator();
				while(it.hasNext())
				{
					Cube c=it.next();
					int hz = b.getMaxY()-c.getY()+1;
					int cz=b.getMaxZ()-c.getZ();
					int cx=c.getX()-b.getMinX();
					if(hz>br[cz][cx])
						br[cz][cx]=hz;
				}
			} 
			else if (d==2)
			{
				br=new int[b.getMaxZ()-b.getMinZ()+1][b.getMaxY()-b.getMinY()+1];
				for(int i=0;i<br.length;i++)
					for(int j=0;j<br[i].length;j++)
						br[i][j]=Integer.MIN_VALUE;
				Iterator<Cube> it = b.getCubes().iterator();
				while(it.hasNext())
				{
					Cube c=it.next();
					int hz = b.getMaxX()-c.getX()+1;
					int cz=b.getMaxZ()-c.getZ();
					int cy=c.getY()-b.getMinY();
					if(hz>br[cz][cy])
						br[cz][cy]=hz;
				}
			}
			return br;
		}
		
		/**
		 * 
		 * @param d 0-XY 1-XZ 2-YZ
		 * @param proj
		 * @param bb
		 */
		public int[][] getBaseBlockProjection(int d,Block bb)
		{
			int[][] proj=null;
			if(d==0)
			{
				proj=new int[bb.getMaxY()-bb.getMinY()+1][bb.getMaxX()-bb.getMinX()+1];
				for(int i=0;i<proj.length;i++)
					for(int j=0;j<proj[i].length;j++)
						proj[i][j]=Integer.MIN_VALUE;
				Iterator<Cube> it=bb.getCubes().iterator();
				while(it.hasNext())
				{
					Cube cc=it.next();
					int hz=cc.getZ()-bb.getMinZ()+1;
					int cy=bb.getMaxY()-cc.getY();
					int cx=cc.getX()-bb.getMinX();
					if(proj[cy][cx]<hz)
						proj[cy][cx]=hz;
				}
			}
			else if(d==1)
			{
				proj=new int[bb.getMaxZ()-bb.getMinZ()+1][bb.getMaxX()-bb.getMinX()+1];
				for(int i=0;i<proj.length;i++)
					for(int j=0;j<proj[i].length;j++)
						proj[i][j]=Integer.MIN_VALUE;
				Iterator<Cube> it=bb.getCubes().iterator();
				while(it.hasNext())
				{
					Cube cc=it.next();
					int hz=cc.getY()-bb.getMinY()+1;
					int cz=bb.getMaxZ()-cc.getZ();
					int cx=cc.getX()-bb.getMinX();
					if(proj[cz][cx]<hz)
						proj[cz][cx]=hz;
				}
			}
			else if(d==2)
			{
				proj=new int[bb.getMaxZ()-bb.getMinZ()+1][bb.getMaxY()-bb.getMinY()+1];
				for(int i=0;i<proj.length;i++)
					for(int j=0;j<proj[i].length;j++)
						proj[i][j]=Integer.MIN_VALUE;
				Iterator<Cube> it=bb.getCubes().iterator();
				while(it.hasNext())
				{
					Cube cc=it.next();
					int hz=cc.getX()-bb.getMinX()+1;
					int cz=bb.getMaxZ()-cc.getZ();
					int cy=cc.getY()-bb.getMinY();
					if(proj[cz][cy]<hz)
						proj[cz][cy]=hz;
				}
			}
			return proj;
				
		}
		
		
		/**
		 *  Zwraca czy udalo sie wygenerowac kolejne dopasowanie
		 */
		protected boolean nextFit(int idx, ListElement elem)
		{
			if(idx+1<nfhc.length && nfhc[idx+1]!=null)
				nfhc[idx+1]=null;
			if(nfhc[idx]==null)
				nfhc[idx]=new NextFitHelpClass();
			else if(elem.isGenerateFirstFit())
			{
				nfhc[idx].fit=null;
				nfhc[idx].bbp=null;
				nfhc[idx].bp=null;
				nfhc[idx].currentDirection=0;
				nfhc[idx].w=null;
			}
			
			if(idx==0)
			{
				if(nfhc[idx].fit==null)
				{
					nfhc[idx].buildBlock=new Block(elem.getOrientedBlock());
					nfhc[idx].fit=new Vector3D(0,0,0);
					elem.setFit(nfhc[idx].fit);
					return true;
				}
				nfhc[idx].fit=null;
				elem.setFit(nfhc[idx].fit);
				nfhc[idx].buildBlock=null;
				return false;
			}
			
			
			NextFitHelpClass cnfhc=nfhc[idx];
			cnfhc.block=elem.getOrientedBlock();
			cnfhc.buildBlock=new Block(nfhc[idx-1].buildBlock);
			do{
				if(cnfhc.bp==null) cnfhc.bp=getBlockProjection(cnfhc.currentDirection, cnfhc.block);
				if(cnfhc.bbp==null) cnfhc.bbp=getBaseBlockProjection(cnfhc.currentDirection, cnfhc.buildBlock);
				cnfhc.minJ=1-cnfhc.bp[0].length;
				cnfhc.maxJ=cnfhc.bbp[0].length-1;
				cnfhc.minI=1-cnfhc.bp.length;
				cnfhc.maxI=cnfhc.bbp.length-1;
				
				findWVector(cnfhc.bp, cnfhc.bbp, cnfhc);
				if(cnfhc.w==null)
				{ 
					cnfhc.currentDirection++;
					cnfhc.bp=null;
					cnfhc.bbp=null;
				}
			}while(cnfhc.w==null && cnfhc.currentDirection<3);
			if(cnfhc.currentDirection>=3)
			{
				elem.setFit(null);
				cnfhc.fit=null;
				cnfhc.currentDirection=0;
				return false;
			}
//			System.out.println("W: "+"("+cnfhc.w.getX()+","+cnfhc.w.getY()+","+cnfhc.w.getZ()+")");
//			System.out.println("D: "+cnfhc.currentDirection);
//			if(cnfhc.w.getX()==0 &&  cnfhc.w.getY()==0 && cnfhc.w.getZ()==4)
//				System.out.println();
			switch (cnfhc.currentDirection) {
			case 0: //XY
				cnfhc.fit=new Vector3D(cnfhc.w.getX()+cnfhc.buildBlock.getMinX()-cnfhc.block.getMinX(),
									-cnfhc.w.getY()+cnfhc.buildBlock.getMaxY()-cnfhc.block.getMaxY(),
									cnfhc.w.getZ()-cnfhc.buildBlock.getMinZ()-cnfhc.block.getMaxZ()-1);
				break;
			case 1: //XZ
				cnfhc.fit=new Vector3D(cnfhc.w.getX()+cnfhc.buildBlock.getMinX()-cnfhc.block.getMinX(),
						cnfhc.w.getZ()-cnfhc.buildBlock.getMinY()-cnfhc.block.getMaxY()-1,
						-cnfhc.w.getY()+cnfhc.buildBlock.getMaxZ()-cnfhc.block.getMaxZ());
				break;
			case 2: //YZ
				cnfhc.fit=new Vector3D(cnfhc.w.getZ()-cnfhc.buildBlock.getMinX()-cnfhc.block.getMaxX()-1,
						cnfhc.w.getX()+cnfhc.buildBlock.getMinY()-cnfhc.block.getMinY(),
						-cnfhc.w.getY()+cnfhc.buildBlock.getMaxZ()-cnfhc.block.getMaxZ());					
				break;
			default:
				break;	
			}
			elem.setFit(cnfhc.fit);
			cnfhc.buildBlock.addBlock(cnfhc.fit, cnfhc.block);
			return true;
		}
		
		public void findWVector(int[][] bp,int[][] bbp, NextFitHelpClass cnfhc)
		{
			   if(cnfhc.w==null)
				   cnfhc.w=new Vector3D(cnfhc.minJ-1,cnfhc.minI,0);
				   //cnfhc.w=new Vector3D(cnfhc.minJ,cnfhc.minI-1,0);
			   do
			   {
				  cnfhc.w.setX(cnfhc.w.getX()+1);
				   if(cnfhc.w.getX()>cnfhc.maxJ)
				   {
						cnfhc.w.setY(cnfhc.w.getY()+1);
						cnfhc.w.setX(cnfhc.minJ);
						if(cnfhc.w.getY()>cnfhc.maxI)
						{
							cnfhc.w=null;
							return;
						}
				   }
				   //
				   if(find3rdCoord(cnfhc.w,bp,bbp))
				   		break;
			   }while(cnfhc.w.getZ()>MAX_LENGTH);
				   //
			   //}while(!find3rdCoord(cnfhc.w,bp,bbp)); 	spr czy w.z < MAXLENGTH
		}
		
		protected boolean find3rdCoord(Vector3D w,int[][] bp,int[][] bbp)
		{
			int max=Integer.MIN_VALUE;
			boolean correct=false;
			for(int i=0;i<bp.length;i++)
				for(int j=0;j<bp[i].length;j++)
				{
					if(bp[i][j]<0) continue;
					boolean correctCoords= (i+w.getY()<bbp.length) && (i+w.getY()>=0)
										&& (j+w.getX()<bbp[0].length) && (j+w.getX()>=0);
					int bbpv;
					if(correctCoords)
						bbpv=bbp[i+w.getY()][j+w.getX()];
					else
						bbpv=Integer.MIN_VALUE;
					if(bbpv>=0)
					{
						correct=true;
						if(max<bp[i][j]+bbpv)
							max=bp[i][j]+bbpv;
					}
				}
			if(!correct)
				return false;
			w.setZ(max);
			return true;
		}

		
		public Solution solve(List<BlockCollection> lbc)
		{			
			int counter=0;
			int maxlength=Integer.MIN_VALUE;
			permGenerated=false;
			maxSolutionFound=false;
			blocks=new LinkedList<ListElement>();
			for(Iterator<BlockCollection> it=lbc.iterator();it.hasNext();)
			{
				BlockCollection bc=it.next();
				for(int i=0;i<bc.getAmount();i++)
				{
					Block cb=bc.getBlock();
					if((cb.getMaxX()-cb.getMinX()+1)>maxlength)
						maxlength=cb.getMaxX()-cb.getMinX()+1;
					if((cb.getMaxY()-cb.getMinY()+1)>maxlength)
						maxlength=cb.getMaxY()-cb.getMinY()+1;
					if((cb.getMaxZ()-cb.getMinZ()+1)>maxlength)
						maxlength=cb.getMaxZ()-cb.getMinZ()+1;
					blocks.add(new ListElement(counter,new Block(cb)));
				}
				counter++;
			}
			nfhc=new NextFitHelpClass[blocks.size()];
			MAX_LENGTH=maxlength;
			do
			{
//				//
//				Collections.swap(blocks, 1, 3);
//				//blocks.remove(blocks.size()-1);
//				//
				System.out.print("PERM: ");
				for(int i=0;i<blocks.size();i++)
					System.out.print(blocks.get(i).getN()+" ");
				System.out.println();
				checkPerm();
				if(maxSolutionFound)
					break;
				nextPerm();
				for(int i=0;i<nfhc.length;i++)
					nfhc[i]=null;
			}while(permGenerated);
//			System.out.println("VOLUME: "+bestSolution.getVolume());
			return bestSolution;
		}
		
		public String toString()
		{
			return "ExactSolutionFinder";
		}
}
