package com.github.chainmailstudios.astromine.common.item;

import com.github.chainmailstudios.astromine.registry.AstromineSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireExtinguisherItem extends Item {
	long lastPlayed = 0;

	public FireExtinguisherItem(Item.Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		this.use(context.getWorld(), context.getPlayer(), context.getHand());

		return ActionResult.PASS;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		Vec3d placeVec = user.getCameraPosVec(0);

		Vec3d thrustVec = new Vec3d(0.8, 0.8, 0.8);

		thrustVec = thrustVec.multiply(user.getRotationVector());

		for (int i = 0; i < world.random.nextInt(64); ++i) {
			float r = world.random.nextFloat();
			world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, placeVec.x + thrustVec.x, placeVec.y + thrustVec.y, placeVec.z + thrustVec.z, thrustVec.x * r, thrustVec.y * r, thrustVec.z * r);
		}

		thrustVec = thrustVec.multiply(-1);

		if (!user.isSneaking()) {
			user.addVelocity(thrustVec.x, thrustVec.y, thrustVec.z);
			user.getItemCooldownManager().set(this, 10);
		} else {
			user.getItemCooldownManager().set(this, 2);
		}

		BlockHitResult result = (BlockHitResult) user.rayTrace(6, 0, false);

		BlockPos.Mutable.method_29715(new Box(result.getBlockPos()).expand(1)).forEach(position -> {
			BlockState state = world.getBlockState(position);

			if (state.getBlock() instanceof FireBlock) {
				world.setBlockState(position, Blocks.AIR.getDefaultState());
			} else if (state.getBlock() instanceof CampfireBlock) {
				if (state.get(CampfireBlock.LIT)) world.setBlockState(position, state.with(CampfireBlock.LIT, false));
			}
		});

		world.getEntities(null, new Box(result.getBlockPos()).expand(1)).forEach(entity -> {
			entity.setFireTicks(0);
		});

		if (world.isClient) {
			world.playSound(user, user.getBlockPos(), AstromineSoundEvents.FIRE_EXTINGUISHER_OPEN, SoundCategory.PLAYERS, 1f, 1f);
		}

		return super.use(world, user, hand);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		this.use(user.world, user, hand);

		return ActionResult.PASS;
	}
}
