package org.example;

import java.util.Vector;

public class Brick {
	Vector<Cube> cubes;

	/**
	 * Constructs a new instance.
	 *
	 * @param cubes The cubes for this instance.
	 */
	public Brick(Vector<Cube> cubes)
	{
		this.cubes = cubes;
	}

	public int size(){
		return cubes.size();
	}

	public Cube getCube(int index){
		return cubes.get(index);
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof Brick)) return false;

		Brick other = (Brick) obj;
		if(other.size()!=size()) return false;

		int size = size();
		for(int i=0;i<size;++i)
			if(!other.getCube(i).equals(getCube(i)))
				return false;

		return true;
	}
}
