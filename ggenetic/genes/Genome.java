package ggenetic.genes;

import java.util.ArrayList;

public class Genome<G extends Gene<G>> {
	private final ArrayList<Chromosome<G>> chromosomes;

	public Genome(ArrayList<Chromosome<G>> chromosomes) {
		this.chromosomes = chromosomes;
	}

	public Genome(Genome<G> genome) {
		chromosomes = new ArrayList<Chromosome<G>>();
		for (Chromosome<G> chromosome : genome.chromosomes) {
			chromosomes.add(new Chromosome<G>(chromosome));
		}
	}

	/**
	 * copies the current genome and adds mutations with the given mutation rate
	 * and severity rate for the severity of the mutations.
	 * 
	 * @param mutationRate
	 *            how likely a gene is to mutate
	 * @param mutationSeverity
	 *            how severe the mutation is
	 * @return a copied genome with some mutations in it
	 */
	public Genome<G> copyWithMutation(double mutationRate, double mutationSeverity) {
		// copy the current genome
		Genome<G> copy = new Genome<G>(this);

		// mutate all of the chromosomes in the genome
		for (Chromosome<G> chromosome : copy.chromosomes) {
			chromosome.mutate(mutationRate, mutationSeverity);
		}

		// return the copy
		return copy;
	}

	public Genome<G> breedWithMutation(Genome<G> other, double mutationRate, double mutationSeverity) {
		// make sure they have the same number of chromosomes
		if (other.chromosomes.size() != chromosomes.size())
			throw new IllegalArgumentException("Two genomes need to have the same number of chromosomes to breed!");

		// make the array list for the results
		ArrayList<Chromosome<G>> results = new ArrayList<>();

		// loop through each of the chromosomes, breed and add to the results
		for (int i = 0; i < chromosomes.size(); i++) {
			results.add(chromosomes.get(i).breed(other.chromosomes.get(i), mutationRate, mutationSeverity));
		}

		// return a new genome with the given chromosomes
		return new Genome<>(results);
	}

	/**
	 * gets the chromosome at the given index of the genome
	 * 
	 * @param i
	 *            index to look at
	 * @return chromosome at that index
	 */
	public Chromosome<G> getChromosome(int i) {
		return chromosomes.get(i);
	}

	public String toString() {
		String results = "";

		for (int i = 0; i < chromosomes.size(); i++) {
			results += chromosomes.get(i).toString();
			results += "\n";
		}

		return results.substring(0, results.length() - 1);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chromosomes == null) ? 0 : chromosomes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Genome<G> other = (Genome<G>) obj;
		if (chromosomes == null) {
			if (other.chromosomes != null)
				return false;
		} else if (!chromosomes.equals(other.chromosomes))
			return false;
		return true;
	}
}
