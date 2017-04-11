package ggenetic.creature;

import java.util.ArrayList;
import java.util.Arrays;

import ggenetic.genes.Chromosome;
import ggenetic.genes.Gene;
import ggenetic.genes.Genome;

public abstract class Creature<C extends Creature<C, G>, G extends Gene<G>> {

	protected Genome<G> genes;

	/**
	 * creates a creature with the given genome
	 * 
	 * @param genes
	 *            genome for creature
	 */
	public Creature(Genome<G> genes) {
		this.genes = genes;
	}

	/**
	 * copy constructor
	 * 
	 * @param creature
	 *            creature to copy
	 */
	public Creature(Creature<C, G> creature) {
		genes = new Genome<G>(creature.genes);
	}

	/**
	 * creates a creature with a randomly generated genome with the given number
	 * of chromosomes and genes in each chromosome. For the genes in each
	 * chromosome give either 1 number which says how many are in all of the
	 * chromosomes, or give 1 number for each chromosome to specify how many in
	 * each
	 * 
	 * @param seedGene
	 *            gene so other genes can be created
	 * @param chromosomes
	 *            how many chromosomes the creature will have
	 * @param genesInChromosomes
	 *            how many genes are in each chromosome
	 */
	public Creature(G seedGene, int chromosomes, int... genesInChromosomes) {
		// check if invalild number of arguments were given for genes in
		// chromosomes
		if (genesInChromosomes.length != 1 && genesInChromosomes.length != chromosomes)
			throw new IllegalArgumentException(
					"you must provide a correct argument for how many genes are in 'each' or in 'every' chromosome!");

		// if length is 1 make it a new array filled with the value
		if (genesInChromosomes.length == 1) {
			int numOfGenes = genesInChromosomes[0];
			genesInChromosomes = new int[chromosomes];
			Arrays.fill(genesInChromosomes, numOfGenes);
		}

		// fill each chromosome with the appropriate number of genes
		ArrayList<Chromosome<G>> chromes = new ArrayList<>();
		for (int i = 0; i < chromosomes; i++) {
			chromes.add(new Chromosome<G>(seedGene, genesInChromosomes[i]));
		}

		// set the genes of the creature
		genes = new Genome<G>(chromes);
	}

	/**
	 * reproduces asexually with the given mutation rate and mutation severity.
	 * 
	 * @param mutationRate
	 *            rate for mutations
	 * @param mutationSeverity
	 *            how severe the mutations will be
	 * @return the results of the reproduction
	 */
	public abstract C asexuallyReproduce(double mutationRate, double mutationSeverity);

	/**
	 * reproduces sexually with another creature and the given mutation rate and
	 * mutation severity.
	 * 
	 * @param other
	 *            creature to breed with
	 * @param mutationRate
	 *            rate for mutations
	 * @param mutationSeverity
	 *            how severe the mutations will be
	 * @return the results of the reproduction
	 */
	public abstract C breed(C other, double mutationRate, double mutationSeverity);

	/**
	 * creates a random creature with the statistics of the current creature
	 * 
	 * @return a random creature
	 */
	public abstract C randomize();

	public String toString() {
		return genes.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((genes == null) ? 0 : genes.hashCode());
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
		C other = (C) obj;
		if (genes == null) {
			if (other.genes != null)
				return false;
		} else if (!genes.equals(other.genes))
			return false;
		return true;
	}
}
