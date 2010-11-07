package cuboid.solvers;

import java.util.ArrayList;
import java.util.List;

import cuboid.base.BlockCollection;
import cuboid.base.Solution;

public class CloningAlgorithm implements SolutionFinder {

	public Solution getSolution(List<BlockCollection> blockCollections) {
		List<BlockCollection> blocks = new
			ArrayList<BlockCollection>();

		for(BlockCollection blockCollection : blockCollections)
			if(blockCollection.getAmount()>1)
				blocks.add(blockCollection);

		if(blocks.isEmpty()) return null;

		return null;
	};
}
