package ggenetic.test;

import ggenetic.creature.Creature;
import ggenetic.genes.Genome;

public class SortCreature extends Creature<SortCreature, SwapGene> {

	public SortCreature() {
		super(new SwapGene(0, 0, 0), 1, 30);
	}

	public SortCreature(Genome<SwapGene> genome) {
		super(genome);
	}

	@Override
	public SortCreature asexuallyReproduce(double mutationRate, double mutationSeverity) {
		SortCreature results = new SortCreature(genes.copyWithMutation(mutationRate, mutationSeverity));
		return results;
	}

	@Override
	public SortCreature breed(SortCreature other, double mutationRate, double mutationSeverity) {
		SortCreature results = new SortCreature(genes.breedWithMutation(other.genes, mutationRate, mutationSeverity));
		return results;
	}

	@Override
	public SortCreature randomize() {
		return new SortCreature();
	}

	public Genome<SwapGene> getGenome() {
		return genes;
	}

}
