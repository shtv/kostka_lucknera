package cuboid.solvers;

import java.util.ArrayList;
import java.util.List;

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

		originSolution.clone(blockCollections,copiesNumber,lengthLimit);

		return originSolution;
	}

	Solution findSolution(List<BlockCollection> blockCollections,SolutionFinder algorithm){
		List<BlockCollection> usedBlockCollections = new ArrayList<BlockCollection>();

		for(Proportion proportion:proportions){
			usedBlockCollections.add(new BlockCollection(proportion.getBlockCollection().getBlock(),
						proportion.getUsedAmount()));
		}

		Solution solution = algorithm.solve(usedBlockCollections);

		return cloneSolution(blockCollections,solution);
	}

	public void findLengthLimit(List<BlockCollection> blockCollections){
		lengthLimit = 0;
		for(BlockCollection bc:blockCollections){
			int m = bc.getBlock().computeMaxLength();
			if(m>lengthLimit)
				lengthLimit = m;
		}
	}

	public Solution solve(List<BlockCollection> blockCollections) {
		findLengthLimit(blockCollections);
		generateFirstProportions(blockCollections);

		if(proportions.isEmpty()) return null;

		Solution bestSolution = null; 
		SolutionFinder algorithm = new ExactSolutionFinder();
		int i = 1;
		do{
			Solution solution = findSolution(blockCollections,algorithm);
			if(solution != null && (bestSolution==null ||
						(solution.compareTo(bestSolution)>0)))
				bestSolution = solution;

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
