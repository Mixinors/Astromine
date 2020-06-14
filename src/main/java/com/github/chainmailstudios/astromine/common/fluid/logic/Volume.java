package com.github.chainmailstudios.astromine.common.fluid.logic;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.PropertyRegistry;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.List;

public class Volume {
	final List<Property> properties = Lists.newArrayList();
	final List<Condition> conditions = Lists.newArrayList();

	private Fluid fluid = Fluids.EMPTY;

	private Fraction fraction = Fraction.EMPTY;

	private Fraction size = Fraction.BUCKET;

	/**
	 * Instantiates a Volume with an empty fraction and fluid.
	 */
	public Volume() {
	}

	/**
	 * Instantiates a Volume with an empty fraction and specified fluid.
	 */
	public Volume(Fluid fluid) {
		this.fluid = fluid;
	}

	/**
	 * Instantiates a Volume with an specified fraction and fluid.
	 */
	public Volume(Fluid fluid, Fraction fraction) {
		this.fluid = fluid;
		this.fraction = fraction;
	}

	/**
	 * Deserializes a Volume from a tag.
	 *
	 * @return a Volume
	 */
	public static Volume fromTag(CompoundTag tag) {
		// TODO: Null checks.

		Volume volume = new Volume(Fluids.EMPTY);

		volume.fluid = Registry.FLUID.get(new Identifier(tag.getString("fluid")));
		volume.fraction = Fraction.fromTag(tag.getCompound("fraction"));

		for (String string : tag.getCompound("properties").getKeys()) {
			volume.properties.add(PropertyRegistry.INSTANCE.get(new Identifier(string)));
		}

		return volume;
	}

	/**
	 * Adds a property to this Volume.
	 */
	public void addProperty(Property property) {
		this.properties.add(property);
	}

	/**
	 * Removes a property from this Volume.
	 */
	public void removeProperty(Property property) {
		this.properties.remove(property);
	}

	/**
	 * Retrieves the properties from this Volume.
	 */
	public Collection<Property> getProperties() {
		return this.properties;
	}

	/**
	 * Adds a condition to this Volume.
	 */
	public void addCondition(Condition condition) {
		this.conditions.add(condition);
	}

	/**
	 * Removes a condition from this Volume.
	 */
	public void removeCondition(Condition condition) {
		this.conditions.remove(condition);
	}

	/**
	 * Retrieves the conditions from this Volume.
	 */
	public Collection<Condition> getConditions() {
		return this.conditions;
	}

	public boolean isFull() {
		return !this.isEmpty();
	}

	public boolean isEmpty() {
		return this.getFraction().equals(Fraction.EMPTY);
	}

	public Fraction getFraction() {
		return this.fraction;
	}

	public void setFraction(Fraction fraction) {
		this.fraction = fraction;
	}

	/**
	 * Serializes this Volume and its properties
	 * into a tag.
	 *
	 * @return a tag
	 */
	public CompoundTag toTag(CompoundTag tag) {
		// TODO: Null checks.

		tag.putString("fluid", Registry.FLUID.getId(this.fluid).toString());
		tag.put("fraction", this.fraction.toTag(new CompoundTag()));

		CompoundTag propertyTag = new CompoundTag();

		for (Property property : this.properties) {
			propertyTag.put(PropertyRegistry.INSTANCE.getId(property).toString(), property.toTag(this));
		}

		tag.put("properties", propertyTag);

		return tag;
	}

	/**
	 * Takes a Volume out of this Volume.
	 */
	public Volume take(Fraction pulled) {
		Volume volume = new Volume();
		Transaction transaction = this.pull(volume, pulled);
		transaction.commit();
		return volume;
	}

	/**
	 * Pull fluids from a Volume into this Volume.
	 * If the Volume's fractional available is smaller than
	 * pulled, ask for the minimum. If not, ask for the
	 * minimum between requested size and available
	 * for pulling into this.
	 *
	 * @return a Transaction representing the request
	 */
	public Transaction pull(Volume target, Fraction pulled) {
		Fraction available = Fraction.subtract(this.size, this.fraction);

		pulled = Fraction.min(pulled, available);

		Transaction transaction;

		if (target.fraction.isSmallerThan(pulled)) { // If target has less than required.
			transaction = new Transaction(target, this, target.fraction, target.fraction);
		} else { // If target has more than or equal to required.
			transaction = new Transaction(target, this, pulled, pulled);
		}

		if (this.conditions.stream().allMatch(condition -> condition.test(transaction))) {
			return transaction;
		} else {
			return Transaction.EMPTY;
		}
	}

	/**
	 * Adds to this Volume.
	 */
	public Volume give(Fraction pushed) {
		Volume volume = new Volume(this.fluid, pushed);
		Transaction transaction = volume.push(this, pushed);
		transaction.commit();
		return volume;
	}

	/**
	 * Push fluids from this Volume into a Volume.
	 * If the Volume's fractional available is smaller than
	 * pushed, ask for the minimum. If not, ask for the
	 * minimum between requested size and available for
	 * pushing into target.
	 *
	 * @return a Transaction representing the request
	 */
	public Transaction push(Volume target, Fraction pushed) {
		Fraction available = Fraction.subtract(target.size, target.fraction);

		pushed = Fraction.min(pushed, available);

		Transaction transaction;

		if (this.fraction.isSmallerThan(pushed)) {
			transaction = new Transaction(this, target, this.fraction, this.fraction);
		} else {
			transaction = new Transaction(this, target, pushed, pushed);
		}

		if (this.conditions.stream().allMatch(condition -> condition.test(transaction))) {
			return transaction;
		} else {
			return Transaction.EMPTY;
		}
	}

	public Fraction getSize() {
		return this.size;
	}

	public void setSize(Fraction size) {
		this.size = size;
	}

	public Fluid getFluid() {
		return this.fluid;
	}

	public void setFluid(Fluid fluid) {
		this.fluid = fluid;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.properties, this.fluid, this.fraction);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof Volume)) return false;

		Volume volume = (Volume) object;

		return Objects.equal(this.properties, volume.properties) && Objects.equal(this.fluid, volume.fluid) && Objects.equal(this.fraction, volume.fraction);
	}

	@Override
	public String toString() {
		return "Volume{" + "fluid=" + this.fluid + ", fraction=" + this.fraction + '}';
	}
}
