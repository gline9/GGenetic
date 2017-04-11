package ggenetic.genes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

public class Chromosome<G extends Gene<G>> {

	private ArrayList<G> genes;

	@SafeVarargs
	public Chromosome(G... genes) {
		this.genes = new ArrayList<>(Arrays.asList(genes));
	}

	public Chromosome(ArrayList<G> genes) {
		this.genes = genes;
	}

	public Chromosome(Chromosome<G> chromosome) {
		genes = new ArrayList<>();
		for (G gene : chromosome.genes) {
			genes.add(gene.copy());
		}
	}

	public Chromosome(G seedGene, int genes) {
		this.genes = new ArrayList<>();
		for (int i = 0; i < genes; i++) {
			seedGene.randomize();
			this.genes.add(seedGene.copy());
		}
	}

	/**
	 * gets the gene at the specified position
	 * 
	 * @param i
	 *            index to look at
	 * @return gene at that index
	 */
	public G getGene(int i) {
		return genes.get(i);
	}

	/**
	 * mutates the current chromosome with the given rate and severity of the
	 * mutation, a higher rate indicates that more genes will be mutated, and a
	 * higher severity indicates that the genes will be mutated more severely
	 * when there are mutated the mutation rate should be from 0 to 1, but
	 * severity can be any real number.
	 * 
	 * @param rate
	 *            percent chance of mutation for a given gene
	 * @param severity
	 *            how severe the mutations will be on average given the gene is
	 *            mutated
	 */
	public void mutate(double rate, double severity) {
		// make a new random number generator
		Random r = new Random();

		// make the percent point function that converts severity, this will map
		// anything given to the pdf of y/(E^y - 1))*E^(x*y), which is a proper
		// pdf from 0 to 1 with increasing probability in the 1 range with
		// increasing severity, and increasing probability in the 0 range with
		// decreasing severity
		Function<Double, Double> ppf = d -> Math.log(1 - d + Math.exp(severity) * d) / severity;

		// check if severity is 0, in that case use the limit of the function as
		// it is discontinuous.
		if (severity == 0)
			ppf = d -> d;

		// loop through each of the genes and determine if it should be mutated
		for (G g : genes) {

			// determine if it is mutated
			if (r.nextDouble() < rate) {
				// if so mutate with a randomly generated mutation rate
				double m = r.nextDouble();
				double s = ppf.apply(m);
				
				g.mutate(s);

			}

		}
	}

	/**
	 * breeds the current chromosome with the chromosome given, the genes will
	 * come from each parent with 50% probability, and the final chromosome will
	 * be mutated with the given rates.
	 * 
	 * @param other
	 *            chromosome to breed with
	 * @param mutationRate
	 *            rate of mutation
	 * @param mutationSeverity
	 *            severity rate for the mutation
	 * @return the resulting breed chromosome
	 */
	@SuppressWarnings("unchecked")
	public Chromosome<G> breed(Chromosome<G> other, double mutationRate, double mutationSeverity) {
		// new random number generator
		Random r = new Random();

		// the resulting chromosome
		Chromosome<G> results = null;
		// check that they have the same number of genes
		if (genes.size() == other.genes.size()) {

			// stores the results
			ArrayList<G> resultsGenes = new ArrayList<>();

			// if they do just loop through and add each with 50% probability
			// from both sides.
			for (int i = 0; i < genes.size(); i++) {
				// check if it should come from parent 1
				if (r.nextDouble() < .5) {
					resultsGenes.add(getGene(i).copy());
				} else {
					resultsGenes.add(other.getGene(i).copy());
				}
			}

			// create the chromosome from the resulting genes
			results = new Chromosome<G>(resultsGenes);

		} else {

			// stores the results
			ArrayList<G> resultsGenes = new ArrayList<>();

			// while inside both of the sizes for both chromosomes add to the
			// resultsGenes
			for (int i = 0; i < genes.size() && i < other.genes.size(); i++) {

				// check if it should come from parent 1
				if (r.nextDouble() < .5) {
					resultsGenes.add(getGene(i).copy());
				} else {
					resultsGenes.add(other.getGene(i).copy());
				}
			}

			// add the remaining genes from the larger parent until either
			// chance runs out or they have all been added
			Chromosome<G> larger = null;
			if (genes.size() > other.genes.size()) {
				larger = this;
			} else {
				larger = other;
			}

			while (resultsGenes.size() < larger.genes.size() && r.nextDouble() < .5) {
				resultsGenes.add(larger.getGene(resultsGenes.size()).copy());
			}

			// put the results genes into the results chromosome
			results = new Chromosome<G>(resultsGenes.toArray((G[]) new Object[0]));

		}

		// after the results have been obtained mutate the chromosome with the
		// given rates
		results.mutate(mutationRate, mutationSeverity);

		return results;
	}

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
		Chromosome<G> other = (Chromosome<G>) obj;
		if (genes == null) {
			if (other.genes != null)
				return false;
		} else if (!genes.equals(other.genes))
			return false;
		return true;
	}

}
