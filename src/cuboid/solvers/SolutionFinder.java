package cuboid.solvers;

import java.util.Set;

import cuboid.base.Block;
import cuboid.base.Solution;

public interface SolutionFinder {

	Solution getSolution(Set<Block> blocks);

}
