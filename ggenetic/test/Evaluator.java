package ggenetic.test;

import java.util.Arrays;
import java.util.Random;

import ggenetic.genes.Chromosome;

public class Evaluator {
	private final static int[] tester = new int[] { 10, 1, 9, 2, 6, 3, 7, 4, 8, 5};
	private final static int[] sorted = new int[] { 18, 87, 380, 433, 604, 633, 686, 789, 812, 990 };

	public static int[] useAlgorithm(SortCreature c) {
		int[] copy = Arrays.copyOf(tester, 10);

		Chromosome<SwapGene> chrome = c.getGenome().getChromosome(0);

		int index = 0;

		boolean negated = false;

		// go through all of the genes in the creature
		for (int i = 0; i < 30; i++) {
			// check if the index should be looped back
			if (index >= copy.length - 1 || index < 0)
				index = 0;

			// get the two numbers to compare
			int a = copy[index];
			int b = copy[index + 1];

			// get the next command by the genes from the creature
			int command = chrome.getGene(i).execute(negated, a, b);

			// execute the command
			negated = false;
			switch (command) {
			case SwapGene.Nothing:
				break;
			case SwapGene.Swap:
				copy[index] = b;
				copy[index + 1] = a;
				break;
			case SwapGene.NegateNext:
				negated = true;
				break;
			default:
				// this is for the goto option
				index = command;
			}
		}

		return copy;
	}

	public static double getNegativeScore(int[] copy) {
		// score is negative as for minimization of fitness
		double distance = 0;
		for (int i = 0; i < copy.length - 1; i++) {
			distance += -Math.exp(-Math.pow((copy[i] - copy[i + 1]), 2));
		}
		return distance;
	}

	public static int[] randomArray(int size) {
		Random r = new Random();
		int[] results = new int[size];
		for (int i = 0; i < size; i++) {
			results[i] = r.nextInt(40);
		}
		return results;
	}

	public static double evaluate(SortCreature c) {
		return getNegativeScore(useAlgorithm(c));
	}
}
