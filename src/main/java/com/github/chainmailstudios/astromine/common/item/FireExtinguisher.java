package com.github.chainmailstudios.astromine.common.item;

import com.github.chainmailstudios.astromine.registry.AstromineItemGroups;
import com.github.chainmailstudios.astromine.registry.AstromineSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

public class FireExtinguisher extends Item {
	public static final Item.Settings SETTINGS = new Item.Settings().maxCount(1).group(AstromineItemGroups.ASTROMINE);

	public FireExtinguisher() {
		super(SETTINGS);
	}

	long lastPlayed = 0;

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		Vec3d placeVec = user.getCameraPosVec(0);

		Vec3d thrustVec = new Vec3d(0.5, 0.5, 0.5);

		thrustVec = thrustVec.multiply(user.getRotationVector());

		for (int i = 0; i < world.random.nextInt(64); ++i) {
			float r = world.random.nextFloat();
			world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, placeVec.x + thrustVec.x, placeVec.y + thrustVec.y, placeVec.z + thrustVec.z, thrustVec.x * r, thrustVec.y * r, thrustVec.z * r);
		}

		thrustVec = thrustVec.multiply(-1);

		if (!user.isSneaking()) {
			user.addVelocity(thrustVec.x, thrustVec.y, thrustVec.z);
		}

		BlockHitResult result = (BlockHitResult) user.rayTrace(6, 0, false);

		BlockPos.Mutable.method_29715(new Box(result.getBlockPos()).expand(1)).forEach(position -> {
			BlockState state = world.getBlockState(position);

			state.getEntries().keySet().stream().filter(property -> property == FireBlock.EAST || property == FireBlock.WEST || property == FireBlock.NORTH || property == FireBlock.SOUTH || property == FireBlock.UP).forEach(property ->
					world.setBlockState(position, Blocks.AIR.getDefaultState()));
		});

		world.getEntities(null, new Box(result.getBlockPos()).expand(1)).forEach(entity -> {
			entity.setFireTicks(0);
		});

		if (world.isClient) {
			world.playSound(user, user.getBlockPos(), AstromineSounds.FIRE_EXTINGUISHER_OPEN, SoundCategory.PLAYERS, 1f, 1f);
		}

		return super.use(world, user, hand);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		use(user.world, user, hand);

		return ActionResult.PASS;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		use(context.getWorld(), context.getPlayer(), context.getHand());

		return ActionResult.PASS;
	}
}
