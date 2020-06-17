package com.github.chainmailstudios.astromine.common.volume;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.google.common.base.Objects;

import net.minecraft.nbt.CompoundTag;

public class BaseVolume {
	protected Fraction fraction = Fraction.EMPTY;

	protected Fraction size = Fraction.BUCKET;

	/**
	 * Instantiates a Volume with an empty fraction.
	 */
	public BaseVolume() {
	}

	/**
	 * Instantiates a Volume with a specified fraction.
	 */
	public BaseVolume(Fraction fraction) {
		this.fraction = fraction;
	}

	public boolean isFull() {
		return this.getFraction().equals(this.getSize());
	}

	public Fraction getFraction() {
		return this.fraction;
	}

	public void setFraction(Fraction fraction) {
		this.fraction = fraction;
	}

	public Fraction getSize() {
		return this.size;
	}

	public void setSize(Fraction size) {
		this.size = size;
	}

	public boolean isEmpty() {
		return this.getFraction().equals(Fraction.EMPTY);
	}

	/**
	 * Serializes this Volume and its properties into a tag.
	 *
	 * @return a tag
	 */
	public CompoundTag toTag(CompoundTag tag) {
		// TODO: Null checks.

		tag.put("fraction", this.fraction.toTag(new CompoundTag()));

		return tag;
	}

	/**
	 * Takes a Volume out of this Volume.
	 */
	public <T extends BaseVolume> T take(Fraction taken) {
		T volume = (T) new BaseVolume();
		this.push(volume, taken);
		return volume;
	}

	/**
	 * Push fluids from this Volume into a Volume. If the Volume's fractional available is smaller than pushed, ask for the minimum. If not, ask for the minimum between requested size and available for pushing into target.
	 */
	public void push(BaseVolume target, Fraction pushed) {
		Fraction available = Fraction.subtract(target.size, target.fraction);

		pushed = Fraction.min(pushed, available);

		if (this.fraction.isSmallerThan(pushed)) { // If target has less than required.
			this.setFraction(Fraction.subtract(this.fraction, this.fraction));
			target.setFraction(Fraction.add(target.getFraction(), this.fraction));

			target.setFraction(Fraction.simplify(target.getFraction()));
			this.setFraction(Fraction.simplify(this.getFraction()));
		} else { // If target has more than or equal to required.
			this.setFraction(Fraction.subtract(this.fraction, pushed));
			target.setFraction(Fraction.add(target.getFraction(), pushed));

			target.setFraction(Fraction.simplify(target.getFraction()));
			this.setFraction(Fraction.simplify(this.getFraction()));
		}
	}

	/**
	 * Adds to this Volume.
	 */
	public <T extends BaseVolume> T give(Fraction pushed) {
		return (T) new BaseVolume(Fraction.add(this.fraction, pushed));
	}

	/**
	 * Pull fluids from a Volume into this Volume. If the Volume's fractional available is smaller than pulled, ask for the minimum. If not, ask for the minimum between requested size and available for pulling into this.
	 */
	public void pull(BaseVolume target, Fraction pulled) {
		Fraction available = Fraction.subtract(this.size, this.fraction);

		pulled = Fraction.min(pulled, available);

		if (target.fraction.isSmallerThan(pulled)) { // If target has less than required.
			target.setFraction(Fraction.subtract(target.fraction, target.fraction));
			this.setFraction(Fraction.add(this.getFraction(), target.fraction));

			target.setFraction(Fraction.simplify(target.getFraction()));
			this.setFraction(Fraction.simplify(this.getFraction()));
		} else { // If target has more than or equal to required.
			target.setFraction(Fraction.subtract(target.fraction, pulled));
			this.setFraction(Fraction.add(this.getFraction(), pulled));

			target.setFraction(Fraction.simplify(target.getFraction()));
			this.setFraction(Fraction.simplify(this.getFraction()));
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.fraction);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof BaseVolume)) {
			return false;
		}

		BaseVolume volume = (BaseVolume) object;

		return Objects.equal(this.fraction, volume.fraction);
	}
}
