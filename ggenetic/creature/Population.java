package ggenetic.creature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import gcore.tuples.Pair;
import ggenetic.genes.Gene;

public class Population<C extends Creature<C, G>, G extends Gene<G>> {
	private ArrayList<C> creatures = new ArrayList<>();

	private ArrayList<Pair<C, Double>> fitness = new ArrayList<>();

	private boolean isBreedingPopulation = false;

	private double mutationRate = 0;

	private double mutationSeverity = 0;

	public Population() {}

	public void setBreedingPopulation() {
		isBreedingPopulation = true;
	}

	public void setAsexualPopulation() {
		isBreedingPopulation = false;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	public void setMutationSeverity(double mutationSeverity) {
		this.mutationSeverity = mutationSeverity;
	}

	public void populate(int populationSize, C seed) {
		// add the required number of creatures to the creatures array list
		for (int i = 0; i < populationSize; i++) {
			creatures.add(seed.randomize());
		}
	}

	public void computeFitnessValues(Function<C, Double> fitnessFunction) {
		// reset the fitness array list
		fitness = new ArrayList<>();

		// compute the fitness values for all of the creatures
		for (C creature : creatures) {
			fitness.add(new Pair<>(creature, fitnessFunction.apply(creature)));
		}
	}

	public void killAndRepopulate(int numberToRemove) {

		// make a random number generator
		Random r = new Random();

		// make sure number to remove is less than the population size and
		// greater than zero
		if (numberToRemove < 0 || numberToRemove >= creatures.size() - 1)
			throw new IllegalArgumentException("Invalid number of creatures to remove (" + numberToRemove
					+ ") for population of size " + creatures.size());

		// sort the fitness list
		Collections.sort(fitness, (a, b) -> a.getSecond().compareTo(b.getSecond()));

		// take the bottom "numberToRemove" and remove from the population
		for (int i = creatures.size() - numberToRemove; i < fitness.size(); i++) {
			creatures.remove(fitness.get(i).getFirst());
		}

		int reproducingPopulation = creatures.size();

		// check if the population is a breeding or asexual population
		if (isBreedingPopulation) {
			for (int i = 0; i < numberToRemove; i++) {
				// randomly select two creatures and breed them
				int first, second;
				do {
					first = r.nextInt(reproducingPopulation);
					second = r.nextInt(reproducingPopulation);
				} while (first == second);

				C newCreature = creatures.get(first).breed(creatures.get(second), mutationRate, mutationSeverity);

				creatures.add(newCreature);
			}
		} else {
			for (int i = 0; i < numberToRemove; i++) {
				creatures.add(creatures.get(r.nextInt(reproducingPopulation)).asexuallyReproduce(mutationRate,
						mutationSeverity));
			}
		}
	}

	/**
	 * makes a competition of creatures, whoever loses the competition dies, and
	 * whoever wins lives on, after all of the competitions have happened the
	 * remaining creatures breed into the rest of the population pool using the
	 * specified reproduction method
	 * 
	 * @param numberOfCompetitions
	 *            how many competitions will be performed
	 * @param competeFunction
	 *            function that will take in two creatures and return if
	 *            creature 0 or creature 1 won the competition
	 * @since Apr 17, 2016
	 */
	public void competeAndRepopulate(int numberOfCompetitions, BiFunction<C, C, Integer> competeFunction) {
		Random r = new Random();

		// go through and choose creatures at random and remove
		for (int i = 0; i < numberOfCompetitions; i++) {
			int index1, index2;
			do {
				index1 = r.nextInt(creatures.size());
				index2 = r.nextInt(creatures.size());
			} while (index1 == index2);

			C creature1 = creatures.get(index1);
			C creature2 = creatures.get(index2);

			int winner = competeFunction.apply(creature1, creature2);

			switch (winner) {
			case 0:
				creatures.remove(index2);
				break;
			case 1:
				creatures.remove(index1);
				break;
			default:
				throw new RuntimeException(
						"Invalid index for winner of creature tournament: " + winner + " accepted values are 0 or 1!");
			}
		}

		// re-populate the creatures
		int reproducingPopulation = creatures.size();
		if (isBreedingPopulation) {
			for (int i = 0; i < numberOfCompetitions; i++) {
				// randomly select two creatures and breed them
				int first, second;
				do {
					first = r.nextInt(reproducingPopulation);
					second = r.nextInt(reproducingPopulation);
				} while (first == second);

				C newCreature = creatures.get(first).breed(creatures.get(second), mutationRate, mutationSeverity);

				creatures.add(newCreature);
			}
		} else {
			for (int i = 0; i < numberOfCompetitions; i++) {
				creatures.add(creatures.get(r.nextInt(reproducingPopulation)).asexuallyReproduce(mutationRate,
						mutationSeverity));
			}
		}
	}

	/**
	 * creates a competition where the creatures compete against each other
	 * using the compete function, the winner of the tournament is returned at
	 * the end of the competition
	 * 
	 * @param competeFunction
	 *            function that will take in two creatures and return if
	 *            creature 0 or creature 1 won the competition
	 * @return the creature that won the competition
	 * @since Apr 17, 2016
	 */
	public C getBestCreatureByCompetition(BiFunction<C, C, Integer> competeFunction) {
		Random r = new Random();

		// take the initial population of creatures and do consecutive
		// tournaments to determine the best creature
		ArrayList<C> startingPopulation = new ArrayList<>(creatures);
		ArrayList<C> winningPopulation = new ArrayList<>();

		// each loop of this is a new tournament
		while (startingPopulation.size() > 1) {

			// each loop of this is a new battle
			while (startingPopulation.size() >= 2) {
				// take two random creatures to battle
				int first, second;
				do {
					first = r.nextInt(startingPopulation.size());
					second = r.nextInt(startingPopulation.size());
				} while (first == second);

				// get the creatures
				C creature1 = startingPopulation.get(first);
				C creature2 = startingPopulation.get(second);

				// get the winner
				C winner = competeFunction.apply(creature1, creature2) == 0 ? creature1 : creature2;

				// add the winner to the winning population
				winningPopulation.add(winner);

				// remove both creatures from the starting population
				startingPopulation.remove(creature1);
				startingPopulation.remove(creature2);
			}

			// check to see if there is just one left over
			if (startingPopulation.size() == 1)
				winningPopulation.add(startingPopulation.get(0));

			// set the starting population to be the same as the winning
			// population and reset the winning population
			startingPopulation = winningPopulation;
			winningPopulation = new ArrayList<C>();
		}

		// return the winner which is the same as the only creature left
		return startingPopulation.get(0);
	}

	public C getBestCreature() {
		return fitness.get(0).getFirst();
	}

	public double getBestFitness() {
		return fitness.get(0).getSecond();
	}
}
