package ggenetic.genes;

public abstract class Gene<G extends Gene<G>> {

	/**
	 * should make a copy of the current gene
	 * 
	 * @return a copy of the current gene
	 */
	public abstract G copy();

	/**
	 * mutates the current gene with the given mutation rate, the rate will be
	 * from 0 to 1 uniform to determine if it is a rare mutation or a common
	 * mutation
	 * 
	 * @param mutationSeverity
	 *            how severe the mutation is
	 */
	public abstract void mutate(double mutationSeverity);

	/**
	 * randomizes the current gene
	 */
	public abstract void randomize();

	public abstract boolean equals(Object obj);

	public abstract int hashCode();
}
