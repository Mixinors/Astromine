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
    public static final Transaction EMPTY = new Transaction(null, null, null, null);
    private final Volume target;
    private final Volume source;
    private final Fraction targetRollback;
    private final Fraction sourceRollback;
    private final Fraction targetOperator;
    private final Fraction sourceOperator;
    private boolean finished = false;
    private Stack<Transaction> stack = new Stack<>();

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
        this.stack.add(child);
        child.stack = this.stack;
        return child;
    }

    /**
     * Applies a Transaction to two Volumes, altering them.
     */
    public void commit() {
        if (this == EMPTY) return;

        if (this.finished) {
            throw new UnsupportedOperationException("Attempting to reuse finished Transaction!");
        }

        this.target.setFraction(Fraction.subtract(this.target.getFraction(), this.targetOperator));
        this.source.setFraction(Fraction.add(this.source.getFraction(), this.sourceOperator));

        this.target.setFraction(Fraction.simplify(this.target.getFraction()));
        this.source.setFraction(Fraction.simplify(this.source.getFraction()));

        this.finished = true;
    }

    /**
     * Inversely applies a Transaction to two Volumes, altering them.
     */
    public void abort() {
        if (this == EMPTY) return;

        this.target.setFraction(this.targetRollback);
        this.source.setFraction(this.sourceRollback);

        this.finished = false;
    }

    @Override
    public String toString() {
        return "Transaction{" + "volumeA=" + this.target + ", volumeB=" + this.source + ", fractionA=" + this.targetOperator + ", fractionB=" + this.sourceOperator + ", finished=" + this.finished + '}';
    }
}
