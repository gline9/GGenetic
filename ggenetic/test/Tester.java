package ggenetic.test;

import java.util.Arrays;

import ggenetic.creature.Population;

public class Tester {
	public static void main(String[] args) {
		Population<SortCreature, SwapGene> p = new Population<>();

		p.populate(1000, new SortCreature());

		p.setBreedingPopulation();
		p.setMutationRate(.1);
		p.setMutationSeverity(-7);

		for (int i = 0; i < 2000; i++) {
			System.out.println("Generation: " + i);
			p.computeFitnessValues(Evaluator::evaluate);
			System.out.println(p.getBestCreature());
			System.out.println("fitness: " + -p.getBestFitness());
			if (i == 1999)
				System.out.println(Arrays.toString(Evaluator.useAlgorithm(p.getBestCreature())));
			p.killAndRepopulate(500);
		}
	}
}
