package cuboid.solvers;

import java.util.ArrayList;
import java.util.List;

import cuboid.base.Block;
import cuboid.base.BlockCollection;
import cuboid.base.Solution;

public class CloningAlgorithm implements SolutionFinder {
	/**
	 * Proportions limit. Recommended 10.
	 */
	final int limit;
	/**
	 * Limit of used block's clones per amount. Recommended 0.9.
	 */
	final double usedLimit;
	/**
	 * Limit of solid's length.
	 */
	int lengthLimit;
	final List<Proportion> proportions;
	int copiesNumber;

	class Proportion {
		BlockCollection blockCollection;
		int usedAmount;

		/**
		 * Constructs a new instance.
		 *
		 * @param blockCollection The blockCollection for this instance.
		 * @param usedAmount The usedAmount for this instance.
		 */
		public Proportion(BlockCollection blockCollection, int usedAmount)
		{
			this.blockCollection = blockCollection;
			this.usedAmount = usedAmount;
		}
		/**
		 * Gets the blockCollection for this instance.
		 *
		 * @return The blockCollection.
		 */
		public BlockCollection getBlockCollection()
		{
			return this.blockCollection;
		}
		public void increaseUsedAmount()
		{
			if(blockCollection.getAmount()>usedAmount)
				++usedAmount;
		}
		/**
		 * Gets the usedAmount for this instance.
		 *
		 * @return The usedAmount.
		 */
		public int getUsedAmount()
		{
			return this.usedAmount;
		}

		public double getQuotient(){
			return (double)usedAmount/(double)blockCollection.getAmount();
		}

		public int getClonedAmount(){
			return usedAmount*copiesNumber;
		}

		public double getUse(){
			return (double)getClonedAmount()/(double)getAmount();
		}

		public int getAmount(){
			return blockCollection.getAmount();
		}

		public int getExcess(){
			return getAmount()-getClonedAmount();
		}

		public void reduceToOne(){
			usedAmount = 1;
		}

		public String toString() {
			return "proportion[block="+blockCollection+",usedAmount="+usedAmount+"]";
		}
	}

	/**
	 * Constructs a new instance.
	 *
	 * @param limit The limit for this instance.
	 */
	public CloningAlgorithm(int limit,double usedLimit)
	{
		this.limit = limit;
		this.usedLimit = usedLimit;
		proportions = new ArrayList<Proportion>();
	}

	public void computeCopiesNumber(){
		copiesNumber = proportions.get(0).getAmount()/proportions.get(0).getUsedAmount();
		
		for(Proportion proportion:proportions){
			int n = proportion.getAmount()/proportion.getUsedAmount();
			if(n<copiesNumber)
				copiesNumber = n;
		}

	}

	void generateFirstProportions(List<BlockCollection> blockCollections){
		for(BlockCollection blockCollection : blockCollections)
			if(blockCollection.getAmount()>1)
				proportions.add(new Proportion(blockCollection,1));

		computeCopiesNumber();
	}

	void increaseProportions(){
		Proportion p = proportions.get(0);

		for(Proportion proportion:proportions)
			if(proportion.getExcess()>p.getExcess())
				p = proportion;

		p.increaseUsedAmount();
	}

	void reduceProportions(){
		if(proportions.isEmpty())
			return;
		Proportion p = proportions.get(0);

		for(Proportion proportion:proportions)
			if(proportion.getAmount()<p.getAmount())
				p = proportion;

		proportions.remove(p);

		for(Proportion proportion:proportions)
			proportion.reduceToOne();
	}

	int generatingMethod() {
		for(Proportion proportion:proportions)
			if(proportion.getUse()<usedLimit)
				return 2;
		return 1;
	}

	void generateNextProportions(){
		if(generatingMethod()==1)
			increaseProportions();
		else
			reduceProportions();
	}

	Solution cloneSolution(List<BlockCollection> blockCollections,Solution originSolution){
		if(originSolution == null)
			return null;

		System.out.println("1.originSolution = "+originSolution);
		originSolution.clone(blockCollections,copiesNumber,lengthLimit);
		System.out.println("2.originSolution = "+originSolution);

		System.out.println("3.usedBlockCollections:");
		for(BlockCollection bc:blockCollections)
			System.out.println(bc);
		System.out.println("3.usedBlockCollections: end");

		return originSolution;
	}

	Solution findSolution(List<BlockCollection> blockCollections,SolutionFinder algorithm){
		List<BlockCollection> usedBlockCollections = new ArrayList<BlockCollection>();

		for(Proportion proportion:proportions){
			usedBlockCollections.add(new BlockCollection(new Block(proportion.getBlockCollection().getBlock()),
						proportion.getUsedAmount()));
		}
		System.out.println("11.usedBlockCollections:");
		for(BlockCollection bc:usedBlockCollections)
			System.out.println(bc);
		System.out.println("11.usedBlockCollections: end");

		Solution solution = algorithm.solve(usedBlockCollections);
		System.out.println("solution = "+solution);

		System.out.println("2.usedBlockCollections:");
		for(BlockCollection bc:usedBlockCollections)
			System.out.println(bc);
		System.out.println("2.usedBlockCollections: end");

		return cloneSolution(blockCollections,solution);
	}

	public void findLengthLimit(List<BlockCollection> blockCollections){
		lengthLimit = 0;
//		System.out.println("find blockCols:");
		for(BlockCollection bc:blockCollections){
//			System.out.println(bc);
			int m = bc.getBlock().computeMaxLength();
//			System.out.println("m = "+m);
			if(m>lengthLimit)
				lengthLimit = m;
		}
//		System.out.println("find blockCols: end");
	}

	public Solution solve(List<BlockCollection> blockCollections) {
		findLengthLimit(blockCollections);
//		System.out.println("lenLim = "+lengthLimit);
		generateFirstProportions(blockCollections);

		if(proportions.isEmpty()) return null;

		Solution bestSolution = null; 
		SolutionFinder algorithm = new ExactSolutionFinder();
		int i = 1;
		do{
			System.out.println("Let's see generated proportions:");
			for(Proportion proportion:proportions)
				System.out.println("proportion: "+proportion);
			Solution solution = findSolution(blockCollections,algorithm);
			if(solution != null && (bestSolution==null ||
						(solution.compareTo(bestSolution)>0)))
				bestSolution = solution;
			System.out.println("best = "+bestSolution);

			if(++i<=limit)
				generateNextProportions();

		}while(i<=limit);

		return bestSolution;
	}

	public String toString()
	{
		return "CloningAlgorithm";
	}
}
