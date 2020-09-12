/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.technologies.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentFluidInventoryBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.handler.FluidHandler;
import com.github.chainmailstudios.astromine.common.volume.handler.ItemHandler;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.FluidSizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.TierProvider;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public abstract class TankBlockEntity extends ComponentFluidInventoryBlockEntity implements TierProvider, FluidSizeProvider, SpeedProvider {
	public TankBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	protected FluidInventoryComponent createFluidComponent() {
		FluidInventoryComponent fluidComponent = new SimpleFluidInventoryComponent(1);
		FluidHandler.of(fluidComponent).getFirst().setSize(getFluidSize());
		return fluidComponent;
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(2);
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null) return;
		if (world.isClient) return;

		FluidHandler.ofOptional(this).ifPresent(fluids -> {
			ItemHandler.ofOptional(this).ifPresent(items -> {
				FluidHandler.ofOptional(items.getFirst()).ifPresent(stackFluids -> {
					FluidVolume ourVolume = fluids.getFirst();
					FluidVolume stackVolume = stackFluids.getFirst();
					
					if (ourVolume.canAccept(stackVolume.getFluid())) {
						if (items.getFirst().getItem() instanceof BucketItem) {
							if (items.getFirst().getItem() != Items.BUCKET && items.getFirst().getCount() == 1) {
								if (ourVolume.hasAvailable(Fraction.bucket())) {
									ourVolume.moveFrom(stackVolume, Fraction.bucket());

									items.setFirst(new ItemStack(Items.BUCKET));
								}
							}
						} else {
							ourVolume.moveFrom(stackVolume, Fraction.ofDecimal(getMachineSpeed()));
						}
					}
				});

				FluidHandler.ofOptional(items.getSecond()).ifPresent(stackFluids -> {
					FluidVolume ourVolume = fluids.getFirst();
					FluidVolume stackVolume = stackFluids.getFirst();

					if (ourVolume.canAccept(stackVolume.getFluid())) {
						if (items.getSecond().getItem() instanceof BucketItem) {
							if (items.getSecond().getItem() == Items.BUCKET && items.getSecond().getCount() == 1) {
								if (ourVolume.hasStored(Fraction.bucket())) {
									ourVolume.add(stackVolume, Fraction.bucket());

									items.setSecond(new ItemStack(stackVolume.getFluid().getBucketItem()));
								}
							}
						} else {
							ourVolume.add(stackVolume, Fraction.ofDecimal(getMachineSpeed()));
						}
					}
				});
			});
		});
	}

	public static class Primitive extends TankBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlockEntityTypes.PRIMITIVE_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitiveTankSpeed;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().primitiveTankFluid, 1);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends TankBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlockEntityTypes.BASIC_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicTankSpeed;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().basicTankFluid, 1);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends TankBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlockEntityTypes.ADVANCED_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedTankSpeed;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().advancedTankFluid, 1);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends TankBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlockEntityTypes.ELITE_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().eliteTankSpeed;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().eliteTankFluid, 1);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}

	public static class Creative extends TankBlockEntity {
		public Creative() {
			super(AstromineTechnologiesBlockEntityTypes.CREATIVE_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return Double.MAX_VALUE;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(Long.MAX_VALUE);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.CREATIVE;
		}

		@Override
		public void tick() {
			super.tick();

			FluidHandler.ofOptional(fluidComponent).ifPresent(fluids -> {
				fluids.getFirst().setAmount(Fraction.of(Long.MAX_VALUE));
				fluids.getFirst().setSize(Fraction.of(Long.MAX_VALUE));
			});
		}
	}
}
