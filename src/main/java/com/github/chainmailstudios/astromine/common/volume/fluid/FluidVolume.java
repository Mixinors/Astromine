package com.github.chainmailstudios.astromine.common.volume.fluid;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import com.github.chainmailstudios.astromine.registry.PropertyRegistry;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.List;

public class FluidVolume extends BaseVolume {
	public static final int TYPE = 0;

	final List<FluidProperty> properties = Lists.newArrayList();
	final List<FluidCondition> fluidConditions = Lists.newArrayList();

	private Fluid fluid = Fluids.EMPTY;

	public static final FluidVolume EMPTY = new FluidVolume();

	/**
	 * Instantiates a Volume with an empty fraction and fluid.
	 */
	public FluidVolume() {
	}

	/**
	 * Instantiates a Volume with an empty fraction and specified fluid.
	 */
	public FluidVolume(Fluid fluid) {
		this.fluid = fluid;
	}

	/**
	 * Instantiates a Volume with an specified fraction and fluid.
	 */
	public FluidVolume(Fluid fluid, Fraction fraction) {
		this.fluid = fluid;
		this.fraction = fraction;
	}

	/**
	 * Deserializes a Volume from a tag.
	 *
	 * @return a Volume
	 */
	public static FluidVolume fromTag(CompoundTag tag) {
		// TODO: Null checks.

		FluidVolume fluidVolume = new FluidVolume(Fluids.EMPTY);

		if (!tag.contains("fluid")) {
			fluidVolume.fluid = AstromineFluids.OXYGEN;
		} else {
			fluidVolume.fluid = Registry.FLUID.get(new Identifier(tag.getString("fluid")));
		}

		if (!tag.contains("fraction")) {
			fluidVolume.fraction = Fraction.EMPTY;
		} else {
			fluidVolume.fraction = Fraction.fromTag(tag.getCompound("fraction"));
		}

		for (String string : tag.getCompound("properties").getKeys()) {
			fluidVolume.properties.add(PropertyRegistry.INSTANCE.get(new Identifier(string)));
		}

		return fluidVolume;
	}

	/**
	 * Adds a property to this Volume.
	 */
	public void addProperty(FluidProperty fluidProperty) {
		this.properties.add(fluidProperty);
	}

	/**
	 * Removes a property from this Volume.
	 */
	public void removeProperty(FluidProperty fluidProperty) {
		this.properties.remove(fluidProperty);
	}

	/**
	 * Retrieves the properties from this Volume.
	 */
	public Collection<FluidProperty> getProperties() {
		return this.properties;
	}

	/**
	 * Adds a condition to this Volume.
	 */
	public void addCondition(FluidCondition fluidCondition) {
		this.fluidConditions.add(fluidCondition);
	}

	/**
	 * Removes a condition from this Volume.
	 */
	public void removeCondition(FluidCondition fluidCondition) {
		this.fluidConditions.remove(fluidCondition);
	}

	/**
	 * Retrieves the conditions from this Volume.
	 */
	public Collection<FluidCondition> getFluidConditions() {
		return this.fluidConditions;
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

		for (FluidProperty fluidProperty : this.properties) {
			propertyTag.put(PropertyRegistry.INSTANCE.getId(fluidProperty).toString(), fluidProperty.toTag(this));
		}

		tag.put("properties", propertyTag);

		return tag;
	}

	public Fluid getFluid() {
		return this.fluid;
	}

	public void setFluid(Fluid fluid) {
		this.fluid = fluid;
	}

	public String toInterfaceString() {
		return new TranslatableText(fluid.getBucketItem().getTranslationKey()).getString().replace(" Bucket", "") + " | " + fraction.getNumerator() + "/" + fraction.getDenominator();
	}

	@Override
	public <T extends BaseVolume> T take(Fraction taken) {
		return (T) new FluidVolume(fluid, super.take(taken).getFraction());
	}

	@Override
	public <T extends BaseVolume> T give(Fraction pushed) {
		return (T) new FluidVolume(fluid, super.give(pushed).getFraction());
	}

	public void pull(FluidVolume target, Fraction pulled) {
		if (fluidConditions.stream().anyMatch(condition -> !condition.test(this, target))) return;
		else super.pull(target, pulled);
	}

	public void push(FluidVolume target, Fraction pushed) {
		if (fluidConditions.stream().anyMatch(condition -> !condition.test(this, target))) return;
		else super.push(target, pushed);
	}

	@Override
	public boolean isFull() {
		return getFraction().equals(getSize()) && this.fluid != Fluids.EMPTY;
	}

	@Override
	public boolean isEmpty() {
		return this.getFraction().equals(Fraction.EMPTY) || this.fluid == Fluids.EMPTY;
	}

	@Deprecated
	public void pull(BaseVolume target, Fraction pulled) {
		// Deprecated!
	}

	@Deprecated
	public void push(BaseVolume target, Fraction pushed) {
		// Deprecated!
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.properties, this.fluid, this.fraction);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof FluidVolume)) return false;

		FluidVolume fluidVolume = (FluidVolume) object;

		return Objects.equal(this.properties, fluidVolume.properties) && Objects.equal(this.fluid, fluidVolume.fluid) && Objects.equal(this.fraction, fluidVolume.fraction);
	}

	@Override
	public String toString() {
		return "Volume{" + "fluid=" + this.fluid + ", fraction=" + this.fraction + '}';
	}
}
