package cuboid.solvers;

import java.util.List;

import cuboid.base.BlockCollection;
import cuboid.base.Solution;

public interface SolutionFinder {

	Solution solve(List<BlockCollection> blockCollections);

}
