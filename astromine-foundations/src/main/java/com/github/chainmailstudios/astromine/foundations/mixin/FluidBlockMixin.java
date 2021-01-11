package com.github.chainmailstudios.astromine.foundations.mixin;

import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin {
	@Shadow
	@Final
	protected FlowableFluid fluid;

	@Shadow
	protected abstract void playExtinguishSound(WorldAccess world, BlockPos pos);

	@Inject(method = "receiveNeighborFluids", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
	private void astromine_receiveNeighborFluids(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (this.fluid.isIn(AstromineFoundationsTags.OXYGEN)) {
			if (astromine_tryExplodeGas(world, pos, AstromineFoundationsTags.HYDROGEN)) {
				cir.setReturnValue(false);
			}
		} else if (this.fluid.isIn(AstromineFoundationsTags.HYDROGEN)) {
			if (astromine_tryExplodeGas(world, pos, AstromineFoundationsTags.OXYGEN)) {
				cir.setReturnValue(false);
			}
		}
	}

	private boolean astromine_tryExplodeGas(World world, BlockPos pos, Tag<Fluid> tag) {
		for(Direction direction : Direction.values()) {
			if (direction != Direction.DOWN) {
				BlockPos neighbourPos = pos.offset(direction);
				FluidState neighbourFluid = world.getFluidState(neighbourPos);
				if (neighbourFluid.isIn(tag)) {
					astromine_explodeGas(world, pos, neighbourPos, world.getFluidState(pos), neighbourFluid);
					return true;
				}
			}
		}
		return false;
	}

	private void astromine_explodeGas(World world, BlockPos posA, BlockPos posB, FluidState fluidA, FluidState fluidB) {
		float power = 4F / (32F / (fluidA.getLevel() + fluidB.getLevel() + 2));
		double x = posA.getX() == posB.getX() ? posA.getX() + 0.5 : (posA.getX() + posB.getX()) / 2.0;
		double y = posA.getY() == posB.getY() ? posA.getY() + 0.5 : (posA.getY() + posB.getY()) / 2.0;
		double z = posA.getZ() == posB.getZ() ? posA.getZ() + 0.5 : (posA.getZ() + posB.getZ()) / 2.0;
		world.createExplosion(null, x, y, z, power, true, Explosion.DestructionType.BREAK);
		world.setBlockState(posA, astromine_convertToWater(fluidA));
		world.setBlockState(posB, astromine_convertToWater(fluidB));
		this.playExtinguishSound(world, posA);
	}

	private BlockState astromine_convertToWater(FluidState state) {
		int level = state.isStill() ? 0 : 8 - Math.min(state.getLevel(), 8) + (state.get(FlowableFluid.FALLING) ? 8 : 0);
		return Blocks.WATER.getDefaultState().with(FluidBlock.LEVEL, level);
	}
}
