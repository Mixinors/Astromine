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

package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.component.world.WorldBridgeComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class HolographicBridgeInvisibleBlock extends Block {
	public static final Material MATERIAL = new Material.Builder(MaterialColor.CLEAR).build();

	public HolographicBridgeInvisibleBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos position, ShapeContext context) {
		return context.isHolding(Items.DEBUG_STICK) ? getCollisionShape(state, world, position, context) : VoxelShapes.empty();
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos position, ShapeContext context) {
		if (!(world instanceof World)) {
			return VoxelShapes.empty();
		} else {
			ComponentProvider componentProvider = ComponentProvider.fromWorld((World) world);

			WorldBridgeComponent bridgeComponent = componentProvider.getComponent(AstromineComponentTypes.WORLD_BRIDGE_COMPONENT);

			return bridgeComponent.getShape(position);
		}
	}
}
