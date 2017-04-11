package ggenetic.test;

import java.util.Random;

import ggenetic.genes.Gene;

public class SwapGene extends Gene<SwapGene> {

	public static final int False = -3;
	public static final int LessOrEqual = -2;
	public static final int Less = -1;
	public static final int Equal = 0;
	public static final int Greater = 1;
	public static final int GreaterOrEqual = 2;
	public static final int True = 3;

	public static final int Nothing = 0;
	public static final int Swap = -1;
	public static final int NegateNext = -2;
	public static final int GOTO = -3;

	private int bool;
	private int command;
	private int gotoP;

	protected SwapGene(int bool, int command, int gotoP) {
		this.bool = bool;
		this.command = command;
		this.gotoP = gotoP;
	}

	@Override
	public SwapGene copy() {
		return new SwapGene(bool, command, gotoP);
	}

	@Override
	public void mutate(double mutationSeverity) {
		Random r = new Random();

		int pick = r.nextInt(3);

		if (pick == 0) {
			if (mutationSeverity < .3)
				bool *= -1;
			else
				bool = r.nextInt(7) - 3;
		} else if (pick == 1) {
			command = r.nextInt(4) - 3;
		} else {
			gotoP += (r.nextInt(2) == 0 ? -1 : 1) * mutationSeverity * 100;
			gotoP = Math.floorMod(gotoP, 100);
		}

		// check for a strong mutation
		if (mutationSeverity > .8)
			randomize();
	}

	@Override
	public void randomize() {
		Random r = new Random();
		bool = r.nextInt(7) - 3;
		command = r.nextInt(4) - 3;
		gotoP = r.nextInt(100);
	}

	public int execute(boolean negated, int a, int b) {
		// check if the condition is met
		boolean results = false;
		switch (bool) {
		case False:
			results = false;
			break;
		case LessOrEqual:
			results = a <= b;
			break;
		case Less:
			results = a < b;
			break;
		case Equal:
			results = a == b;
			break;
		case Greater:
			results = a > b;
			break;
		case GreaterOrEqual:
			results = a >= b;
			break;
		case True:
			results = true;
			break;
		}

		// if negate swap results
		if (negated)
			results = !results;

		// if the results is true do the command
		if (results) {
			if (command == GOTO)
				return gotoP;
			else
				return command;
		}

		// otherwise return do nothing
		return Nothing;
	}

	public String toString() {
		String results = "";
		switch (bool) {
		case False:
			results += "false";
			break;
		case LessOrEqual:
			results += "<=";
			break;
		case Less:
			results += "<";
			break;
		case Equal:
			results += "=";
			break;
		case Greater:
			results += ">";
			break;
		case GreaterOrEqual:
			results += ">=";
			break;
		case True:
			results += "true";
		}

		switch (command) {
		case Nothing:
			results += " null";
			break;
		case Swap:
			results += " swap";
			break;
		case NegateNext:
			results += " !";
			break;
		case GOTO:
			results += " goto";
		}

		if (command == GOTO)
			results += " " + gotoP;

		return results;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bool;
		result = prime * result + command;
		result = prime * result + gotoP;
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
		SwapGene other = (SwapGene) obj;
		if (bool != other.bool)
			return false;
		if (command != other.command)
			return false;
		if (gotoP != other.gotoP)
			return false;
		return true;
	}

}
