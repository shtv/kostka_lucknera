package cuboid.solvers;

import java.util.ArrayList;
import java.util.List;

import cuboid.base.BlockCollection;
import cuboid.base.Solution;

public class CloningAlgorithm implements SolutionFinder {
	final int limit;
	final List<Proportion> proportions;

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
		public void increaseUsedAmount(int usedAmount)
		{
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
	}

	/**
	 * Constructs a new instance.
	 *
	 * @param limit The limit for this instance.
	 */
	public CloningAlgorithm(int limit)
	{
		this.limit = limit;
		proportions = new ArrayList<Proportion>();
	}

	void generateFirstProportions(List<BlockCollection> blockCollections){
		for(BlockCollection blockCollection : blockCollections)
			if(blockCollection.getAmount()>1)
				proportions.add(new Proportion(blockCollection,1));
	}

	void generateNextProportions(){
	}

	Solution findSolution(SolutionFinder algorithm){

		return null;
	}

	public Solution getSolution(List<BlockCollection> blockCollections) {
		generateFirstProportions(blockCollections);

		if(proportions.isEmpty()) return null;

		Solution bestSolution = null; 
		SolutionFinder algorithm = new ExactSolutionFinder();
		int i = 1;
		do{
			Solution solution = findSolution(algorithm);
			if(solution != null && (bestSolution==null ||
						(solution.compareTo(bestSolution)>0)))
				bestSolution = solution;

			if(++i<=limit)
				generateNextProportions();

		}while(i<=limit);

		return bestSolution;
	}
}
