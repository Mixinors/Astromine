package com.github.chainmailstudios.astromine.common.fluid.logic;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;

import java.util.Stack;

/**
 * A class representing a fractional fluid
 * transaction between two volumes.
 * <p>
 * Only one transaction may be active at a time.
 * If a transaction is not completed, reverted
 * or cancelled, all further transactions will
 * be locked.
 * <p>
 * A transaction will alter the state of its
 * source and target upon completion.
 * `
 * If the resulting data is invalid,
 * the transaction may be reverted,
 * restoring the original state.
 */
public class Transaction {
	private final Volume target;
	private final Volume source;

	private final Fraction targetRollback;
	private final Fraction sourceRollback;

	private final Fraction targetOperator;
	private final Fraction sourceOperator;

	private boolean finished = false;

	private Stack<Transaction> stack = new Stack<>();

	public static final Transaction EMPTY = new Transaction(null, null, null, null);

	/**
	 * Instantiates a Transaction based on two Volumes and two Fractions.
	 */
	public Transaction(Volume target, Volume source, Fraction targetOperator, Fraction sourceOperator) {
		this.target = target;
		this.source = source;

		this.targetRollback = target == null ? Fraction.EMPTY : target.getFraction();
		this.sourceRollback = source == null ? Fraction.EMPTY : source.getFraction();

		this.targetOperator = targetOperator;
		this.sourceOperator = sourceOperator;
	}

	public Transaction child(Volume target, Volume source, Fraction targetOperator, Fraction sourceOperator) {
		Transaction child = new Transaction(target, source, targetOperator, sourceOperator);
		stack.add(child);
		child.stack = stack;
		return child;
	}

	/**
	 * Applies a Transaction to two Volumes, altering them.
	 */
	public void commit() {
		if (this == EMPTY) return;

		if (finished) {
			throw new UnsupportedOperationException("Attempting to reuse finished Transaction!");
		}

		target.setFraction(Fraction.subtract(target.getFraction(), targetOperator));
		source.setFraction(Fraction.add(source.getFraction(), sourceOperator));

		target.setFraction(Fraction.simplify(target.getFraction()));
		source.setFraction(Fraction.simplify(source.getFraction()));

		finished = true;
	}

	/**
	 * Inversely applies a Transaction to two Volumes, altering them.
	 */
	public void abort() {
		if (this == EMPTY) return;

		target.setFraction(targetRollback);
		source.setFraction(sourceRollback);

		finished = false;
	}

	@Override
	public String toString() {
		return "Transaction{" +
				"volumeA=" + target +
				", volumeB=" + source +
				", fractionA=" + targetOperator +
				", fractionB=" + sourceOperator +
				", finished=" + finished +
				'}';
	}
}
