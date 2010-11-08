package cuboid.solvers;

import java.util.List;
import java.util.ListIterator;

import cuboid.base.BlockCollection;
import cuboid.base.Solution;
import cuboid.solvers.ExactSolutionFinder.ListElement;

public class AproximationAlgorithm extends ExactSolutionFinder {

	@Override
	protected void checkPerm() {
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
				nextFit(q.previousIndex(),currentElem);
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
}
